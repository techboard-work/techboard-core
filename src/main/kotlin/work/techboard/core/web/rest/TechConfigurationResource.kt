package work.techboard.core.web.rest

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import work.techboard.core.repository.TechConfigurationRepository
import work.techboard.core.service.TechConfigurationService
import work.techboard.core.service.dto.TechConfigurationDTO
import work.techboard.core.web.rest.errors.BadRequestAlertException
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects
import javax.validation.Valid
import javax.validation.constraints.NotNull

private const val ENTITY_NAME = "techConfiguration"
/**
 * REST controller for managing [work.techboard.core.domain.TechConfiguration].
 */
@RestController
@RequestMapping("/api")
class TechConfigurationResource(
    private val techConfigurationService: TechConfigurationService,
    private val techConfigurationRepository: TechConfigurationRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "techConfiguration"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tech-configurations` : Create a new techConfiguration.
     *
     * @param techConfigurationDTO the techConfigurationDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new techConfigurationDTO, or with status `400 (Bad Request)` if the techConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tech-configurations")
    fun createTechConfiguration(@Valid @RequestBody techConfigurationDTO: TechConfigurationDTO): ResponseEntity<TechConfigurationDTO> {
        log.debug("REST request to save TechConfiguration : $techConfigurationDTO")
        if (techConfigurationDTO.id != null) {
            throw BadRequestAlertException(
                "A new techConfiguration cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = techConfigurationService.save(techConfigurationDTO)
        return ResponseEntity.created(URI("/api/tech-configurations/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /tech-configurations/:id} : Updates an existing techConfiguration.
     *
     * @param id the id of the techConfigurationDTO to save.
     * @param techConfigurationDTO the techConfigurationDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated techConfigurationDTO,
     * or with status `400 (Bad Request)` if the techConfigurationDTO is not valid,
     * or with status `500 (Internal Server Error)` if the techConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tech-configurations/{id}")
    fun updateTechConfiguration(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody techConfigurationDTO: TechConfigurationDTO
    ): ResponseEntity<TechConfigurationDTO> {
        log.debug("REST request to update TechConfiguration : {}, {}", id, techConfigurationDTO)
        if (techConfigurationDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, techConfigurationDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!techConfigurationRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = techConfigurationService.update(techConfigurationDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    techConfigurationDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /tech-configurations/:id} : Partial updates given fields of an existing techConfiguration, field will ignore if it is null
     *
     * @param id the id of the techConfigurationDTO to save.
     * @param techConfigurationDTO the techConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the techConfigurationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the techConfigurationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the techConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/tech-configurations/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateTechConfiguration(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody techConfigurationDTO: TechConfigurationDTO
    ): ResponseEntity<TechConfigurationDTO> {
        log.debug("REST request to partial update TechConfiguration partially : {}, {}", id, techConfigurationDTO)
        if (techConfigurationDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, techConfigurationDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!techConfigurationRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = techConfigurationService.partialUpdate(techConfigurationDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techConfigurationDTO.id.toString())
        )
    }

    /**
     * `GET  /tech-configurations` : get all the techConfigurations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of techConfigurations in body.
     */
    @GetMapping("/tech-configurations") fun getAllTechConfigurations(@RequestParam(required = false, defaultValue = "false") eagerload: Boolean): MutableList<TechConfigurationDTO> {

        log.debug("REST request to get all TechConfigurations")

        return techConfigurationService.findAll()
    }

    /**
     * `GET  /tech-configurations/:id` : get the "id" techConfiguration.
     *
     * @param id the id of the techConfigurationDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the techConfigurationDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tech-configurations/{id}")
    fun getTechConfiguration(@PathVariable id: Long): ResponseEntity<TechConfigurationDTO> {
        log.debug("REST request to get TechConfiguration : $id")
        val techConfigurationDTO = techConfigurationService.findOne(id)
        return ResponseUtil.wrapOrNotFound(techConfigurationDTO)
    }
    /**
     *  `DELETE  /tech-configurations/:id` : delete the "id" techConfiguration.
     *
     * @param id the id of the techConfigurationDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tech-configurations/{id}")
    fun deleteTechConfiguration(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete TechConfiguration : $id")

        techConfigurationService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
