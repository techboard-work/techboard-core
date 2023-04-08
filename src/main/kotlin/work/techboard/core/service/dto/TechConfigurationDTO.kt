package work.techboard.core.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.Objects
import javax.validation.constraints.*

/**
 * A DTO for the [work.techboard.core.domain.TechConfiguration] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TechConfigurationDTO(

    var id: Long? = null,

    @get: NotNull
    @get: Min(value = 0)
    @get: Max(value = 1000000)
    var version: Int? = null,

    @get: NotNull
    var timestamp: Instant? = null,

    @get: NotNull
    var content: String? = null,

    var author: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TechConfigurationDTO) return false
        val techConfigurationDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, techConfigurationDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
