package work.techboard.core.repository

import work.techboard.core.domain.Activity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Suppress("unused")
@Repository
interface ActivityRepository : JpaRepository<Activity, Long> {
}
