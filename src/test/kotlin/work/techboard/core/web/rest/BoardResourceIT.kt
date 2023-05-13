package work.techboard.core.web.rest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.IntegrationTest
import java.util.*

/**
 * Integration tests for the [EnvironmentResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoardResourceIT {
    @Autowired
    private lateinit var restEnvironmentMockMvc: MockMvc

    companion object {
        private const val BOARD_API_URL: String = "/api/board/environments"
    }
    @BeforeEach
    fun initTest() {
    }

    @Test
    @Transactional
    fun getAllEnvironments() {
        // Initialize the database from init-data.xml

        // Get all the environmentList
        val result = restEnvironmentMockMvc.perform(get(BOARD_API_URL))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[0].id").value(11001))
            .andExpect(jsonPath("$.[0].name").value("Integration"))
            .andExpect(jsonPath("$.[0].activities.[0].name").value("Resolve"))
            .andExpect(jsonPath("$.[0].activities.[0].startedOn").value("2023-05-01T14:10:00Z"))
            .andExpect(jsonPath("$.[0].activities.[0].finishedOn").doesNotExist())
            .andExpect(jsonPath("$.[0].activities.[0].tags.[0].tag").value("Deploy"))
            .andExpect(jsonPath("$.[0].activities.[0].tags.[1].tag").value("Fix"))
            .andExpect(jsonPath("$.[0].activities.[0].owner.login").value("kepler"))
            .andExpect(jsonPath("$.[1].id").value(11002))
            .andExpect(jsonPath("$.[1].name").value("UAT"))
            .andExpect(jsonPath("$.[2].id").value(11003))
            .andExpect(jsonPath("$.[2].name").value("Production"))
            .andExpect(jsonPath("$.[2].activities").doesNotExist())
            .andReturn()
    }

    @Test
    @Transactional
    fun getOneEnvironment() {
        // Initialize the database from init-data.xml

        // Get one env
        val result = restEnvironmentMockMvc.perform(get(BOARD_API_URL + "/11001"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(11001))
            .andExpect(jsonPath("$.name").value("Integration"))
            .andExpect(jsonPath("$.activities.[0].name").value("Resolve"))
            .andExpect(jsonPath("$.activities.[0].startedOn").value("2023-05-01T14:10:00Z"))
            .andExpect(jsonPath("$.activities.[0].tags.[0].tag").value("Deploy"))
            .andExpect(jsonPath("$.activities.[0].tags.[1].tag").value("Fix"))
            .andExpect(jsonPath("$.activities.[0].owner.login").value("kepler"))
            .andReturn()
    }
}
