package work.techboard.core.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.Activity
import work.techboard.core.repository.ActivityRepository
import work.techboard.core.service.ActivityService
import java.time.Instant
import java.util.Optional

/**
 * Service Implementation for managing [Activity].
 */
@Service
@Transactional
class ActivityServiceImpl(
    private val activityRepository: ActivityRepository,
) : ActivityService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(activity: Activity): Activity {
        log.debug("Request to save Activity : $activity")
        return activityRepository.save(activity)
    }

    override fun update(activity: Activity): Activity {
        log.debug("Request to update Activity : {}", activity)
        return activityRepository.save(activity)
    }

    override fun partialUpdate(activity: Activity): Optional<Activity> {
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
    override fun findAll(): MutableList<Activity> {
        log.debug("Request to get all Activities")
        return activityRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<Activity> {
        log.debug("Request to get Activity : $id")
        return activityRepository.findById(id)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete Activity : $id")

        activityRepository.deleteById(id)
    }

    override fun getCurrentActivities(timestamp: Instant, envs: List<Long>): List<Activity> {
        log.debug("Request to get Activities for: $envs")
        val sorted = activityRepository.findCurrentIn(timestamp, envs)
        // TODO configure the severity limit
        val limit = 3
        val (flagged, usual) = sorted.partition { activity: Activity -> activity.severity!! <= limit }
        log.debug("${sorted.size} Activities found.")
        return flagged + usual
    }
}
