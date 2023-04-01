package work.techboard.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import work.techboard.core.domain.ActivityKind

/**
 * Spring Data JPA repository for the ActivityKind entity.
 */
@Suppress("unused")
@Repository
interface ActivityKindRepository : JpaRepository<ActivityKind, Long>
