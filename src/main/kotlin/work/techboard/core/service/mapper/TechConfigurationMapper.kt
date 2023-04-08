package work.techboard.core.service.mapper

import org.mapstruct.*
import work.techboard.core.domain.TechConfiguration
import work.techboard.core.domain.User
import work.techboard.core.service.dto.TechConfigurationDTO
import work.techboard.core.service.dto.UserDTO

/**
 * Mapper for the entity [TechConfiguration] and its DTO [TechConfigurationDTO].
 */
@Mapper(componentModel = "spring")
interface TechConfigurationMapper :
    EntityMapper<TechConfigurationDTO, TechConfiguration> {

    @Mappings(
        Mapping(target = "author", source = "author", qualifiedByName = ["userLogin"])
    )
    override fun toDto(s: TechConfiguration): TechConfigurationDTO @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id"), Mapping(target = "login", source = "login")
    )
    fun toDtoUserLogin(user: User): UserDTO
}
