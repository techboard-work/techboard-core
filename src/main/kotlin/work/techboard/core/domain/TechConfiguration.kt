package work.techboard.core.domain

import java.io.Serializable
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.*

/**
 * A TechConfiguration.
 */

@Entity
@Table(name = "tech_configuration")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TechConfiguration(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    @get: NotNull
    @get: Min(value = 0)
    @get: Max(value = 1000000)

    @Column(name = "version", nullable = false, unique = true)
    var version: Int? = null,

    @get: NotNull

    @Column(name = "timestamp", nullable = false)
    var timestamp: Instant? = null,

    @get: NotNull

    @Column(name = "content", nullable = false)
    var content: String? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    var author: User? = null

    fun author(user: User?): TechConfiguration {
        this.author = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TechConfiguration) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "TechConfiguration{" +
            "id=" + id +
            ", version=" + version +
            ", timestamp='" + timestamp + "'" +
            ", content='" + content + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
