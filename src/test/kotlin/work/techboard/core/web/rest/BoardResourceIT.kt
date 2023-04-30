package work.techboard.core.web.rest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import work.techboard.core.IntegrationTest
import work.techboard.core.repository.EnvironmentRepository
import java.util.*
import javax.persistence.EntityManager

/**
 * Integration tests for the [EnvironmentResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoardResourceIT {
    @Autowired
    private lateinit var environmentRepository: EnvironmentRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restEnvironmentMockMvc: MockMvc

    companion object {
        private val BOARD_API_URL: String = "/api/board/environments"
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
            .andReturn()
        // System.out.println(result)
    }
}
