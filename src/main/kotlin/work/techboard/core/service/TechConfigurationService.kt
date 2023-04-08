package work.techboard.core.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.TechConfiguration
import work.techboard.core.repository.TechConfigurationRepository
import work.techboard.core.service.dto.TechConfigurationDTO
import work.techboard.core.service.mapper.TechConfigurationMapper
import java.util.Optional

/**
 * Service Implementation for managing [TechConfiguration].
 */
@Service
@Transactional
class TechConfigurationService(
    private val techConfigurationRepository: TechConfigurationRepository,
    private val techConfigurationMapper: TechConfigurationMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a techConfiguration.
     *
     * @param techConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(techConfigurationDTO: TechConfigurationDTO): TechConfigurationDTO {
        log.debug("Request to save TechConfiguration : $techConfigurationDTO")
        var techConfiguration = techConfigurationMapper.toEntity(techConfigurationDTO)
        techConfiguration = techConfigurationRepository.save(techConfiguration)
        return techConfigurationMapper.toDto(techConfiguration)
    }

    /**
     * Update a techConfiguration.
     *
     * @param techConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    fun update(techConfigurationDTO: TechConfigurationDTO): TechConfigurationDTO {
        log.debug("Request to update TechConfiguration : {}", techConfigurationDTO)
        var techConfiguration = techConfigurationMapper.toEntity(techConfigurationDTO)
        techConfiguration = techConfigurationRepository.save(techConfiguration)
        return techConfigurationMapper.toDto(techConfiguration)
    }

    /**
     * Partially updates a techConfiguration.
     *
     * @param techConfigurationDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(techConfigurationDTO: TechConfigurationDTO): Optional<TechConfigurationDTO> {
        log.debug("Request to partially update TechConfiguration : {}", techConfigurationDTO)

        return techConfigurationRepository.findById(techConfigurationDTO.id)
            .map {
                techConfigurationMapper.partialUpdate(it, techConfigurationDTO)
                it
            }
            .map { techConfigurationRepository.save(it) }
            .map { techConfigurationMapper.toDto(it) }
    }

    /**
     * Get all the techConfigurations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(): MutableList<TechConfigurationDTO> {
        log.debug("Request to get all TechConfigurations")
        return techConfigurationRepository.findAll()
            .mapTo(mutableListOf(), techConfigurationMapper::toDto)
    }

    /**
     * Get all the techConfigurations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        techConfigurationRepository.findAllWithEagerRelationships(pageable).map(techConfigurationMapper::toDto)

    /**
     * Get one techConfiguration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<TechConfigurationDTO> {
        log.debug("Request to get TechConfiguration : $id")
        return techConfigurationRepository.findOneWithEagerRelationships(id)
            .map(techConfigurationMapper::toDto)
    }

    /**
     * Delete the techConfiguration by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete TechConfiguration : $id")

        techConfigurationRepository.deleteById(id)
    }
}
