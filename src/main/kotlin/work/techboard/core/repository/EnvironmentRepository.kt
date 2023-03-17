package work.techboard.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import work.techboard.core.domain.Environment

/**
 * Spring Data JPA repository for the Environment entity.
 */
@Suppress("unused")
@Repository
interface EnvironmentRepository : JpaRepository<Environment, Long>
