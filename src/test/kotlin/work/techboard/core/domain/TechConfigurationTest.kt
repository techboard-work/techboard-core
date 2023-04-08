package work.techboard.core.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import work.techboard.core.web.rest.equalsVerifier

class TechConfigurationTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(TechConfiguration::class)
        val techConfiguration1 = TechConfiguration()
        techConfiguration1.id = 1L
        val techConfiguration2 = TechConfiguration()
        techConfiguration2.id = techConfiguration1.id
        assertThat(techConfiguration1).isEqualTo(techConfiguration2)
        techConfiguration2.id = 2L
        assertThat(techConfiguration1).isNotEqualTo(techConfiguration2)
        techConfiguration1.id = null
        assertThat(techConfiguration1).isNotEqualTo(techConfiguration2)
    }
}
