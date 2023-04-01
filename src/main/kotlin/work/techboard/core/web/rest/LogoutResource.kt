package work.techboard.core.web.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * REST controller for managing global OIDC logout.
 */
@RestController
class LogoutResource(
    private val registrations: ClientRegistrationRepository
) {
    private val registration = registrations.findByRegistrationId("oidc")

    /**
     * `POST  /api/logout` : logout the current user.
     *
     * @param request the [HttpServletRequest].
     * @param idToken the ID token.
     * @return the [ResponseEntity] with status `200 (OK)` and a body with a global logout URL.
     */
    @PostMapping("/api/logout")
    fun logout(
        request: HttpServletRequest,
        @AuthenticationPrincipal(expression = "idToken") idToken: OidcIdToken?
    ): ResponseEntity<*> {
        val logoutUrl = StringBuilder()

        val issuerUri = registration.providerDetails.issuerUri
        if (issuerUri.contains("auth0.com")) {
            val iUrl = if (issuerUri.endsWith("/")) {
                issuerUri + "v2/logout"
            } else {
                issuerUri + "/v2/logout"
            }
            logoutUrl.append(iUrl)
        } else {
            logoutUrl.append(registration.providerDetails.configurationMetadata["end_session_endpoint"].toString())
        }

        val originUrl = request.getHeader(HttpHeaders.ORIGIN)

        if (issuerUri.contains("auth0.com")) {
            logoutUrl.append("?client_id=").append(registration.clientId).append("&returnTo=").append(originUrl)
        } else {
            logoutUrl.append("?id_token_hint=").append(idToken?.tokenValue).append("&post_logout_redirect_uri=").append(originUrl)
        }

        val logoutDetails = mutableMapOf(
            "logoutUrl" to logoutUrl.toString()
        )
        request.session.invalidate()
        return ResponseEntity.ok().body(logoutDetails)
    }
}
