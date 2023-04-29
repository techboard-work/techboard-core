package work.techboard.core.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.Environment
import work.techboard.core.repository.EnvironmentRepository
import java.util.Optional

/**
 * Service Implementation for managing [Environment].
 */
@Service
@Transactional
class EnvironmentService(
    private val environmentRepository: EnvironmentRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a environment.
     *
     * @param environment the entity to save.
     * @return the persisted entity.
     */
    fun save(environment: Environment): Environment {
        log.debug("Request to save Environment : $environment")
        return environmentRepository.save(environment)
    }

    /**
     * Update a environment.
     *
     * @param environment the entity to save.
     * @return the persisted entity.
     */
    fun update(environment: Environment): Environment {
        log.debug("Request to update Environment : {}", environment)
        return environmentRepository.save(environment)
    }

    /**
     * Partially updates a environment.
     *
     * @param environment the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(environment: Environment): Optional<Environment> {
        log.debug("Request to partially update Environment : {}", environment)

        return environmentRepository.findById(environment.id)
            .map {

                if (environment.name != null) {
                    it.name = environment.name
                }
                if (environment.code != null) {
                    it.code = environment.code
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

    /**
     * Get all the environments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(): MutableList<Environment> {
        log.debug("Request to get all Environments")
        return environmentRepository.findAll()
    }

    /**
     * Get one environment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<Environment> {
        log.debug("Request to get Environment : $id")
        return environmentRepository.findById(id)
    }

    /**
     * Delete the environment by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete Environment : $id")

        environmentRepository.deleteById(id)
    }
}
