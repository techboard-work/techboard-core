package work.techboard.core.repository

import work.techboard.core.domain.Environment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Environment entity.
 */
@Suppress("unused")
@Repository
interface EnvironmentRepository : JpaRepository<Environment, Long> {
}
