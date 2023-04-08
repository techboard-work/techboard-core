package work.techboard.core.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.Activity
import work.techboard.core.repository.ActivityRepository
import java.time.Instant
import java.util.*

/**
 * Service Implementation for managing [Activity].
 */
@Service
@Transactional
class ActivityService(
    private val activityRepository: ActivityRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun save(activity: Activity): Activity {
        log.debug("Request to save Activity : $activity")
        return activityRepository.save(activity)
    }

    fun update(activity: Activity): Activity {
        log.debug("Request to update Activity : {}", activity)
        return activityRepository.save(activity)
    }

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
                if (activity.link != null) {
                    it.link = activity.link
                }
                if (activity.severity != null) {
                    it.severity = activity.severity
                }

                it
            }
            .map { activityRepository.save(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(): MutableList<Activity> {
        log.debug("Request to get all Activities")
        return activityRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<Activity> {
        log.debug("Request to get Activity : $id")
        return activityRepository.findById(id)
    }

    fun delete(id: Long) {
        log.debug("Request to delete Activity : $id")

        activityRepository.deleteById(id)
    }

    fun getCurrentActivities(timestamp: Instant, envs: List<Long>): List<Activity> {
        log.debug("Request to get Activities for: $envs")
        val sorted = activityRepository.findCurrentIn(timestamp, envs)
        // TODO configure the severity limit
        val limit = 3
        val (flagged, usual) = sorted.partition { activity: Activity -> activity.severity!! <= limit }
        log.debug("${sorted.size} Activities found.")
        return flagged + usual
    }
}
