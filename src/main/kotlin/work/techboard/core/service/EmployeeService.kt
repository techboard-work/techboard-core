package work.techboard.core.service
import work.techboard.core.domain.Employee

import java.util.Optional

/**
 * Service Interface for managing [Employee].
 */
interface EmployeeService {

    /**
     * Save a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    fun save(employee: Employee): Employee

    /**
     * Updates a employee.
     *
     * @param employee the entity to update.
     * @return the persisted entity.
     */
     fun update(employee: Employee): Employee

    /**
     * Partially updates a employee.
     *
     * @param employee the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(employee: Employee): Optional<Employee>

    /**
     * Get all the employees.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Employee>

    /**
     * Get the "id" employee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<Employee>

    /**
     * Delete the "id" employee.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
