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

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "link")
    var link: String? = null,

    @get: NotNull

    @Column(name = "flagged", nullable = false)
    var flagged: Boolean? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToMany
    @JoinTable(
        name = "rel_activity__tag",
        joinColumns = [
            JoinColumn(name = "activity_id")
        ],
        inverseJoinColumns = [
            JoinColumn(name = "tag_id")
        ]
    )
    @JsonIgnoreProperties(
        value = [
            "activities",
        ],
        allowSetters = true
    )
    var tags: MutableSet<Tag>? = mutableSetOf()

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = [
            "activities",
        ],
        allowSetters = true
    )
    var environment: Environment? = null

    @ManyToOne
    var owner: User? = null

    fun addTag(tag: Tag): Activity {
        this.tags?.add(tag)
        tag.activities?.add(this)
        return this
    }
    fun removeTag(tag: Tag): Activity {
        this.tags?.remove(tag)
        tag.activities?.remove(this)
        return this
    }
    fun environment(environment: Environment?): Activity {
        this.environment = environment
        return this
    }
    fun owner(user: User?): Activity {
        this.owner = user
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
            ", description='" + description + "'" +
            ", link='" + link + "'" +
            ", flagged='" + flagged + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
