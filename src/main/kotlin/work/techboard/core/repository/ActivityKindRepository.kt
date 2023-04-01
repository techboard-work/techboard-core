package work.techboard.core.repository

import work.techboard.core.domain.ActivityKind
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the ActivityKind entity.
 */
@Suppress("unused")
@Repository
interface ActivityKindRepository : JpaRepository<ActivityKind, Long> {
}
