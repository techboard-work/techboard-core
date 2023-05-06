package work.techboard.core.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import work.techboard.core.domain.Activity
import java.time.Instant
import java.util.Optional

/**
 * Spring Data JPA repository for the Activity entity.
 *
 * When extending this class, extend ActivityRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
interface ActivityRepository : ActivityRepositoryWithBagRelationships, JpaRepository<Activity, Long> {

    @Query("select activity from Activity activity where activity.owner.login = ?#{principal.preferredUsername}")
    fun findByOwnerIsCurrentUser(): MutableList<Activity>

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<Activity> {
        return this.fetchBagRelationships(this.findById(id))
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<Activity> {
        return this.fetchBagRelationships(this.findAll())
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<Activity> {
        return this.fetchBagRelationships(this.findAll(pageable))
    }

    fun findByFinishedOnIsNull(): List<Activity>
    @Query(
        """select activity from Activity activity
        where activity.startedOn > :timestamp
        and activity.environment.id is :env
        order by startedOn"""
    )
    fun findBoardActivities(@Param("timestamp") timestamp: Instant, @Param("env") env: Long): List<Activity>

    @JvmDefault fun findByFinishedOnIsNullWithEagerRelationships(): MutableList<Activity> {
        return this.fetchBagRelationships(this.findByFinishedOnIsNull())
    }

    @JvmDefault fun findBoardActivitiesWithEagerRelationships(
        @Param("timestamp") timestamp: Instant,
        @Param("env") env: Long
    ): MutableList<Activity> {
        return this.fetchBagRelationships(this.findBoardActivities(timestamp, env))
    }
}
