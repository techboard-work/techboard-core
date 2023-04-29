package work.techboard.core.web.rest

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import work.techboard.core.domain.Environment
import work.techboard.core.service.BoardService

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
}
