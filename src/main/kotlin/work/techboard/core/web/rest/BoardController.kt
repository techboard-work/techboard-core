package work.techboard.core.web.rest

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.ResponseUtil
import work.techboard.core.domain.Environment
import work.techboard.core.service.BoardService
import java.time.Instant

/**
 * REST controller to view the whole board
 */
@RestController
@RequestMapping("/api/board")
class BoardController(
    private val boardService: BoardService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * `GET  /environments` : get all the environments.
     *
     * @return the [ResponseEntity] with status `200 (OK)` and the list of environments in body.
     */
    @GetMapping("/environments")
    fun getAllEnvironments(): List<Environment> {
        log.debug("REST request to get all Environments")
        return boardService.findAll()
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
        val environment = boardService.findOne(Instant.now().minusSeconds(60 * 60 * 24 * 14), id)
        return ResponseUtil.wrapOrNotFound(environment)
    }
}
