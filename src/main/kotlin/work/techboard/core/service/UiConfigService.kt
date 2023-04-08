package work.techboard.core.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.TechConfiguration
import work.techboard.core.repository.TechConfigurationRepository
import work.techboard.core.service.dto.UiConfig

@Service
@Transactional
class UiConfigService(private val techConfigurationRepository: TechConfigurationRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun latestUiConfig(): UiConfig {
        log.debug("Find latest config")
        val tech = techConfigurationRepository.findAll().getOrElse(0) {
            TechConfiguration(content = "")
        }
        return UiConfig(listOf(), 3)
    }
}
