package work.techboard.core.service.impl


import work.techboard.core.service.EmployeeService
import work.techboard.core.domain.Employee
import work.techboard.core.repository.EmployeeRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * Service Implementation for managing [Employee].
 */
@Service
@Transactional
class EmployeeServiceImpl(
            private val employeeRepository: EmployeeRepository,
) : EmployeeService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(employee: Employee): Employee {
        log.debug("Request to save Employee : $employee")
        return employeeRepository.save(employee)
    }

    override fun update(employee: Employee): Employee{
            log.debug("Request to update Employee : {}", employee);
            return employeeRepository.save(employee)
        }

    override fun partialUpdate(employee: Employee): Optional<Employee> {
        log.debug("Request to partially update Employee : {}", employee)


         return employeeRepository.findById(employee.id)
            .map {

                  if (employee.name!= null) {
                     it.name = employee.name
                  }

               it
            }
            .map { employeeRepository.save(it) }


    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<Employee> {
        log.debug("Request to get all Employees")
        return employeeRepository.findAll()
    }


    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<Employee> {
        log.debug("Request to get Employee : $id")
        return employeeRepository.findById(id)
    }

    override fun delete(id: Long): Unit {
        log.debug("Request to delete Employee : $id")

        employeeRepository.deleteById(id)
    }
}
