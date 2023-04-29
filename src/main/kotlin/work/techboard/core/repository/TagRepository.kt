package work.techboard.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import work.techboard.core.domain.Tag

/**
 * Spring Data JPA repository for the Tag entity.
 */
@Suppress("unused")
@Repository
interface TagRepository : JpaRepository<Tag, Long>
