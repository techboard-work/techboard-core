package work.techboard.core.service
import work.techboard.core.domain.Environment
import java.util.Optional

/**
 * Service Interface for managing [Environment].
 */
interface EnvironmentService {

    /**
     * Save a environment.
     *
     * @param environment the entity to save.
     * @return the persisted entity.
     */
    fun save(environment: Environment): Environment

    /**
     * Updates a environment.
     *
     * @param environment the entity to update.
     * @return the persisted entity.
     */
    fun update(environment: Environment): Environment

    /**
     * Partially updates a environment.
     *
     * @param environment the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(environment: Environment): Optional<Environment>

    /**
     * Get all the environments.
     *
     * @return the list of entities.
     */
    fun findAll(): List<Environment>

    /**
     * Get the "id" environment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<Environment>

    /**
     * Delete the "id" environment.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
