package work.techboard.core.web.rest

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import work.techboard.core.service.UiConfigService
import work.techboard.core.service.dto.UiConfig

/**
 * UiConfigResource controller
 */
@RestController
@RequestMapping("/api/ui-config")
class UiConfigResource(val service: UiConfigService) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * GET UiConfig
     */
    @GetMapping("/getUiConfig")
    fun get(): UiConfig {
        log.debug("REST request for config")
        return service.latestUiConfig()
    }
}
