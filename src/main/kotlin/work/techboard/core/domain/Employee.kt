package work.techboard.core.domain

import javax.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable


/**
 * A Employee.
 */
  
@Entity
@Table(name = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Employee(

    
              
  
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
        var id: Long? = null,
                  
  
    @Column(name = "name")
        var name: String? = null,
            
    // jhipster-needle-entity-add-field - JHipster will add fields here
) :  Serializable {

      
  
  
  
    @OneToMany(mappedBy = "employee")
            @JsonIgnoreProperties(value = [
                "employee",
                "environment",
                "kind",
            ], allowSetters = true)
    var activities: MutableSet<Activity>? = mutableSetOf()
  
  
    
                fun addActivity(activity: Activity) : Employee {
        this.activities?.add(activity)
        activity.employee = this
        return this;
    }
                fun removeActivity(activity: Activity) : Employee{
        this.activities?.remove(activity)
        activity.employee = null
        return this;
    }
    
        // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Employee) return false
      return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Employee{" +
            "id=" + id +
            ", name='" + name + "'" +
            "}";
    }

    companion object {
        private const val serialVersionUID = 1L
            }
}
