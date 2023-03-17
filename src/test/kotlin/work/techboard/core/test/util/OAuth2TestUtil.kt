package work.techboard.core.test.util

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import work.techboard.core.security.ADMIN
import work.techboard.core.security.extractAuthorityFromClaims
import java.time.Instant
import java.time.temporal.ChronoUnit

const val TEST_USER_LOGIN = "test"

const val ID_TOKEN =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
        ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsIm" +
        "p0aSI6ImQzNWRmMTRkLTA5ZjYtNDhmZi04YTkzLTdjNmYwMzM5MzE1OSIsImlhdCI6MTU0M" +
        "Tk3MTU4MywiZXhwIjoxNTQxOTc1MTgzfQ.QaQOarmV8xEUYV7yvWzX3cUE_4W1luMcWCwpr" +
        "oqqUrg"

fun testAuthenticationToken(): OAuth2AuthenticationToken {
    val claims = mutableMapOf<String, Any>(
        "sub" to TEST_USER_LOGIN,
        "preferred_username" to TEST_USER_LOGIN,
        "email" to "john.doe@jhipster.com",
        "roles" to ADMIN
    )
    return authenticationToken(claims)
}

fun authenticationToken(claims: MutableMap<String, Any>): OAuth2AuthenticationToken {
    var issuedAt = Instant.now()
    var expiresAt = Instant.now().plus(1, ChronoUnit.DAYS)
    if (!claims.containsKey("sub")) {
        claims["sub"] = "jane"
    }
    if (!claims.containsKey("preferred_username")) {
        claims["preferred_username"] = "jane"
    }
    if (!claims.containsKey("email")) {
        claims["email"] = "jane.doe@jhipster.com"
    }
    if (claims.containsKey("auth_time")) {
        issuedAt = claims["auth_time"] as Instant
    } else {
        claims["auth_time"] = issuedAt
    }
    if (claims.containsKey("exp")) {
        expiresAt = claims["exp"] as Instant
    } else {
        claims["exp"] = expiresAt
    }
    val authorities = extractAuthorityFromClaims(claims)
    val token = OidcIdToken(ID_TOKEN, issuedAt, expiresAt, claims)
    val userInfo = OidcUserInfo(claims)
    val user = DefaultOidcUser(authorities, token, userInfo, "preferred_username")
    return OAuth2AuthenticationToken(user, user.authorities, "oidc")
}
fun registerAuthenticationToken(authorizedClientService: OAuth2AuthorizedClientService, clientRegistration: ClientRegistration, authentication: OAuth2AuthenticationToken): OAuth2AuthenticationToken {
    val userDetails = authentication.principal.attributes

    val token = OAuth2AccessToken(TokenType.BEARER, "Token", userDetails.get("auth_time") as Instant, userDetails.get("exp") as Instant)

    authorizedClientService.saveAuthorizedClient(
        OAuth2AuthorizedClient(clientRegistration, authentication.name, token),
        authentication
    )

    return authentication
}
