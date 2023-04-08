package work.techboard.core.web.rest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import work.techboard.core.IntegrationTest
import work.techboard.core.service.UiConfigService

/**
 * Test class for the UiConfigResource REST controller.
 *
 * @see UiConfigResource
 */
@IntegrationTest
@Extensions(
    ExtendWith(MockitoExtension::class)
)
@AutoConfigureMockMvc
@WithMockUser
class UiConfigResourceIT {

    private lateinit var restMockMvc: MockMvc

    @Autowired
    private lateinit var uiConfigService: UiConfigService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val uiConfigResource = UiConfigResource(uiConfigService)
        restMockMvc = MockMvcBuilders
            .standaloneSetup(uiConfigResource)
            .build()
    }

    /**
     * Test get
     */
    @Test
    @Throws(Exception::class)
    fun testGet() {
        restMockMvc.perform(get("/api/ui-config/getUiConfig"))
            .andExpect(status().isOk)
    }
}
