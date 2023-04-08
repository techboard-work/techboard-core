package work.techboard.core.web.rest

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import work.techboard.core.domain.Environment
import work.techboard.core.repository.EnvironmentRepository
import work.techboard.core.service.EnvironmentService
import work.techboard.core.web.rest.errors.BadRequestAlertException
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects
import javax.validation.Valid
import javax.validation.constraints.NotNull

private const val ENTITY_NAME = "environment"
/**
 * REST controller for managing [work.techboard.core.domain.Environment].
 */
@RestController
@RequestMapping("/api")
class EnvironmentResource(
    private val environmentService: EnvironmentService,
    private val environmentRepository: EnvironmentRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "environment"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /environments` : Create a new environment.
     *
     * @param environment the environment to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new environment, or with status `400 (Bad Request)` if the environment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/environments")
    fun createEnvironment(@Valid @RequestBody environment: Environment): ResponseEntity<Environment> {
        log.debug("REST request to save Environment : $environment")
        if (environment.id != null) {
            throw BadRequestAlertException(
                "A new environment cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = environmentService.save(environment)
        return ResponseEntity.created(URI("/api/environments/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /environments/:id} : Updates an existing environment.
     *
     * @param id the id of the environment to save.
     * @param environment the environment to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated environment,
     * or with status `400 (Bad Request)` if the environment is not valid,
     * or with status `500 (Internal Server Error)` if the environment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/environments/{id}")
    fun updateEnvironment(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody environment: Environment
    ): ResponseEntity<Environment> {
        log.debug("REST request to update Environment : {}, {}", id, environment)
        if (environment.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, environment.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!environmentRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = environmentService.update(environment)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    environment.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /environments/:id} : Partial updates given fields of an existing environment, field will ignore if it is null
     *
     * @param id the id of the environment to save.
     * @param environment the environment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated environment,
     * or with status {@code 400 (Bad Request)} if the environment is not valid,
     * or with status {@code 404 (Not Found)} if the environment is not found,
     * or with status {@code 500 (Internal Server Error)} if the environment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/environments/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateEnvironment(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody environment: Environment
    ): ResponseEntity<Environment> {
        log.debug("REST request to partial update Environment partially : {}, {}", id, environment)
        if (environment.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, environment.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!environmentRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = environmentService.partialUpdate(environment)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, environment.id.toString())
        )
    }

    /**
     * `GET  /environments` : get all the environments.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of environments in body.
     */
    @GetMapping("/environments") fun getAllEnvironments(): List<Environment> {

        log.debug("REST request to get all Environments")

        return environmentService.findAll()
    }

    /**
     * `GET  /environments/:id` : get the "id" environment.
     *
     * @param id the id of the environment to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the environment, or with status `404 (Not Found)`.
     */
    @GetMapping("/environments/{id}")
    fun getEnvironment(@PathVariable id: Long): ResponseEntity<Environment> {
        log.debug("REST request to get Environment : $id")
        val environment = environmentService.findOne(id)
        return ResponseUtil.wrapOrNotFound(environment)
    }
    /**
     *  `DELETE  /environments/:id` : delete the "id" environment.
     *
     * @param id the id of the environment to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/environments/{id}")
    fun deleteEnvironment(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Environment : $id")

        environmentService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
