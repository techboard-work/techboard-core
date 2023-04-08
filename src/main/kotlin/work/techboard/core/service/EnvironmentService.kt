package work.techboard.core.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.Environment
import work.techboard.core.repository.EnvironmentRepository
import java.time.Instant
import java.util.*

/**
 * Service Implementation for managing [Environment].
 */
@Service
@Transactional
class EnvironmentService(
    private val environmentRepository: EnvironmentRepository,
    private val activityService: ActivityService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(environment: Environment): Environment {
        log.debug("Request to save Environment : $environment")
        return environmentRepository.save(environment)
    }

    fun update(environment: Environment): Environment {
        log.debug("Request to update Environment : {}", environment)
        return environmentRepository.save(environment)
    }

    fun partialUpdate(environment: Environment): Optional<Environment> {
        log.debug("Request to partially update Environment : {}", environment)

        return environmentRepository.findById(environment.id)
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
    fun findAll(): List<Environment> {
        log.debug("Request to get all Environments")
        val envs = environmentRepository.findAll()
        val currentActivities =
            activityService.getCurrentActivities(Instant.now(), envs.map { e -> e.id!! })
        // set activities for every environment or empty set
        return envs.map { env ->
            env.apply {
                activities = currentActivities.filter { activity ->
                    env.id == activity.environment!!.id
                }.toMutableSet()
            }
        }
    }

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<Environment> {
        log.debug("Request to get Environment : $id")
        return environmentRepository.findById(id)
    }

    fun delete(id: Long) {
        log.debug("Request to delete Environment : $id")

        environmentRepository.deleteById(id)
    }
}
