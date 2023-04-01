package work.techboard.core.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
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
import work.techboard.core.domain.Event
import work.techboard.core.repository.EventRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

/**
 * Integration tests for the [EventResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {
    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restEventMockMvc: MockMvc

    private lateinit var event: Event

    @BeforeEach
    fun initTest() {
        event = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createEvent() {
        val databaseSizeBeforeCreate = eventRepository.findAll().size
        // Create the Event
        restEventMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(event))
        ).andExpect(status().isCreated)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1)
        val testEvent = eventList[eventList.size - 1]

        assertThat(testEvent.message).isEqualTo(DEFAULT_MESSAGE)
        assertThat(testEvent.receivedOn).isEqualTo(DEFAULT_RECEIVED_ON)
        assertThat(testEvent.link).isEqualTo(DEFAULT_LINK)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createEventWithExistingId() {
        // Create the Event with an existing ID
        event.id = 1L

        val databaseSizeBeforeCreate = eventRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(event))
        ).andExpect(status().isBadRequest)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkMessageIsRequired() {
        val databaseSizeBeforeTest = eventRepository.findAll().size
        // set the field null
        event.message = null

        // Create the Event, which fails.

        restEventMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(event))
        ).andExpect(status().isBadRequest)

        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkReceivedOnIsRequired() {
        val databaseSizeBeforeTest = eventRepository.findAll().size
        // set the field null
        event.receivedOn = null

        // Create the Event, which fails.

        restEventMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(event))
        ).andExpect(status().isBadRequest)

        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllEvents() {
        // Initialize the database
        eventRepository.saveAndFlush(event)

        // Get all the eventList
        restEventMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.id?.toInt())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].receivedOn").value(hasItem(DEFAULT_RECEIVED_ON.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getEvent() {
        // Initialize the database
        eventRepository.saveAndFlush(event)

        val id = event.id
        assertNotNull(id)

        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, event.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.id?.toInt()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.receivedOn").value(DEFAULT_RECEIVED_ON.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingEvent() {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingEvent() {
        // Initialize the database
        eventRepository.saveAndFlush(event)

        val databaseSizeBeforeUpdate = eventRepository.findAll().size

        // Update the event
        val updatedEvent = eventRepository.findById(event.id).get()
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent)
        updatedEvent.message = UPDATED_MESSAGE
        updatedEvent.receivedOn = UPDATED_RECEIVED_ON
        updatedEvent.link = UPDATED_LINK

        restEventMockMvc.perform(
            put(ENTITY_API_URL_ID, updatedEvent.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedEvent))
        ).andExpect(status().isOk)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
        val testEvent = eventList[eventList.size - 1]
        assertThat(testEvent.message).isEqualTo(UPDATED_MESSAGE)
        assertThat(testEvent.receivedOn).isEqualTo(UPDATED_RECEIVED_ON)
        assertThat(testEvent.link).isEqualTo(UPDATED_LINK)
    }

    @Test
    @Transactional
    fun putNonExistingEvent() {
        val databaseSizeBeforeUpdate = eventRepository.findAll().size
        event.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(
            put(ENTITY_API_URL_ID, event.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(event))
        )
            .andExpect(status().isBadRequest)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchEvent() {
        val databaseSizeBeforeUpdate = eventRepository.findAll().size
        event.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(event))
        ).andExpect(status().isBadRequest)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamEvent() {
        val databaseSizeBeforeUpdate = eventRepository.findAll().size
        event.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(
            put(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(event))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateEventWithPatch() {
        eventRepository.saveAndFlush(event)

        val databaseSizeBeforeUpdate = eventRepository.findAll().size

// Update the event using partial update
        val partialUpdatedEvent = Event().apply {
            id = event.id

            message = UPDATED_MESSAGE
            receivedOn = UPDATED_RECEIVED_ON
            link = UPDATED_LINK
        }

        restEventMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedEvent.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedEvent))
        )
            .andExpect(status().isOk)

// Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
        val testEvent = eventList.last()
        assertThat(testEvent.message).isEqualTo(UPDATED_MESSAGE)
        assertThat(testEvent.receivedOn).isEqualTo(UPDATED_RECEIVED_ON)
        assertThat(testEvent.link).isEqualTo(UPDATED_LINK)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateEventWithPatch() {
        eventRepository.saveAndFlush(event)

        val databaseSizeBeforeUpdate = eventRepository.findAll().size

// Update the event using partial update
        val partialUpdatedEvent = Event().apply {
            id = event.id

            message = UPDATED_MESSAGE
            receivedOn = UPDATED_RECEIVED_ON
            link = UPDATED_LINK
        }

        restEventMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedEvent.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedEvent))
        )
            .andExpect(status().isOk)

// Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
        val testEvent = eventList.last()
        assertThat(testEvent.message).isEqualTo(UPDATED_MESSAGE)
        assertThat(testEvent.receivedOn).isEqualTo(UPDATED_RECEIVED_ON)
        assertThat(testEvent.link).isEqualTo(UPDATED_LINK)
    }

    @Throws(Exception::class)
    fun patchNonExistingEvent() {
        val databaseSizeBeforeUpdate = eventRepository.findAll().size
        event.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(
            patch(ENTITY_API_URL_ID, event.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(event))
        )
            .andExpect(status().isBadRequest)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchEvent() {
        val databaseSizeBeforeUpdate = eventRepository.findAll().size
        event.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(event))
        )
            .andExpect(status().isBadRequest)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamEvent() {
        val databaseSizeBeforeUpdate = eventRepository.findAll().size
        event.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(
            patch(ENTITY_API_URL).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(event))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Event in the database
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteEvent() {
        // Initialize the database
        eventRepository.saveAndFlush(event)
        val databaseSizeBeforeDelete = eventRepository.findAll().size
        // Delete the event
        restEventMockMvc.perform(
            delete(ENTITY_API_URL_ID, event.id).with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val eventList = eventRepository.findAll()
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_MESSAGE = "AAAAAAAAAA"
        private const val UPDATED_MESSAGE = "BBBBBBBBBB"

        private val DEFAULT_RECEIVED_ON: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_RECEIVED_ON: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_LINK = "AAAAAAAAAA"
        private const val UPDATED_LINK = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/events"
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
        fun createEntity(em: EntityManager): Event {
            val event = Event(
                message = DEFAULT_MESSAGE,

                receivedOn = DEFAULT_RECEIVED_ON,

                link = DEFAULT_LINK

            )

            return event
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Event {
            val event = Event(
                message = UPDATED_MESSAGE,

                receivedOn = UPDATED_RECEIVED_ON,

                link = UPDATED_LINK

            )

            return event
        }
    }
}
