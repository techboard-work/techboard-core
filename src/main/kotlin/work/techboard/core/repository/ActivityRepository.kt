package work.techboard.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import work.techboard.core.domain.Activity
import java.time.Instant

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Suppress("unused")
@Repository
interface ActivityRepository : JpaRepository<Activity, Long> {

    @Query("select activity from Activity activity where activity.owner.login = ?#{principal.preferredUsername}")
    fun findByOwnerIsCurrentUser(): MutableList<Activity>

    @Query(
        """select activity from Activity activity
        where activity.startedOn < :timestamp
        and (activity.finishedOn is null or activity.finishedOn > :timestamp)
        and activity.environment.id = :envId
        order by startedOn"""
    )
    fun findCurrentIn(@Param("envId") envId: Long, @Param("timestamp") timestamp: Instant): List<Activity>
}
