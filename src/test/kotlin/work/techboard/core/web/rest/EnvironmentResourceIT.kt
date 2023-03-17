package work.techboard.core.web.rest


import work.techboard.core.IntegrationTest
import work.techboard.core.domain.Environment
import work.techboard.core.repository.EnvironmentRepository
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*



/**
 * Integration tests for the [EnvironmentResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnvironmentResourceIT {
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

    private lateinit var environment: Environment



    @BeforeEach
    fun initTest() {
        environment = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createEnvironment() {
        val databaseSizeBeforeCreate = environmentRepository.findAll().size
        // Create the Environment
        restEnvironmentMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(environment))
        ).andExpect(status().isCreated)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeCreate + 1)
        val testEnvironment = environmentList[environmentList.size - 1]

        assertThat(testEnvironment.name).isEqualTo(DEFAULT_NAME)
        assertThat(testEnvironment.label).isEqualTo(DEFAULT_LABEL)
        assertThat(testEnvironment.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testEnvironment.color).isEqualTo(DEFAULT_COLOR)
        assertThat(testEnvironment.level).isEqualTo(DEFAULT_LEVEL)
        assertThat(testEnvironment.link).isEqualTo(DEFAULT_LINK)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createEnvironmentWithExistingId() {
        // Create the Environment with an existing ID
        environment.id = 1L

        val databaseSizeBeforeCreate = environmentRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restEnvironmentMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(environment))
        ).andExpect(status().isBadRequest)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = environmentRepository.findAll().size
        // set the field null
        environment.name = null

        // Create the Environment, which fails.

        restEnvironmentMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(environment))
        ).andExpect(status().isBadRequest)

        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkLabelIsRequired() {
        val databaseSizeBeforeTest = environmentRepository.findAll().size
        // set the field null
        environment.label = null

        // Create the Environment, which fails.

        restEnvironmentMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(environment))
        ).andExpect(status().isBadRequest)

        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkDescriptionIsRequired() {
        val databaseSizeBeforeTest = environmentRepository.findAll().size
        // set the field null
        environment.description = null

        // Create the Environment, which fails.

        restEnvironmentMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(environment))
        ).andExpect(status().isBadRequest)

        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkColorIsRequired() {
        val databaseSizeBeforeTest = environmentRepository.findAll().size
        // set the field null
        environment.color = null

        // Create the Environment, which fails.

        restEnvironmentMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(environment))
        ).andExpect(status().isBadRequest)

        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkLevelIsRequired() {
        val databaseSizeBeforeTest = environmentRepository.findAll().size
        // set the field null
        environment.level = null

        // Create the Environment, which fails.

        restEnvironmentMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(environment))
        ).andExpect(status().isBadRequest)

        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllEnvironments() {
        // Initialize the database
        environmentRepository.saveAndFlush(environment)

        // Get all the environmentList
        restEnvironmentMockMvc.perform(get(ENTITY_API_URL+ "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(environment.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))    }
    
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getEnvironment() {
        // Initialize the database
        environmentRepository.saveAndFlush(environment)

        val id = environment.id
        assertNotNull(id)

        // Get the environment
        restEnvironmentMockMvc.perform(get(ENTITY_API_URL_ID, environment.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(environment.id?.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingEnvironment() {
        // Get the environment
        restEnvironmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingEnvironment() {
        // Initialize the database
        environmentRepository.saveAndFlush(environment)

        val databaseSizeBeforeUpdate = environmentRepository.findAll().size

        // Update the environment
        val updatedEnvironment = environmentRepository.findById(environment.id).get()
        // Disconnect from session so that the updates on updatedEnvironment are not directly saved in db
        em.detach(updatedEnvironment)
        updatedEnvironment.name = UPDATED_NAME
        updatedEnvironment.label = UPDATED_LABEL
        updatedEnvironment.description = UPDATED_DESCRIPTION
        updatedEnvironment.color = UPDATED_COLOR
        updatedEnvironment.level = UPDATED_LEVEL
        updatedEnvironment.link = UPDATED_LINK

        restEnvironmentMockMvc.perform(
            put(ENTITY_API_URL_ID, updatedEnvironment.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedEnvironment))
        ).andExpect(status().isOk)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
        val testEnvironment = environmentList[environmentList.size - 1]
        assertThat(testEnvironment.name).isEqualTo(UPDATED_NAME)
        assertThat(testEnvironment.label).isEqualTo(UPDATED_LABEL)
        assertThat(testEnvironment.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testEnvironment.color).isEqualTo(UPDATED_COLOR)
        assertThat(testEnvironment.level).isEqualTo(UPDATED_LEVEL)
        assertThat(testEnvironment.link).isEqualTo(UPDATED_LINK)
    }

    @Test
    @Transactional
    fun putNonExistingEnvironment() {
        val databaseSizeBeforeUpdate = environmentRepository.findAll().size
        environment.id = count.incrementAndGet()


        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc.perform(put(ENTITY_API_URL_ID, environment.id).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(environment)))
            .andExpect(status().isBadRequest)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchEnvironment() {
        val databaseSizeBeforeUpdate = environmentRepository.findAll().size
        environment.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(environment))
        ).andExpect(status().isBadRequest)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamEnvironment() {
        val databaseSizeBeforeUpdate = environmentRepository.findAll().size
        environment.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc.perform(put(ENTITY_API_URL).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(environment)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateEnvironmentWithPatch() {
        environmentRepository.saveAndFlush(environment)
        
        
val databaseSizeBeforeUpdate = environmentRepository.findAll().size

// Update the environment using partial update
val partialUpdatedEnvironment = Environment().apply {
    id = environment.id

    
        name = UPDATED_NAME
        label = UPDATED_LABEL
        description = UPDATED_DESCRIPTION
        color = UPDATED_COLOR
        level = UPDATED_LEVEL
}


restEnvironmentMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedEnvironment.id).with(csrf())
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedEnvironment)))
.andExpect(status().isOk)

// Validate the Environment in the database
val environmentList = environmentRepository.findAll()
assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
val testEnvironment = environmentList.last()
    assertThat(testEnvironment.name).isEqualTo(UPDATED_NAME)
    assertThat(testEnvironment.label).isEqualTo(UPDATED_LABEL)
    assertThat(testEnvironment.description).isEqualTo(UPDATED_DESCRIPTION)
    assertThat(testEnvironment.color).isEqualTo(UPDATED_COLOR)
    assertThat(testEnvironment.level).isEqualTo(UPDATED_LEVEL)
    assertThat(testEnvironment.link).isEqualTo(DEFAULT_LINK)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateEnvironmentWithPatch() {
        environmentRepository.saveAndFlush(environment)
        
        
val databaseSizeBeforeUpdate = environmentRepository.findAll().size

// Update the environment using partial update
val partialUpdatedEnvironment = Environment().apply {
    id = environment.id

    
        name = UPDATED_NAME
        label = UPDATED_LABEL
        description = UPDATED_DESCRIPTION
        color = UPDATED_COLOR
        level = UPDATED_LEVEL
        link = UPDATED_LINK
}


restEnvironmentMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedEnvironment.id).with(csrf())
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedEnvironment)))
.andExpect(status().isOk)

// Validate the Environment in the database
val environmentList = environmentRepository.findAll()
assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
val testEnvironment = environmentList.last()
    assertThat(testEnvironment.name).isEqualTo(UPDATED_NAME)
    assertThat(testEnvironment.label).isEqualTo(UPDATED_LABEL)
    assertThat(testEnvironment.description).isEqualTo(UPDATED_DESCRIPTION)
    assertThat(testEnvironment.color).isEqualTo(UPDATED_COLOR)
    assertThat(testEnvironment.level).isEqualTo(UPDATED_LEVEL)
    assertThat(testEnvironment.link).isEqualTo(UPDATED_LINK)
    }

    @Throws(Exception::class)
    fun patchNonExistingEnvironment() {
        val databaseSizeBeforeUpdate = environmentRepository.findAll().size
        environment.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc.perform(patch(ENTITY_API_URL_ID, environment.id).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(environment)))
            .andExpect(status().isBadRequest)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchEnvironment() {
        val databaseSizeBeforeUpdate = environmentRepository.findAll().size
        environment.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(environment)))
            .andExpect(status().isBadRequest)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamEnvironment() {
        val databaseSizeBeforeUpdate = environmentRepository.findAll().size
        environment.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc.perform(patch(ENTITY_API_URL).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(environment)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Environment in the database
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteEnvironment() {
        // Initialize the database
        environmentRepository.saveAndFlush(environment)
        val databaseSizeBeforeDelete = environmentRepository.findAll().size
        // Delete the environment
        restEnvironmentMockMvc.perform(
            delete(ENTITY_API_URL_ID, environment.id).with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val environmentList = environmentRepository.findAll()
        assertThat(environmentList).hasSize(databaseSizeBeforeDelete - 1)
    }


    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_LABEL = "AAAAAAAAAA"
        private const val UPDATED_LABEL = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_COLOR = "AAAAAAAAAA"
        private const val UPDATED_COLOR = "BBBBBBBBBB"

        private const val DEFAULT_LEVEL: Int = 1
        private const val UPDATED_LEVEL: Int = 2

        private const val DEFAULT_LINK = "AAAAAAAAAA"
        private const val UPDATED_LINK = "BBBBBBBBBB"


        private val ENTITY_API_URL: String = "/api/environments"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + ( 2 * Integer.MAX_VALUE ))




        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Environment {
            val environment = Environment(
                name = DEFAULT_NAME,

                label = DEFAULT_LABEL,

                description = DEFAULT_DESCRIPTION,

                color = DEFAULT_COLOR,

                level = DEFAULT_LEVEL,

                link = DEFAULT_LINK

            )


            return environment
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Environment {
            val environment = Environment(
                name = UPDATED_NAME,

                label = UPDATED_LABEL,

                description = UPDATED_DESCRIPTION,

                color = UPDATED_COLOR,

                level = UPDATED_LEVEL,

                link = UPDATED_LINK

            )


            return environment
        }

    }
}
