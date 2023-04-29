package work.techboard.core.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.Activity
import work.techboard.core.domain.Environment
import work.techboard.core.repository.ActivityRepository
import work.techboard.core.repository.EnvironmentRepository

/**
 * Service Implementation for managing the whole board
 */
@Service
@Transactional
class BoardService(
    private val environmentRepository: EnvironmentRepository,
    private val activityRepository: ActivityRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Get all the environments.
     *
     * @return the list of entities with open activities
     */
    @Transactional(readOnly = true)
    fun findAll(): List<Environment> {
        log.debug("Request to get all Environments")
        val openActivities = activityRepository.findByFinishedOnIsNullWithEagerRelationships()
        val grouped: Map<Long, List<Activity>> = openActivities.groupBy { a -> a.environment!!.id!! }

        val envs = environmentRepository.findAll()

        // add open activities
        return envs.map { env -> env.apply { env.activities = grouped[env.id]?.toMutableSet() } }
    }
}
