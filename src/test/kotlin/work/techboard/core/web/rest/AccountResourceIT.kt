package work.techboard.core.web.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.test.context.TestSecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.IntegrationTest
import work.techboard.core.security.ADMIN
import work.techboard.core.test.util.TEST_USER_LOGIN
import work.techboard.core.test.util.registerAuthenticationToken
import work.techboard.core.test.util.testAuthenticationToken

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(value = TEST_USER_LOGIN)
@IntegrationTest
class AccountResourceIT {

    @Autowired
    private lateinit var restAccountMockMvc: MockMvc

    @Autowired
    private lateinit var authorizedClientService: OAuth2AuthorizedClientService

    @Autowired
    private lateinit var clientRegistration: ClientRegistration

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testGetExistingAccount() {
        TestSecurityContextHolder
            .getContext()
            .setAuthentication(registerAuthenticationToken(authorizedClientService, clientRegistration, testAuthenticationToken()))

        restAccountMockMvc.perform(
            get("/api/account")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("\$.login").value(TEST_USER_LOGIN))
            .andExpect(jsonPath("\$.email").value("john.doe@jhipster.com"))
            .andExpect(jsonPath("\$.authorities").value(ADMIN))
    }

    @Test
    @Throws(Exception::class)
    fun testGetUnknownAccount() {
        restAccountMockMvc.perform(
            get("/api/account")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isInternalServerError())
    }

    @Test
    @WithUnauthenticatedMockUser
    @Throws(Exception::class)
    fun testNonAuthenticatedUser() {
        restAccountMockMvc.perform(
            get("/api/authenticate")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))
    }

    @Test
    @Throws(Exception::class)
    fun testAuthenticatedUser() {
        restAccountMockMvc.perform(
            get("/api/authenticate")
                .with { request ->
                    request.remoteUser = TEST_USER_LOGIN
                    request
                }
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(TEST_USER_LOGIN))
    }
}
