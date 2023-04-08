package work.techboard.core.service
import work.techboard.core.domain.Activity
import java.time.Instant
import java.util.Optional

/**
 * Service Interface for managing [Activity].
 */
interface ActivityService {

    /**
     * Save a activity.
     *
     * @param activity the entity to save.
     * @return the persisted entity.
     */
    fun save(activity: Activity): Activity

    /**
     * Updates a activity.
     *
     * @param activity the entity to update.
     * @return the persisted entity.
     */
    fun update(activity: Activity): Activity

    /**
     * Partially updates a activity.
     *
     * @param activity the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(activity: Activity): Optional<Activity>

    /**
     * Get all the activities.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Activity>

    /**
     * Get the "id" activity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<Activity>

    /**
     * Delete the "id" activity.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Get al the activities which are open/active
     * (started and not finished for the given timestamp)
     * for the provided Environments
     * @param timestamp - the moment of time (should be current time normally)
     * @param envs - Environment IDs
     */
    fun getCurrentActivities(timestamp: Instant, envs: List<Long>): List<Activity>
}
