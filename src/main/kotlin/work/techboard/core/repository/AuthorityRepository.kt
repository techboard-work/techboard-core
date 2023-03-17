package work.techboard.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import work.techboard.core.domain.Authority

/**
 * Spring Data JPA repository for the [Authority] entity.
 */

interface AuthorityRepository : JpaRepository<Authority, String>
