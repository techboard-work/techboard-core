package work.techboard.core.web.rest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import work.techboard.core.IntegrationTest
import work.techboard.core.security.USER
import work.techboard.core.test.util.ID_TOKEN
import work.techboard.core.test.util.authenticationToken
import work.techboard.core.test.util.registerAuthenticationToken

/**
 * Integration tests for the [LogoutResource] REST controller.
 */
@IntegrationTest
class LogoutResourceIT {

    @Autowired
    private lateinit var registrations: ClientRegistrationRepository

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var authorizedClientService: OAuth2AuthorizedClientService

    @Autowired
    private lateinit var clientRegistration: ClientRegistration

    private lateinit var restLogoutMockMvc: MockMvc

    private lateinit var claims: Map<String, Any>

    @BeforeEach
    @Throws(Exception::class)
    fun before() {
        val claims = mapOf(
            "groups" to listOf(USER),
            "sub" to 123
        )
        SecurityContextHolder.getContext().authentication = registerAuthenticationToken(authorizedClientService, clientRegistration, authenticationToken(claims.toMutableMap()))
        val authInjector = SecurityContextHolderAwareRequestFilter()
        authInjector.afterPropertiesSet()

        restLogoutMockMvc = MockMvcBuilders.webAppContextSetup(this.context).build()
    }

    @Test
    @Throws(Exception::class)
    fun getLogoutInformation() {
        val ORIGIN_URL = "http://localhost:8080"
        var logoutUrl = this.registrations.findByRegistrationId("oidc").providerDetails
            .configurationMetadata["end_session_endpoint"].toString()
        logoutUrl = "$logoutUrl?id_token_hint=$ID_TOKEN&post_logout_redirect_uri=$ORIGIN_URL"
        restLogoutMockMvc.perform(post("http://localhost:8080/api/logout").header(HttpHeaders.ORIGIN, ORIGIN_URL))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("\$.logoutUrl").value(logoutUrl))
    }
}
