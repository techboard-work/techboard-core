package work.techboard.core.web.rest

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import work.techboard.core.domain.ActivityKind
import work.techboard.core.repository.ActivityKindRepository
import work.techboard.core.web.rest.errors.BadRequestAlertException
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects
import javax.validation.Valid
import javax.validation.constraints.NotNull

private const val ENTITY_NAME = "activityKind"
/**
 * REST controller for managing [work.techboard.core.domain.ActivityKind].
 */
@RestController
@RequestMapping("/api")
@Transactional
class ActivityKindResource(
    private val activityKindRepository: ActivityKindRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "activityKind"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /activity-kinds` : Create a new activityKind.
     *
     * @param activityKind the activityKind to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new activityKind, or with status `400 (Bad Request)` if the activityKind has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activity-kinds")
    fun createActivityKind(@Valid @RequestBody activityKind: ActivityKind): ResponseEntity<ActivityKind> {
        log.debug("REST request to save ActivityKind : $activityKind")
        if (activityKind.id != null) {
            throw BadRequestAlertException(
                "A new activityKind cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = activityKindRepository.save(activityKind)
        return ResponseEntity.created(URI("/api/activity-kinds/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /activity-kinds/:id} : Updates an existing activityKind.
     *
     * @param id the id of the activityKind to save.
     * @param activityKind the activityKind to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated activityKind,
     * or with status `400 (Bad Request)` if the activityKind is not valid,
     * or with status `500 (Internal Server Error)` if the activityKind couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activity-kinds/{id}")
    fun updateActivityKind(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody activityKind: ActivityKind
    ): ResponseEntity<ActivityKind> {
        log.debug("REST request to update ActivityKind : {}, {}", id, activityKind)
        if (activityKind.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, activityKind.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!activityKindRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = activityKindRepository.save(activityKind)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    activityKind.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /activity-kinds/:id} : Partial updates given fields of an existing activityKind, field will ignore if it is null
     *
     * @param id the id of the activityKind to save.
     * @param activityKind the activityKind to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityKind,
     * or with status {@code 400 (Bad Request)} if the activityKind is not valid,
     * or with status {@code 404 (Not Found)} if the activityKind is not found,
     * or with status {@code 500 (Internal Server Error)} if the activityKind couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/activity-kinds/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateActivityKind(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody activityKind: ActivityKind
    ): ResponseEntity<ActivityKind> {
        log.debug("REST request to partial update ActivityKind partially : {}, {}", id, activityKind)
        if (activityKind.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, activityKind.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!activityKindRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = activityKindRepository.findById(activityKind.id)
            .map {

                if (activityKind.name != null) {
                    it.name = activityKind.name
                }
                if (activityKind.description != null) {
                    it.description = activityKind.description
                }
                if (activityKind.color != null) {
                    it.color = activityKind.color
                }
                if (activityKind.icon != null) {
                    it.icon = activityKind.icon
                }

                it
            }
            .map { activityKindRepository.save(it) }

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activityKind.id.toString())
        )
    }

    /**
     * `GET  /activity-kinds` : get all the activityKinds.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of activityKinds in body.
     */
    @GetMapping("/activity-kinds") fun getAllActivityKinds(): MutableList<ActivityKind> {

        log.debug("REST request to get all ActivityKinds")
        return activityKindRepository.findAll()
    }

    /**
     * `GET  /activity-kinds/:id` : get the "id" activityKind.
     *
     * @param id the id of the activityKind to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the activityKind, or with status `404 (Not Found)`.
     */
    @GetMapping("/activity-kinds/{id}")
    fun getActivityKind(@PathVariable id: Long): ResponseEntity<ActivityKind> {
        log.debug("REST request to get ActivityKind : $id")
        val activityKind = activityKindRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(activityKind)
    }
    /**
     *  `DELETE  /activity-kinds/:id` : delete the "id" activityKind.
     *
     * @param id the id of the activityKind to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/activity-kinds/{id}")
    fun deleteActivityKind(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete ActivityKind : $id")

        activityKindRepository.deleteById(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
