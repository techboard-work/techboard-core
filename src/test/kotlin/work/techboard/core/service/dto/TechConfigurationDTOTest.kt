package work.techboard.core.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import work.techboard.core.web.rest.equalsVerifier

class TechConfigurationDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(TechConfigurationDTO::class)
        val techConfigurationDTO1 = TechConfigurationDTO()
        techConfigurationDTO1.id = 1L
        val techConfigurationDTO2 = TechConfigurationDTO()
        assertThat(techConfigurationDTO1).isNotEqualTo(techConfigurationDTO2)
        techConfigurationDTO2.id = techConfigurationDTO1.id
        assertThat(techConfigurationDTO1).isEqualTo(techConfigurationDTO2)
        techConfigurationDTO2.id = 2L
        assertThat(techConfigurationDTO1).isNotEqualTo(techConfigurationDTO2)
        techConfigurationDTO1.id = null
        assertThat(techConfigurationDTO1).isNotEqualTo(techConfigurationDTO2)
    }
}
