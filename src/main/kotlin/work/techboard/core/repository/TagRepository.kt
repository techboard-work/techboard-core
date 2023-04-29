package work.techboard.core.repository

import work.techboard.core.domain.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Tag entity.
 */
@Suppress("unused")
@Repository
interface TagRepository : JpaRepository<Tag, Long> {
}
