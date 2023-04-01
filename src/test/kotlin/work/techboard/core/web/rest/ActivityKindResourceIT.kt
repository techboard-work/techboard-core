package work.techboard.core.web.rest


import work.techboard.core.IntegrationTest
import work.techboard.core.domain.ActivityKind
import work.techboard.core.repository.ActivityKindRepository
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
 * Integration tests for the [ActivityKindResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivityKindResourceIT {
    @Autowired
    private lateinit var activityKindRepository: ActivityKindRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator


    @Autowired
    private lateinit var em: EntityManager


    @Autowired
    private lateinit var restActivityKindMockMvc: MockMvc

    private lateinit var activityKind: ActivityKind



    @BeforeEach
    fun initTest() {
        activityKind = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createActivityKind() {
        val databaseSizeBeforeCreate = activityKindRepository.findAll().size
        // Create the ActivityKind
        restActivityKindMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activityKind))
        ).andExpect(status().isCreated)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeCreate + 1)
        val testActivityKind = activityKindList[activityKindList.size - 1]

        assertThat(testActivityKind.name).isEqualTo(DEFAULT_NAME)
        assertThat(testActivityKind.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testActivityKind.color).isEqualTo(DEFAULT_COLOR)
        assertThat(testActivityKind.icon).isEqualTo(DEFAULT_ICON)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createActivityKindWithExistingId() {
        // Create the ActivityKind with an existing ID
        activityKind.id = 1L

        val databaseSizeBeforeCreate = activityKindRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityKindMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activityKind))
        ).andExpect(status().isBadRequest)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = activityKindRepository.findAll().size
        // set the field null
        activityKind.name = null

        // Create the ActivityKind, which fails.

        restActivityKindMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activityKind))
        ).andExpect(status().isBadRequest)

        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkDescriptionIsRequired() {
        val databaseSizeBeforeTest = activityKindRepository.findAll().size
        // set the field null
        activityKind.description = null

        // Create the ActivityKind, which fails.

        restActivityKindMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activityKind))
        ).andExpect(status().isBadRequest)

        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkColorIsRequired() {
        val databaseSizeBeforeTest = activityKindRepository.findAll().size
        // set the field null
        activityKind.color = null

        // Create the ActivityKind, which fails.

        restActivityKindMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activityKind))
        ).andExpect(status().isBadRequest)

        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllActivityKinds() {
        // Initialize the database
        activityKindRepository.saveAndFlush(activityKind)

        // Get all the activityKindList
        restActivityKindMockMvc.perform(get(ENTITY_API_URL+ "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityKind.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))    }
    
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getActivityKind() {
        // Initialize the database
        activityKindRepository.saveAndFlush(activityKind)

        val id = activityKind.id
        assertNotNull(id)

        // Get the activityKind
        restActivityKindMockMvc.perform(get(ENTITY_API_URL_ID, activityKind.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activityKind.id?.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingActivityKind() {
        // Get the activityKind
        restActivityKindMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingActivityKind() {
        // Initialize the database
        activityKindRepository.saveAndFlush(activityKind)

        val databaseSizeBeforeUpdate = activityKindRepository.findAll().size

        // Update the activityKind
        val updatedActivityKind = activityKindRepository.findById(activityKind.id).get()
        // Disconnect from session so that the updates on updatedActivityKind are not directly saved in db
        em.detach(updatedActivityKind)
        updatedActivityKind.name = UPDATED_NAME
        updatedActivityKind.description = UPDATED_DESCRIPTION
        updatedActivityKind.color = UPDATED_COLOR
        updatedActivityKind.icon = UPDATED_ICON

        restActivityKindMockMvc.perform(
            put(ENTITY_API_URL_ID, updatedActivityKind.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedActivityKind))
        ).andExpect(status().isOk)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
        val testActivityKind = activityKindList[activityKindList.size - 1]
        assertThat(testActivityKind.name).isEqualTo(UPDATED_NAME)
        assertThat(testActivityKind.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testActivityKind.color).isEqualTo(UPDATED_COLOR)
        assertThat(testActivityKind.icon).isEqualTo(UPDATED_ICON)
    }

    @Test
    @Transactional
    fun putNonExistingActivityKind() {
        val databaseSizeBeforeUpdate = activityKindRepository.findAll().size
        activityKind.id = count.incrementAndGet()


        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityKindMockMvc.perform(put(ENTITY_API_URL_ID, activityKind.id).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(activityKind)))
            .andExpect(status().isBadRequest)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchActivityKind() {
        val databaseSizeBeforeUpdate = activityKindRepository.findAll().size
        activityKind.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityKindMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activityKind))
        ).andExpect(status().isBadRequest)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamActivityKind() {
        val databaseSizeBeforeUpdate = activityKindRepository.findAll().size
        activityKind.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityKindMockMvc.perform(put(ENTITY_API_URL).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(activityKind)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateActivityKindWithPatch() {
        activityKindRepository.saveAndFlush(activityKind)
        
        
val databaseSizeBeforeUpdate = activityKindRepository.findAll().size

// Update the activityKind using partial update
val partialUpdatedActivityKind = ActivityKind().apply {
    id = activityKind.id

    
        name = UPDATED_NAME
        color = UPDATED_COLOR
}


restActivityKindMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedActivityKind.id).with(csrf())
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedActivityKind)))
.andExpect(status().isOk)

// Validate the ActivityKind in the database
val activityKindList = activityKindRepository.findAll()
assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
val testActivityKind = activityKindList.last()
    assertThat(testActivityKind.name).isEqualTo(UPDATED_NAME)
    assertThat(testActivityKind.description).isEqualTo(DEFAULT_DESCRIPTION)
    assertThat(testActivityKind.color).isEqualTo(UPDATED_COLOR)
    assertThat(testActivityKind.icon).isEqualTo(DEFAULT_ICON)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateActivityKindWithPatch() {
        activityKindRepository.saveAndFlush(activityKind)
        
        
val databaseSizeBeforeUpdate = activityKindRepository.findAll().size

// Update the activityKind using partial update
val partialUpdatedActivityKind = ActivityKind().apply {
    id = activityKind.id

    
        name = UPDATED_NAME
        description = UPDATED_DESCRIPTION
        color = UPDATED_COLOR
        icon = UPDATED_ICON
}


restActivityKindMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedActivityKind.id).with(csrf())
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedActivityKind)))
.andExpect(status().isOk)

