package work.techboard.core.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.Activity
import work.techboard.core.repository.ActivityRepository
import java.util.Optional

/**
 * Service Implementation for managing [Activity].
 */
@Service
@Transactional
class ActivityService(
    private val activityRepository: ActivityRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a activity.
     *
     * @param activity the entity to save.
     * @return the persisted entity.
     */
    fun save(activity: Activity): Activity {
        log.debug("Request to save Activity : $activity")
        return activityRepository.save(activity)
    }

    /**
     * Update a activity.
     *
     * @param activity the entity to save.
     * @return the persisted entity.
     */
    fun update(activity: Activity): Activity {
        log.debug("Request to update Activity : {}", activity)
        return activityRepository.save(activity)
    }

    /**
     * Partially updates a activity.
     *
     * @param activity the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(activity: Activity): Optional<Activity> {
        log.debug("Request to partially update Activity : {}", activity)

        return activityRepository.findById(activity.id)
            .map {

                if (activity.name != null) {
                    it.name = activity.name
                }
                if (activity.startedOn != null) {
                    it.startedOn = activity.startedOn
                }
                if (activity.finishedOn != null) {
                    it.finishedOn = activity.finishedOn
                }
                if (activity.description != null) {
                    it.description = activity.description
                }
                if (activity.link != null) {
                    it.link = activity.link
                }
                if (activity.flagged != null) {
                    it.flagged = activity.flagged
                }

                it
            }
            .map { activityRepository.save(it) }
    }

    /**
     * Get all the activities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(): MutableList<Activity> {
        log.debug("Request to get all Activities")
        return activityRepository.findAll()
    }

    /**
     * Get all the activities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        activityRepository.findAllWithEagerRelationships(pageable)

    /**
     * Get one activity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<Activity> {
        log.debug("Request to get Activity : $id")
        return activityRepository.findOneWithEagerRelationships(id)
    }

    /**
     * Delete the activity by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete Activity : $id")

        activityRepository.deleteById(id)
    }
}
