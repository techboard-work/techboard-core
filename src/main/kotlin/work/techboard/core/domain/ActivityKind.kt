package work.techboard.core.domain

import javax.validation.constraints.*
import javax.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable


/**
 * A ActivityKind.
 */
  
@Entity
@Table(name = "activity_kind")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class ActivityKind(

    
              
  
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
        var id: Long? = null,
                  
  
    @get: NotNull
  
  
    @Column(name = "name", nullable = false, unique = true)
        var name: String? = null,
                  
  
    @get: NotNull
  
  
    @Column(name = "description", nullable = false)
        var description: String? = null,
                  
  
    @get: NotNull
  
  
    @Column(name = "color", nullable = false)
        var color: String? = null,
                  
  
    @Column(name = "icon")
        var icon: String? = null,
            
    // jhipster-needle-entity-add-field - JHipster will add fields here
) :  Serializable {

      
  
  
  
    @OneToMany(mappedBy = "kind")
            @JsonIgnoreProperties(value = [
                "environment",
                "kind",
                "owner",
            ], allowSetters = true)
    var activities: MutableSet<Activity>? = mutableSetOf()
  
  
    
                fun addActivity(activity: Activity) : ActivityKind {
        this.activities?.add(activity)
        activity.kind = this
        return this;
    }
                fun removeActivity(activity: Activity) : ActivityKind{
        this.activities?.remove(activity)
        activity.kind = null
        return this;
    }
    
        // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ActivityKind) return false
      return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "ActivityKind{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", color='" + color + "'" +
            ", icon='" + icon + "'" +
            "}";
    }

    companion object {
        private const val serialVersionUID = 1L
            }
}
