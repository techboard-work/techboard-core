package work.techboard.core.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import work.techboard.core.web.rest.equalsVerifier

class EnvironmentTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Environment::class)
        val environment1 = Environment()
        environment1.id = 1L
        val environment2 = Environment()
        environment2.id = environment1.id
        assertThat(environment1).isEqualTo(environment2)
        environment2.id = 2L
        assertThat(environment1).isNotEqualTo(environment2)
        environment1.id = null
        assertThat(environment1).isNotEqualTo(environment2)
    }
}