// Validate the ActivityKind in the database
val activityKindList = activityKindRepository.findAll()
assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
val testActivityKind = activityKindList.last()
    assertThat(testActivityKind.name).isEqualTo(UPDATED_NAME)
    assertThat(testActivityKind.description).isEqualTo(UPDATED_DESCRIPTION)
    assertThat(testActivityKind.color).isEqualTo(UPDATED_COLOR)
    assertThat(testActivityKind.icon).isEqualTo(UPDATED_ICON)
    }

    @Throws(Exception::class)
    fun patchNonExistingActivityKind() {
        val databaseSizeBeforeUpdate = activityKindRepository.findAll().size
        activityKind.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityKindMockMvc.perform(patch(ENTITY_API_URL_ID, activityKind.id).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(activityKind)))
            .andExpect(status().isBadRequest)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchActivityKind() {
        val databaseSizeBeforeUpdate = activityKindRepository.findAll().size
        activityKind.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityKindMockMvc.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(activityKind)))
            .andExpect(status().isBadRequest)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamActivityKind() {
        val databaseSizeBeforeUpdate = activityKindRepository.findAll().size
        activityKind.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityKindMockMvc.perform(patch(ENTITY_API_URL).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(activityKind)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the ActivityKind in the database
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteActivityKind() {
        // Initialize the database
        activityKindRepository.saveAndFlush(activityKind)
        val databaseSizeBeforeDelete = activityKindRepository.findAll().size
        // Delete the activityKind
        restActivityKindMockMvc.perform(
            delete(ENTITY_API_URL_ID, activityKind.id).with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val activityKindList = activityKindRepository.findAll()
        assertThat(activityKindList).hasSize(databaseSizeBeforeDelete - 1)
    }


    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_COLOR = "AAAAAAAAAA"
        private const val UPDATED_COLOR = "BBBBBBBBBB"

        private const val DEFAULT_ICON = "AAAAAAAAAA"
        private const val UPDATED_ICON = "BBBBBBBBBB"


        private val ENTITY_API_URL: String = "/api/activity-kinds"
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
        fun createEntity(em: EntityManager): ActivityKind {
            val activityKind = ActivityKind(
                name = DEFAULT_NAME,

                description = DEFAULT_DESCRIPTION,

                color = DEFAULT_COLOR,

                icon = DEFAULT_ICON

            )


            return activityKind
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): ActivityKind {
            val activityKind = ActivityKind(
                name = UPDATED_NAME,

                description = UPDATED_DESCRIPTION,

                color = UPDATED_COLOR,

                icon = UPDATED_ICON

            )


            return activityKind
        }

    }
}
