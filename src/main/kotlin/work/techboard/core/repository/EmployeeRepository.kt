package work.techboard.core.repository

import work.techboard.core.domain.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Employee entity.
 */
@Suppress("unused")
@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {
}
