package work.techboard.core.domain

import javax.validation.constraints.*
import javax.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable


/**
 * A Environment.
 */
  
@Entity
@Table(name = "environment")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Environment(

    
              
  
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
        var id: Long? = null,
                  
  
    @get: NotNull
  
  
    @Column(name = "name", nullable = false, unique = true)
        var name: String? = null,
                  
  
    @get: NotNull
  
  
    @Column(name = "label", nullable = false, unique = true)
        var label: String? = null,
                  
  
    @get: NotNull
  
  
    @Column(name = "description", nullable = false)
        var description: String? = null,
                  
  
    @get: NotNull
  
  
    @Column(name = "color", nullable = false)
        var color: String? = null,
                  
  
    @get: NotNull
  
  
    @Column(name = "level", nullable = false)
        var level: Int? = null,
                  
  
    @Column(name = "link")
        var link: String? = null,
            
    // jhipster-needle-entity-add-field - JHipster will add fields here
) :  Serializable {

      
  
  
  
    @OneToMany(mappedBy = "environment")
            @JsonIgnoreProperties(value = [
                "environment",
                "kind",
                "owner",
            ], allowSetters = true)
    var activities: MutableSet<Activity>? = mutableSetOf()
      
  
  
  
    @OneToMany(mappedBy = "environment")
            @JsonIgnoreProperties(value = [
                "environment",
                "reporter",
            ], allowSetters = true)
    var events: MutableSet<Event>? = mutableSetOf()
  
  
    
                fun addActivity(activity: Activity) : Environment {
        this.activities?.add(activity)
        activity.environment = this
        return this;
    }
                fun removeActivity(activity: Activity) : Environment{
        this.activities?.remove(activity)
        activity.environment = null
        return this;
    }
                    fun addEvent(event: Event) : Environment {
        this.events?.add(event)
        event.environment = this
        return this;
    }
                fun removeEvent(event: Event) : Environment{
        this.events?.remove(event)
        event.environment = null
        return this;
    }
    
        // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Environment) return false
      return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Environment{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", label='" + label + "'" +
            ", description='" + description + "'" +
            ", color='" + color + "'" +
            ", level=" + level +
            ", link='" + link + "'" +
            "}";
    }

    companion object {
        private const val serialVersionUID = 1L
            }
}
