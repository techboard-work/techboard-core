package work.techboard.core.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import work.techboard.core.domain.TechConfiguration
import java.util.Optional

/**
 * Spring Data JPA repository for the TechConfiguration entity.
 */
@Repository
interface TechConfigurationRepository : JpaRepository<TechConfiguration, Long> {

    @Query("select techConfiguration from TechConfiguration techConfiguration where techConfiguration.author.login = ?#{principal.preferredUsername}")
    fun findByAuthorIsCurrentUser(): MutableList<TechConfiguration>

    @JvmDefault fun findOneWithEagerRelationships(id: Long): Optional<TechConfiguration> {
        return this.findOneWithToOneRelationships(id)
    }

    @JvmDefault fun findAllWithEagerRelationships(): MutableList<TechConfiguration> {
        return this.findAllWithToOneRelationships()
    }

    @JvmDefault fun findAllWithEagerRelationships(pageable: Pageable): Page<TechConfiguration> {
        return this.findAllWithToOneRelationships(pageable)
    }

    @Query(
        value = "select distinct techConfiguration from TechConfiguration techConfiguration left join fetch techConfiguration.author",
        countQuery = "select count(distinct techConfiguration) from TechConfiguration techConfiguration"
    )
    fun findAllWithToOneRelationships(pageable: Pageable): Page<TechConfiguration>

    @Query("select distinct techConfiguration from TechConfiguration techConfiguration left join fetch techConfiguration.author")
    fun findAllWithToOneRelationships(): MutableList<TechConfiguration>

    @Query("select techConfiguration from TechConfiguration techConfiguration left join fetch techConfiguration.author where techConfiguration.id =:id")
    fun findOneWithToOneRelationships(@Param("id") id: Long): Optional<TechConfiguration>
}
