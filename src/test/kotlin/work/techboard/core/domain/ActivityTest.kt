package work.techboard.core.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import work.techboard.core.web.rest.equalsVerifier

import java.util.UUID

class ActivityTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Activity::class)
        val activity1 = Activity()
        activity1.id = 1L
        val activity2 = Activity()
        activity2.id = activity1.id
        assertThat(activity1).isEqualTo(activity2)
        activity2.id = 2L
        assertThat(activity1).isNotEqualTo(activity2)
        activity1.id = null
        assertThat(activity1).isNotEqualTo(activity2)
    }
}
