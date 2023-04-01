package work.techboard.core.repository

import work.techboard.core.domain.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Event entity.
 */
@Suppress("unused")
@Repository
interface EventRepository : JpaRepository<Event, Long> {

    @Query("select event from Event event where event.reporter.login = ?#{principal.preferredUsername}")
    fun findByReporterIsCurrentUser(): MutableList<Event>
}
