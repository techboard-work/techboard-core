package work.techboard.core.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.Environment
import work.techboard.core.repository.EnvironmentRepository
import work.techboard.core.service.EnvironmentService
import java.util.Optional

/**
 * Service Implementation for managing [Environment].
 */
@Service
@Transactional
class EnvironmentServiceImpl(
    private val environmentRepository: EnvironmentRepository,
) : EnvironmentService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(environment: Environment): Environment {
        log.debug("Request to save Environment : $environment")
        return environmentRepository.save(environment)
    }

    override fun update(environment: Environment): Environment {
        log.debug("Request to update Environment : {}", environment)
        return environmentRepository.save(environment)
    }

    override fun partialUpdate(environment: Environment): Optional<Environment> {
        log.debug("Request to partially update Environment : {}", environment)

        return environmentRepository.findById(environment.id!!)
            .map {

                if (environment.name != null) {
                    it.name = environment.name
                }
                if (environment.label != null) {
                    it.label = environment.label
                }
                if (environment.description != null) {
                    it.description = environment.description
                }
                if (environment.color != null) {
                    it.color = environment.color
                }
                if (environment.level != null) {
                    it.level = environment.level
                }
                if (environment.link != null) {
                    it.link = environment.link
                }

                it
            }
            .map { environmentRepository.save(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<Environment> {
        log.debug("Request to get all Environments")
        return environmentRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<Environment> {
        log.debug("Request to get Environment : $id")
        return environmentRepository.findById(id)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete Environment : $id")

        environmentRepository.deleteById(id)
    }
}
