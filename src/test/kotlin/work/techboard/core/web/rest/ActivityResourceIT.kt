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
import work.techboard.core.domain.Activity
import work.techboard.core.domain.Environment
import work.techboard.core.repository.ActivityRepository
import work.techboard.core.service.ActivityService
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

/**
 * Integration tests for the [ActivityResource] REST controller.
 */
@IntegrationTest
@Extensions(
    ExtendWith(MockitoExtension::class)
)
@AutoConfigureMockMvc
@WithMockUser
class ActivityResourceIT {
    @Autowired
    private lateinit var activityRepository: ActivityRepository

    @Mock
    private lateinit var activityRepositoryMock: ActivityRepository

    @Mock
    private lateinit var activityServiceMock: ActivityService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restActivityMockMvc: MockMvc

    private lateinit var activity: Activity

    @BeforeEach
    fun initTest() {
        activity = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createActivity() {
        val databaseSizeBeforeCreate = activityRepository.findAll().size
        // Create the Activity
        restActivityMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activity))
        ).andExpect(status().isCreated)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeCreate + 1)
        val testActivity = activityList[activityList.size - 1]

        assertThat(testActivity.name).isEqualTo(DEFAULT_NAME)
        assertThat(testActivity.startedOn).isEqualTo(DEFAULT_STARTED_ON)
        assertThat(testActivity.finishedOn).isEqualTo(DEFAULT_FINISHED_ON)
        assertThat(testActivity.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testActivity.link).isEqualTo(DEFAULT_LINK)
        assertThat(testActivity.flagged).isEqualTo(DEFAULT_FLAGGED)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createActivityWithExistingId() {
        // Create the Activity with an existing ID
        activity.id = 1L

        val databaseSizeBeforeCreate = activityRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activity))
        ).andExpect(status().isBadRequest)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = activityRepository.findAll().size
        // set the field null
        activity.name = null

        // Create the Activity, which fails.

        restActivityMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activity))
        ).andExpect(status().isBadRequest)

        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkStartedOnIsRequired() {
        val databaseSizeBeforeTest = activityRepository.findAll().size
        // set the field null
        activity.startedOn = null

        // Create the Activity, which fails.

        restActivityMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activity))
        ).andExpect(status().isBadRequest)

        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkFlaggedIsRequired() {
        val databaseSizeBeforeTest = activityRepository.findAll().size
        // set the field null
        activity.flagged = null

        // Create the Activity, which fails.

        restActivityMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activity))
        ).andExpect(status().isBadRequest)

        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllActivities() {
        // Initialize the database
        activityRepository.saveAndFlush(activity)

        // Get all the activityList
        restActivityMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startedOn").value(hasItem(DEFAULT_STARTED_ON.toString())))
            .andExpect(jsonPath("$.[*].finishedOn").value(hasItem(DEFAULT_FINISHED_ON.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].flagged").value(hasItem(DEFAULT_FLAGGED)))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllActivitiesWithEagerRelationshipsIsEnabled() {
        `when`(activityServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk)

        verify(activityRepositoryMock, times(1)).findAll(any(Pageable::class.java))
    }

    @Suppress("unchecked")
    @Throws(Exception::class)
    fun getAllActivitiesWithEagerRelationshipsIsNotEnabled() {
        `when`(activityServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        restActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk)

        verify(activityServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getActivity() {
        // Initialize the database
        activityRepository.saveAndFlush(activity)

        val id = activity.id
        assertNotNull(id)

        // Get the activity
        restActivityMockMvc.perform(get(ENTITY_API_URL_ID, activity.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activity.id?.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startedOn").value(DEFAULT_STARTED_ON.toString()))
            .andExpect(jsonPath("$.finishedOn").value(DEFAULT_FINISHED_ON.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.flagged").value(DEFAULT_FLAGGED))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingActivity() {
        // Get the activity
        restActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingActivity() {
        // Initialize the database
        activityRepository.saveAndFlush(activity)

        val databaseSizeBeforeUpdate = activityRepository.findAll().size

        // Update the activity
        val updatedActivity = activityRepository.findById(activity.id).get()
        // Disconnect from session so that the updates on updatedActivity are not directly saved in db
        em.detach(updatedActivity)
        updatedActivity.name = UPDATED_NAME
        updatedActivity.startedOn = UPDATED_STARTED_ON
        updatedActivity.finishedOn = UPDATED_FINISHED_ON
        updatedActivity.description = UPDATED_DESCRIPTION
        updatedActivity.link = UPDATED_LINK
        updatedActivity.flagged = UPDATED_FLAGGED

        restActivityMockMvc.perform(
            put(ENTITY_API_URL_ID, updatedActivity.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedActivity))
        ).andExpect(status().isOk)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
        val testActivity = activityList[activityList.size - 1]
        assertThat(testActivity.name).isEqualTo(UPDATED_NAME)
        assertThat(testActivity.startedOn).isEqualTo(UPDATED_STARTED_ON)
        assertThat(testActivity.finishedOn).isEqualTo(UPDATED_FINISHED_ON)
        assertThat(testActivity.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testActivity.link).isEqualTo(UPDATED_LINK)
        assertThat(testActivity.flagged).isEqualTo(UPDATED_FLAGGED)
    }

    @Test
    @Transactional
    fun putNonExistingActivity() {
        val databaseSizeBeforeUpdate = activityRepository.findAll().size
        activity.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMockMvc.perform(
            put(ENTITY_API_URL_ID, activity.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activity))
        )
            .andExpect(status().isBadRequest)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchActivity() {
        val databaseSizeBeforeUpdate = activityRepository.findAll().size
        activity.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activity))
        ).andExpect(status().isBadRequest)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamActivity() {
        val databaseSizeBeforeUpdate = activityRepository.findAll().size
        activity.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc.perform(
            put(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(activity))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateActivityWithPatch() {
        activityRepository.saveAndFlush(activity)

        val databaseSizeBeforeUpdate = activityRepository.findAll().size

// Update the activity using partial update
        val partialUpdatedActivity = Activity().apply {
            id = activity.id

            startedOn = UPDATED_STARTED_ON
            description = UPDATED_DESCRIPTION
            flagged = UPDATED_FLAGGED
        }

        restActivityMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedActivity.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedActivity))
        )
            .andExpect(status().isOk)

// Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
        val testActivity = activityList.last()
        assertThat(testActivity.name).isEqualTo(DEFAULT_NAME)
        assertThat(testActivity.startedOn).isEqualTo(UPDATED_STARTED_ON)
        assertThat(testActivity.finishedOn).isEqualTo(DEFAULT_FINISHED_ON)
        assertThat(testActivity.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testActivity.link).isEqualTo(DEFAULT_LINK)
        assertThat(testActivity.flagged).isEqualTo(UPDATED_FLAGGED)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateActivityWithPatch() {
        activityRepository.saveAndFlush(activity)

        val databaseSizeBeforeUpdate = activityRepository.findAll().size

// Update the activity using partial update
        val partialUpdatedActivity = Activity().apply {
            id = activity.id

            name = UPDATED_NAME
            startedOn = UPDATED_STARTED_ON
            finishedOn = UPDATED_FINISHED_ON
            description = UPDATED_DESCRIPTION
            link = UPDATED_LINK
            flagged = UPDATED_FLAGGED
        }

        restActivityMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedActivity.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedActivity))
        )
            .andExpect(status().isOk)

// Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
        val testActivity = activityList.last()
        assertThat(testActivity.name).isEqualTo(UPDATED_NAME)
        assertThat(testActivity.startedOn).isEqualTo(UPDATED_STARTED_ON)
        assertThat(testActivity.finishedOn).isEqualTo(UPDATED_FINISHED_ON)
        assertThat(testActivity.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testActivity.link).isEqualTo(UPDATED_LINK)
        assertThat(testActivity.flagged).isEqualTo(UPDATED_FLAGGED)
    }

    @Throws(Exception::class)
    fun patchNonExistingActivity() {
        val databaseSizeBeforeUpdate = activityRepository.findAll().size
        activity.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMockMvc.perform(
            patch(ENTITY_API_URL_ID, activity.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(activity))
        )
            .andExpect(status().isBadRequest)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchActivity() {
        val databaseSizeBeforeUpdate = activityRepository.findAll().size
        activity.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(activity))
        )
            .andExpect(status().isBadRequest)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamActivity() {
        val databaseSizeBeforeUpdate = activityRepository.findAll().size
        activity.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc.perform(
            patch(ENTITY_API_URL).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(activity))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Activity in the database
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteActivity() {
        // Initialize the database
        activityRepository.saveAndFlush(activity)
        val databaseSizeBeforeDelete = activityRepository.findAll().size
        // Delete the activity
        restActivityMockMvc.perform(
            delete(ENTITY_API_URL_ID, activity.id).with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val activityList = activityRepository.findAll()
        assertThat(activityList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private val DEFAULT_STARTED_ON: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_STARTED_ON: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_FINISHED_ON: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_FINISHED_ON: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_LINK = "AAAAAAAAAA"
        private const val UPDATED_LINK = "BBBBBBBBBB"

        private const val DEFAULT_FLAGGED: Boolean = false
        private const val UPDATED_FLAGGED: Boolean = true

        private val ENTITY_API_URL: String = "/api/activities"
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
        fun createEntity(em: EntityManager): Activity {
            val activity = Activity(
                name = DEFAULT_NAME,

                startedOn = DEFAULT_STARTED_ON,

                finishedOn = DEFAULT_FINISHED_ON,

                description = DEFAULT_DESCRIPTION,

                link = DEFAULT_LINK,

                flagged = DEFAULT_FLAGGED

            )

            // Add required entity
            val environment: Environment
            if (findAll(em, Environment::class).isEmpty()) {
                environment = EnvironmentResourceIT.createEntity(em)
                em.persist(environment)
                em.flush()
            } else {
                environment = findAll(em, Environment::class)[0]
            }
            activity.environment = environment
            return activity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Activity {
            val activity = Activity(
                name = UPDATED_NAME,

                startedOn = UPDATED_STARTED_ON,

                finishedOn = UPDATED_FINISHED_ON,

                description = UPDATED_DESCRIPTION,

                link = UPDATED_LINK,

                flagged = UPDATED_FLAGGED

            )

            // Add required entity
            val environment: Environment
            if (findAll(em, Environment::class).isEmpty()) {
                environment = EnvironmentResourceIT.createUpdatedEntity(em)
                em.persist(environment)
                em.flush()
            } else {
                environment = findAll(em, Environment::class)[0]
            }
            activity.environment = environment
            return activity
        }
    }
}
