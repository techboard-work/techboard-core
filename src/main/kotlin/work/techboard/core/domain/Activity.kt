package work.techboard.core.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.*

/**
 * A Activity.
 */

@Entity
@Table(name = "activity")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Activity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    @get: NotNull

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @get: NotNull

    @Column(name = "started_on", nullable = false)
    var startedOn: Instant? = null,

    @Column(name = "finished_on")
    var finishedOn: Instant? = null,

    @Column(name = "link")
    var link: String? = null,

    @Column(name = "impediment")
    var impediment: Boolean? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "activities",
        ],
        allowSetters = true
    )
    var environment: Environment? = null

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "activities",
        ],
        allowSetters = true
    )
    var kind: ActivityKind? = null

    fun environment(environment: Environment?): Activity {
        this.environment = environment
        return this
    }
    fun kind(activityKind: ActivityKind?): Activity {
        this.kind = activityKind
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Activity) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Activity{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", startedOn='" + startedOn + "'" +
            ", finishedOn='" + finishedOn + "'" +
            ", link='" + link + "'" +
            ", impediment='" + impediment + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}