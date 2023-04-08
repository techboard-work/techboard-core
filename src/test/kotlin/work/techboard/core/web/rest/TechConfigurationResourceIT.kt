package work.techboard.core.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import work.techboard.core.IntegrationTest
import work.techboard.core.domain.TechConfiguration
import work.techboard.core.repository.TechConfigurationRepository
import work.techboard.core.service.TechConfigurationService
import work.techboard.core.service.mapper.TechConfigurationMapper
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

/**
 * Integration tests for the [TechConfigurationResource] REST controller.
 */
@IntegrationTest
@Extensions(
    ExtendWith(MockitoExtension::class)
)
@AutoConfigureMockMvc
@WithMockUser
class TechConfigurationResourceIT {
    @Autowired
    private lateinit var techConfigurationRepository: TechConfigurationRepository

    @Mock
    private lateinit var techConfigurationRepositoryMock: TechConfigurationRepository

    @Autowired
    private lateinit var techConfigurationMapper: TechConfigurationMapper

    @Mock
    private lateinit var techConfigurationServiceMock: TechConfigurationService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restTechConfigurationMockMvc: MockMvc

    private lateinit var techConfiguration: TechConfiguration

    @BeforeEach
    fun initTest() {
        techConfiguration = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTechConfiguration() {
        val databaseSizeBeforeCreate = techConfigurationRepository.findAll().size
        // Create the TechConfiguration
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)
        restTechConfigurationMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        ).andExpect(status().isCreated)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeCreate + 1)
        val testTechConfiguration = techConfigurationList[techConfigurationList.size - 1]

        assertThat(testTechConfiguration.version).isEqualTo(DEFAULT_VERSION)
        assertThat(testTechConfiguration.timestamp).isEqualTo(DEFAULT_TIMESTAMP)
        assertThat(testTechConfiguration.content).isEqualTo(DEFAULT_CONTENT)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTechConfigurationWithExistingId() {
        // Create the TechConfiguration with an existing ID
        techConfiguration.id = 1L
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        val databaseSizeBeforeCreate = techConfigurationRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restTechConfigurationMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        ).andExpect(status().isBadRequest)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkVersionIsRequired() {
        val databaseSizeBeforeTest = techConfigurationRepository.findAll().size
        // set the field null
        techConfiguration.version = null

        // Create the TechConfiguration, which fails.
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        restTechConfigurationMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        ).andExpect(status().isBadRequest)

        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkTimestampIsRequired() {
        val databaseSizeBeforeTest = techConfigurationRepository.findAll().size
        // set the field null
        techConfiguration.timestamp = null

        // Create the TechConfiguration, which fails.
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        restTechConfigurationMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        ).andExpect(status().isBadRequest)

        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkContentIsRequired() {
        val databaseSizeBeforeTest = techConfigurationRepository.findAll().size
        // set the field null
        techConfiguration.content = null

        // Create the TechConfiguration, which fails.
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        restTechConfigurationMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        ).andExpect(status().isBadRequest)

        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTechConfigurations() {
        // Initialize the database
        techConfigurationRepository.saveAndFlush(techConfiguration)

        // Get all the techConfigurationList
        restTechConfigurationMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techConfiguration.id?.toInt())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllTechConfigurationsWithEagerRelationshipsIsEnabled() {
        `when`(techConfigurationServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restTechConfigurationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk)

        verify(techConfigurationRepositoryMock, times(1)).findAll(any(Pageable::class.java))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllTechConfigurationsWithEagerRelationshipsIsNotEnabled() {
        `when`(techConfigurationServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restTechConfigurationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk)

        verify(techConfigurationServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getTechConfiguration() {
        // Initialize the database
        techConfigurationRepository.saveAndFlush(techConfiguration)

        val id = techConfiguration.id
        assertNotNull(id)

        // Get the techConfiguration
        restTechConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, techConfiguration.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techConfiguration.id?.toInt()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingTechConfiguration() {
        // Get the techConfiguration
        restTechConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingTechConfiguration() {
        // Initialize the database
        techConfigurationRepository.saveAndFlush(techConfiguration)

        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size

        // Update the techConfiguration
        val updatedTechConfiguration = techConfigurationRepository.findById(techConfiguration.id).get()
        // Disconnect from session so that the updates on updatedTechConfiguration are not directly saved in db
        em.detach(updatedTechConfiguration)
        updatedTechConfiguration.version = UPDATED_VERSION
        updatedTechConfiguration.timestamp = UPDATED_TIMESTAMP
        updatedTechConfiguration.content = UPDATED_CONTENT
        val techConfigurationDTO = techConfigurationMapper.toDto(updatedTechConfiguration)

        restTechConfigurationMockMvc.perform(
            put(ENTITY_API_URL_ID, techConfigurationDTO.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        ).andExpect(status().isOk)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
        val testTechConfiguration = techConfigurationList[techConfigurationList.size - 1]
        assertThat(testTechConfiguration.version).isEqualTo(UPDATED_VERSION)
        assertThat(testTechConfiguration.timestamp).isEqualTo(UPDATED_TIMESTAMP)
        assertThat(testTechConfiguration.content).isEqualTo(UPDATED_CONTENT)
    }

    @Test
    @Transactional
    fun putNonExistingTechConfiguration() {
        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size
        techConfiguration.id = count.incrementAndGet()

        // Create the TechConfiguration
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechConfigurationMockMvc.perform(
            put(ENTITY_API_URL_ID, techConfigurationDTO.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchTechConfiguration() {
        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size
        techConfiguration.id = count.incrementAndGet()

        // Create the TechConfiguration
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechConfigurationMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        ).andExpect(status().isBadRequest)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamTechConfiguration() {
        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size
        techConfiguration.id = count.incrementAndGet()

        // Create the TechConfiguration
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechConfigurationMockMvc.perform(
            put(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateTechConfigurationWithPatch() {
        techConfigurationRepository.saveAndFlush(techConfiguration)

        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size

// Update the techConfiguration using partial update
        val partialUpdatedTechConfiguration = TechConfiguration().apply {
            id = techConfiguration.id

            version = UPDATED_VERSION
            timestamp = UPDATED_TIMESTAMP
        }

        restTechConfigurationMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedTechConfiguration.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedTechConfiguration))
        )
            .andExpect(status().isOk)

// Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
        val testTechConfiguration = techConfigurationList.last()
        assertThat(testTechConfiguration.version).isEqualTo(UPDATED_VERSION)
        assertThat(testTechConfiguration.timestamp).isEqualTo(UPDATED_TIMESTAMP)
        assertThat(testTechConfiguration.content).isEqualTo(DEFAULT_CONTENT)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateTechConfigurationWithPatch() {
        techConfigurationRepository.saveAndFlush(techConfiguration)

        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size

// Update the techConfiguration using partial update
        val partialUpdatedTechConfiguration = TechConfiguration().apply {
            id = techConfiguration.id

            version = UPDATED_VERSION
            timestamp = UPDATED_TIMESTAMP
            content = UPDATED_CONTENT
        }

        restTechConfigurationMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedTechConfiguration.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedTechConfiguration))
        )
            .andExpect(status().isOk)

// Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
        val testTechConfiguration = techConfigurationList.last()
        assertThat(testTechConfiguration.version).isEqualTo(UPDATED_VERSION)
        assertThat(testTechConfiguration.timestamp).isEqualTo(UPDATED_TIMESTAMP)
        assertThat(testTechConfiguration.content).isEqualTo(UPDATED_CONTENT)
    }

    @Throws(Exception::class)
    fun patchNonExistingTechConfiguration() {
        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size
        techConfiguration.id = count.incrementAndGet()

        // Create the TechConfiguration
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechConfigurationMockMvc.perform(
            patch(ENTITY_API_URL_ID, techConfigurationDTO.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchTechConfiguration() {
        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size
        techConfiguration.id = count.incrementAndGet()

        // Create the TechConfiguration
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechConfigurationMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamTechConfiguration() {
        val databaseSizeBeforeUpdate = techConfigurationRepository.findAll().size
        techConfiguration.id = count.incrementAndGet()

        // Create the TechConfiguration
        val techConfigurationDTO = techConfigurationMapper.toDto(techConfiguration)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechConfigurationMockMvc.perform(
            patch(ENTITY_API_URL).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(techConfigurationDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the TechConfiguration in the database
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteTechConfiguration() {
        // Initialize the database
        techConfigurationRepository.saveAndFlush(techConfiguration)
        val databaseSizeBeforeDelete = techConfigurationRepository.findAll().size
        // Delete the techConfiguration
        restTechConfigurationMockMvc.perform(
            delete(ENTITY_API_URL_ID, techConfiguration.id).with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val techConfigurationList = techConfigurationRepository.findAll()
        assertThat(techConfigurationList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_VERSION: Int = 0
        private const val UPDATED_VERSION: Int = 1

        private val DEFAULT_TIMESTAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_TIMESTAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_CONTENT = "AAAAAAAAAA"
        private const val UPDATED_CONTENT = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/tech-configurations"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): TechConfiguration {
            val techConfiguration = TechConfiguration(
                version = DEFAULT_VERSION,

                timestamp = DEFAULT_TIMESTAMP,

                content = DEFAULT_CONTENT

            )

            return techConfiguration
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): TechConfiguration {
            val techConfiguration = TechConfiguration(
                version = UPDATED_VERSION,

                timestamp = UPDATED_TIMESTAMP,

                content = UPDATED_CONTENT

            )

            return techConfiguration
        }
    }
}
