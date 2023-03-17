package work.techboard.core.web.rest

import work.techboard.core.domain.Employee
import work.techboard.core.repository.EmployeeRepository
import work.techboard.core.service.EmployeeService
import work.techboard.core.web.rest.errors.BadRequestAlertException

import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "employee"
/**
 * REST controller for managing [work.techboard.core.domain.Employee].
 */
@RestController
@RequestMapping("/api")
class EmployeeResource(
        private val employeeService: EmployeeService,
        private val employeeRepository: EmployeeRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "employee"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /employees` : Create a new employee.
     *
     * @param employee the employee to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new employee, or with status `400 (Bad Request)` if the employee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employees")
    fun createEmployee(@RequestBody employee: Employee): ResponseEntity<Employee> {
        log.debug("REST request to save Employee : $employee")
        if (employee.id != null) {
            throw BadRequestAlertException(
                "A new employee cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = employeeService.save(employee)
            return ResponseEntity.created(URI("/api/employees/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /employees/:id} : Updates an existing employee.
     *
     * @param id the id of the employee to save.
     * @param employee the employee to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated employee,
     * or with status `400 (Bad Request)` if the employee is not valid,
     * or with status `500 (Internal Server Error)` if the employee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employees/{id}")
    fun updateEmployee(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody employee: Employee
    ): ResponseEntity<Employee> {
        log.debug("REST request to update Employee : {}, {}", id, employee)
        if (employee.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, employee.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }


        if (!employeeRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = employeeService.update(employee)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     employee.id.toString()
                )
            )
            .body(result)
    }

    /**
    * {@code PATCH  /employees/:id} : Partial updates given fields of an existing employee, field will ignore if it is null
    *
    * @param id the id of the employee to save.
    * @param employee the employee to update.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employee,
    * or with status {@code 400 (Bad Request)} if the employee is not valid,
    * or with status {@code 404 (Not Found)} if the employee is not found,
    * or with status {@code 500 (Internal Server Error)} if the employee couldn't be updated.
    * @throws URISyntaxException if the Location URI syntax is incorrect.
    */
    @PatchMapping(value = ["/employees/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateEmployee(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody employee:Employee
    ): ResponseEntity<Employee> {
        log.debug("REST request to partial update Employee partially : {}, {}", id, employee)
        if (employee.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, employee.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!employeeRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }



            val result = employeeService.partialUpdate(employee)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employee.id.toString())
        )
    }

    /**
     * `GET  /employees` : get all the employees.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of employees in body.
     */
    @GetMapping("/employees")    
    fun getAllEmployees(): MutableList<Employee> {
        
        

            log.debug("REST request to get all Employees")
            
            return employeeService.findAll()
                }

    /**
     * `GET  /employees/:id` : get the "id" employee.
     *
     * @param id the id of the employee to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the employee, or with status `404 (Not Found)`.
     */
    @GetMapping("/employees/{id}")
    fun getEmployee(@PathVariable id: Long): ResponseEntity<Employee> {
        log.debug("REST request to get Employee : $id")
        val employee = employeeService.findOne(id)
        return ResponseUtil.wrapOrNotFound(employee)
    }
    /**
     *  `DELETE  /employees/:id` : delete the "id" employee.
     *
     * @param id the id of the employee to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Employee : $id")

        employeeService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
