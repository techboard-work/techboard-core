package work.techboard.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import work.techboard.core.domain.Activity

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Suppress("unused")
@Repository
interface ActivityRepository : JpaRepository<Activity, Long>
