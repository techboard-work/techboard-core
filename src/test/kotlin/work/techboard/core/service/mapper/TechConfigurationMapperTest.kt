package work.techboard.core.service.mapper

import org.junit.jupiter.api.BeforeEach

class TechConfigurationMapperTest {

    private lateinit var techConfigurationMapper: TechConfigurationMapper

    @BeforeEach
    fun setUp() {
        techConfigurationMapper = TechConfigurationMapperImpl()
    }
}
