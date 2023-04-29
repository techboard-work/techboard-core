package work.techboard.core.web.rest

import work.techboard.core.domain.Tag
import work.techboard.core.repository.TagRepository
import work.techboard.core.service.TagService
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

private const val ENTITY_NAME = "tag"
/**
 * REST controller for managing [work.techboard.core.domain.Tag].
 */
@RestController
@RequestMapping("/api")
class TagResource(
        private val tagService: TagService,
        private val tagRepository: TagRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "tag"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tags` : Create a new tag.
     *
     * @param tag the tag to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tag, or with status `400 (Bad Request)` if the tag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tags")
    fun createTag(@Valid @RequestBody tag: Tag): ResponseEntity<Tag> {
        log.debug("REST request to save Tag : $tag")
        if (tag.id != null) {
            throw BadRequestAlertException(
                "A new tag cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = tagService.save(tag)
            return ResponseEntity.created(URI("/api/tags/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /tags/:id} : Updates an existing tag.
     *
     * @param id the id of the tag to save.
     * @param tag the tag to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tag,
     * or with status `400 (Bad Request)` if the tag is not valid,
     * or with status `500 (Internal Server Error)` if the tag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tags/{id}")
    fun updateTag(
        @PathVariable(value = "id", required = false) id: Long,
        @Valid @RequestBody tag: Tag
    ): ResponseEntity<Tag> {
        log.debug("REST request to update Tag : {}, {}", id, tag)
        if (tag.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, tag.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }


        if (!tagRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tagService.update(tag)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     tag.id.toString()
                )
            )
            .body(result)
    }

    /**
    * {@code PATCH  /tags/:id} : Partial updates given fields of an existing tag, field will ignore if it is null
    *
    * @param id the id of the tag to save.
    * @param tag the tag to update.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tag,
    * or with status {@code 400 (Bad Request)} if the tag is not valid,
    * or with status {@code 404 (Not Found)} if the tag is not found,
    * or with status {@code 500 (Internal Server Error)} if the tag couldn't be updated.
    * @throws URISyntaxException if the Location URI syntax is incorrect.
    */
    @PatchMapping(value = ["/tags/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateTag(
        @PathVariable(value = "id", required = false) id: Long,
        @NotNull @RequestBody tag:Tag
    ): ResponseEntity<Tag> {
        log.debug("REST request to partial update Tag partially : {}, {}", id, tag)
        if (tag.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, tag.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tagRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }



            val result = tagService.partialUpdate(tag)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tag.id.toString())
        )
    }

    /**
     * `GET  /tags` : get all the tags.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of tags in body.
     */
    @GetMapping("/tags")    
    fun getAllTags(): MutableList<Tag> {
        
        

            log.debug("REST request to get all Tags")
            
            return tagService.findAll()
                }

    /**
     * `GET  /tags/:id` : get the "id" tag.
     *
     * @param id the id of the tag to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tag, or with status `404 (Not Found)`.
     */
    @GetMapping("/tags/{id}")
    fun getTag(@PathVariable id: Long): ResponseEntity<Tag> {
        log.debug("REST request to get Tag : $id")
        val tag = tagService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tag)
    }
    /**
     *  `DELETE  /tags/:id` : delete the "id" tag.
     *
     * @param id the id of the tag to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tags/{id}")
    fun deleteTag(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Tag : $id")

        tagService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
