package work.techboard.core.web.rest


import work.techboard.core.IntegrationTest
import work.techboard.core.domain.Employee
import work.techboard.core.repository.EmployeeRepository
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*



/**
 * Integration tests for the [EmployeeResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {
    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator


    @Autowired
    private lateinit var em: EntityManager


    @Autowired
    private lateinit var restEmployeeMockMvc: MockMvc

    private lateinit var employee: Employee



    @BeforeEach
    fun initTest() {
        employee = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createEmployee() {
        val databaseSizeBeforeCreate = employeeRepository.findAll().size
        // Create the Employee
        restEmployeeMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(employee))
        ).andExpect(status().isCreated)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1)
        val testEmployee = employeeList[employeeList.size - 1]

        assertThat(testEmployee.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createEmployeeWithExistingId() {
        // Create the Employee with an existing ID
        employee.id = 1L

        val databaseSizeBeforeCreate = employeeRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(employee))
        ).andExpect(status().isBadRequest)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllEmployees() {
        // Initialize the database
        employeeRepository.saveAndFlush(employee)

        // Get all the employeeList
        restEmployeeMockMvc.perform(get(ENTITY_API_URL+ "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))    }
    
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getEmployee() {
        // Initialize the database
        employeeRepository.saveAndFlush(employee)

        val id = employee.id
        assertNotNull(id)

        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, employee.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.id?.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingEmployee() {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingEmployee() {
        // Initialize the database
        employeeRepository.saveAndFlush(employee)

        val databaseSizeBeforeUpdate = employeeRepository.findAll().size

        // Update the employee
        val updatedEmployee = employeeRepository.findById(employee.id).get()
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee)
        updatedEmployee.name = UPDATED_NAME

        restEmployeeMockMvc.perform(
            put(ENTITY_API_URL_ID, updatedEmployee.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedEmployee))
        ).andExpect(status().isOk)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
        val testEmployee = employeeList[employeeList.size - 1]
        assertThat(testEmployee.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    @Transactional
    fun putNonExistingEmployee() {
        val databaseSizeBeforeUpdate = employeeRepository.findAll().size
        employee.id = count.incrementAndGet()


        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(put(ENTITY_API_URL_ID, employee.id).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchEmployee() {
        val databaseSizeBeforeUpdate = employeeRepository.findAll().size
        employee.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(employee))
        ).andExpect(status().isBadRequest)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamEmployee() {
        val databaseSizeBeforeUpdate = employeeRepository.findAll().size
        employee.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(put(ENTITY_API_URL).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateEmployeeWithPatch() {
        employeeRepository.saveAndFlush(employee)
        
        
val databaseSizeBeforeUpdate = employeeRepository.findAll().size

// Update the employee using partial update
val partialUpdatedEmployee = Employee().apply {
    id = employee.id

    
        name = UPDATED_NAME
}


restEmployeeMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedEmployee.id).with(csrf())
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedEmployee)))
.andExpect(status().isOk)

// Validate the Employee in the database
val employeeList = employeeRepository.findAll()
assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
val testEmployee = employeeList.last()
    assertThat(testEmployee.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateEmployeeWithPatch() {
        employeeRepository.saveAndFlush(employee)
        
        
val databaseSizeBeforeUpdate = employeeRepository.findAll().size

// Update the employee using partial update
val partialUpdatedEmployee = Employee().apply {
    id = employee.id

    
        name = UPDATED_NAME
}


restEmployeeMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedEmployee.id).with(csrf())
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedEmployee)))
.andExpect(status().isOk)

// Validate the Employee in the database
val employeeList = employeeRepository.findAll()
assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
val testEmployee = employeeList.last()
    assertThat(testEmployee.name).isEqualTo(UPDATED_NAME)
    }

    @Throws(Exception::class)
    fun patchNonExistingEmployee() {
        val databaseSizeBeforeUpdate = employeeRepository.findAll().size
        employee.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(patch(ENTITY_API_URL_ID, employee.id).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchEmployee() {
        val databaseSizeBeforeUpdate = employeeRepository.findAll().size
        employee.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamEmployee() {
        val databaseSizeBeforeUpdate = employeeRepository.findAll().size
        employee.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(patch(ENTITY_API_URL).with(csrf())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Employee in the database
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteEmployee() {
        // Initialize the database
        employeeRepository.saveAndFlush(employee)
        val databaseSizeBeforeDelete = employeeRepository.findAll().size
        // Delete the employee
        restEmployeeMockMvc.perform(
            delete(ENTITY_API_URL_ID, employee.id).with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val employeeList = employeeRepository.findAll()
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1)
    }


    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"


        private val ENTITY_API_URL: String = "/api/employees"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + ( 2 * Integer.MAX_VALUE ))




        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Employee {
            val employee = Employee(
                name = DEFAULT_NAME

            )


            return employee
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Employee {
            val employee = Employee(
                name = UPDATED_NAME

            )


            return employee
        }

    }
}
