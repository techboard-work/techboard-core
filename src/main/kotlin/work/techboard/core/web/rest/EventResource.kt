package work.techboard.core.web.rest

import work.techboard.core.domain.Event
import work.techboard.core.repository.EventRepository
import work.techboard.core.service.EventService
import work.techboard.core.web.rest.errors.BadRequestAlertException

import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.validation.Valid
import javax.validation.constraints.NotNull
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "event"
/**
 * REST controller for managing [work.techboard.core.domain.Event].
 */
@RestController
@RequestMapping("/api")
class EventResource(
        private val eventService: EventService,
        private val eventRepository: EventRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "event"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /events` : Create a new event.
     *
     * @param event the event to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new event, or with status `400 (Bad Request)` if the event has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events")
    fun createEvent(@Valid @RequestBody event: Event): ResponseEntity<Event> {
        log.debug("REST request to save Event : $event")
        if (event.id != null) {
            throw BadRequestAlertException(
                "A new event cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = eventService.save(event)
            return ResponseEntity.created(URI("/api/events/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /events/:id} : Updates an existing event.
     *
     * @param id the id of the event to save.
     * @param event the event to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated event,
     * or with status `400 (Bad Request)` if the event is not valid,
     * or with status `500 (Internal Server Error)` if the event couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/events/{id}")
    fun updateEvent(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody event: Event
    ): ResponseEntity<Event> {
        log.debug("REST request to update Event : {}, {}", id, event)
        if (event.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, event.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }


        if (!eventRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = eventService.update(event)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     event.id.toString()
                )
            )
            .body(result)
    }

    /**
    * {@code PATCH  /events/:id} : Partial updates given fields of an existing event, field will ignore if it is null
    *
    * @param id the id of the event to save.
    * @param event the event to update.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated event,
    * or with status {@code 400 (Bad Request)} if the event is not valid,
    * or with status {@code 404 (Not Found)} if the event is not found,
    * or with status {@code 500 (Internal Server Error)} if the event couldn't be updated.
    * @throws URISyntaxException if the Location URI syntax is incorrect.
    */
    @PatchMapping(value = ["/events/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateEvent(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody event:Event
    ): ResponseEntity<Event> {
        log.debug("REST request to partial update Event partially : {}, {}", id, event)
        if (event.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, event.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!eventRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }



            val result = eventService.partialUpdate(event)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, event.id.toString())
        )
    }

    /**
     * `GET  /events` : get all the events.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of events in body.
     */
    @GetMapping("/events")    
    fun getAllEvents(): MutableList<Event> {
        
        

            log.debug("REST request to get all Events")
            
            return eventService.findAll()
                }

    /**
     * `GET  /events/:id` : get the "id" event.
     *
     * @param id the id of the event to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the event, or with status `404 (Not Found)`.
     */
    @GetMapping("/events/{id}")
    fun getEvent(@PathVariable id: Long): ResponseEntity<Event> {
        log.debug("REST request to get Event : $id")
        val event = eventService.findOne(id)
        return ResponseUtil.wrapOrNotFound(event)
    }
    /**
     *  `DELETE  /events/:id` : delete the "id" event.
     *
     * @param id the id of the event to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/events/{id}")
    fun deleteEvent(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Event : $id")

        eventService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
