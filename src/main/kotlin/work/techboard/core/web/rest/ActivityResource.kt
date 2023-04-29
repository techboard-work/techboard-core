package work.techboard.core.web.rest

import work.techboard.core.domain.Activity
import work.techboard.core.repository.ActivityRepository
import work.techboard.core.service.ActivityService
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

private const val ENTITY_NAME = "activity"
/**
 * REST controller for managing [work.techboard.core.domain.Activity].
 */
@RestController
@RequestMapping("/api")
class ActivityResource(
        private val activityService: ActivityService,
        private val activityRepository: ActivityRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "activity"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /activities` : Create a new activity.
     *
     * @param activity the activity to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new activity, or with status `400 (Bad Request)` if the activity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activities")
    fun createActivity(@Valid @RequestBody activity: Activity): ResponseEntity<Activity> {
        log.debug("REST request to save Activity : $activity")
        if (activity.id != null) {
            throw BadRequestAlertException(
                "A new activity cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = activityService.save(activity)
            return ResponseEntity.created(URI("/api/activities/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /activities/:id} : Updates an existing activity.
     *
     * @param id the id of the activity to save.
     * @param activity the activity to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated activity,
     * or with status `400 (Bad Request)` if the activity is not valid,
     * or with status `500 (Internal Server Error)` if the activity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activities/{id}")
    fun updateActivity(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody activity: Activity
    ): ResponseEntity<Activity> {
        log.debug("REST request to update Activity : {}, {}", id, activity)
        if (activity.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, activity.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }


        if (!activityRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = activityService.update(activity)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     activity.id.toString()
                )
            )
            .body(result)
    }

    /**
    * {@code PATCH  /activities/:id} : Partial updates given fields of an existing activity, field will ignore if it is null
    *
    * @param id the id of the activity to save.
    * @param activity the activity to update.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activity,
    * or with status {@code 400 (Bad Request)} if the activity is not valid,
    * or with status {@code 404 (Not Found)} if the activity is not found,
    * or with status {@code 500 (Internal Server Error)} if the activity couldn't be updated.
    * @throws URISyntaxException if the Location URI syntax is incorrect.
    */
    @PatchMapping(value = ["/activities/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateActivity(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody activity:Activity
    ): ResponseEntity<Activity> {
        log.debug("REST request to partial update Activity partially : {}, {}", id, activity)
        if (activity.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, activity.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!activityRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }



            val result = activityService.partialUpdate(activity)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activity.id.toString())
        )
    }

    /**
     * `GET  /activities` : get all the activities.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of activities in body.
     */
    @GetMapping("/activities")    
    fun getAllActivities(@RequestParam(required = false, defaultValue = "false") eagerload: Boolean): MutableList<Activity> {
        
        

            log.debug("REST request to get all Activities")
            
            return activityService.findAll()
                }

    /**
     * `GET  /activities/:id` : get the "id" activity.
     *
     * @param id the id of the activity to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the activity, or with status `404 (Not Found)`.
     */
    @GetMapping("/activities/{id}")
    fun getActivity(@PathVariable id: Long): ResponseEntity<Activity> {
        log.debug("REST request to get Activity : $id")
        val activity = activityService.findOne(id)
        return ResponseUtil.wrapOrNotFound(activity)
    }
    /**
     *  `DELETE  /activities/:id` : delete the "id" activity.
     *
     * @param id the id of the activity to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/activities/{id}")
    fun deleteActivity(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Activity : $id")

        activityService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
