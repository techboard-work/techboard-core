package work.techboard.core.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.*

/**
 * A Tag.
 */

@Entity
@Table(name = "tag")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Tag(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    @get: NotNull

    @Column(name = "tag", nullable = false, unique = true)
    var tag: String? = null,

    @get: NotNull

    @Column(name = "jhi_order", nullable = false)
    var order: Int? = null,

    @get: NotNull

    @Column(name = "color", nullable = false)
    var color: String? = null,

    @get: NotNull

    @Column(name = "active", nullable = false)
    var active: Boolean? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "icon")
    var icon: String? = null,

    @Column(name = "link")
    var link: String? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToMany(mappedBy = "tags")
    @JsonIgnoreProperties(
        value = [
            "tags",
            "environment",
            "owner",
        ],
        allowSetters = true
    )
    var activities: MutableSet<Activity>? = mutableSetOf()

    fun addActivity(activity: Activity): Tag {
        this.activities?.add(activity)
        activity.tags?.add(this)
        return this
    }
    fun removeActivity(activity: Activity): Tag {
        this.activities?.remove(activity)
        activity.tags?.remove(this)
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tag) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Tag{" +
            "id=" + id +
            ", tag='" + tag + "'" +
            ", order=" + order +
            ", color='" + color + "'" +
            ", active='" + active + "'" +
            ", description='" + description + "'" +
            ", icon='" + icon + "'" +
            ", link='" + link + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
