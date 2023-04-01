package work.techboard.core.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.*

/**
 * A Event.
 */

@Entity
@Table(name = "event")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Event(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    @get: NotNull

    @Column(name = "message", nullable = false)
    var message: String? = null,

    @get: NotNull

    @Column(name = "received_on", nullable = false)
    var receivedOn: Instant? = null,

    @Column(name = "link")
    var link: String? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "activities",
            "events",
        ],
        allowSetters = true
    )
    var environment: Environment? = null

    @ManyToOne
    var reporter: User? = null

    fun environment(environment: Environment?): Event {
        this.environment = environment
        return this
    }
    fun reporter(user: User?): Event {
        this.reporter = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Event) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Event{" +
            "id=" + id +
            ", message='" + message + "'" +
            ", receivedOn='" + receivedOn + "'" +
            ", link='" + link + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
