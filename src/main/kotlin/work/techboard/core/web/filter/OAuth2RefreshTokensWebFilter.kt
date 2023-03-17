package work.techboard.core.web.filter

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Refresh oauth2 tokens.
 */
@Component
class OAuth2RefreshTokensWebFilter(
    private val clientManager: OAuth2AuthorizedClientManager,
    private val authorizedClientRepository: OAuth2AuthorizedClientRepository,
    private val clientRegistrationRepository: ClientRegistrationRepository
) : OncePerRequestFilter() {

    private val authorizationRedirectStrategy = DefaultRedirectStrategy()
    private val authorizationRequestResolver =
        DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository,
            OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
        )

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        when (val authentication = SecurityContextHolder.getContext().authentication) {
            is OAuth2AuthenticationToken -> {
                try {
                    val authorizedClient = authorizedClient(authentication)
                    authorizedClientRepository.saveAuthorizedClient(
                        authorizedClient,
                        authentication,
                        request,
                        response
                    )
                } catch (e: Exception) {
                    val authorizedRequest = authorizationRequestResolver.resolve(request)
                    if (authorizedRequest != null) {
                        authorizationRedirectStrategy.sendRedirect(
                            request,
                            response,
                            authorizedRequest.authorizationRequestUri
                        )
                    }
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun authorizedClient(oauth2Authentication: OAuth2AuthenticationToken): OAuth2AuthorizedClient {
        val clientRegistrationId = oauth2Authentication.getAuthorizedClientRegistrationId()
        val request = OAuth2AuthorizeRequest
            .withClientRegistrationId(clientRegistrationId)
            .principal(oauth2Authentication)
            .build()
        if (clientManager == null) {
            throw IllegalStateException(
                "No OAuth2AuthorizedClientManager bean was found. Did you include the " +
                    "org.springframework.boot:spring-boot-starter-oauth2-client dependency?"
            )
        }
        return clientManager.authorize(request)
    }
}
