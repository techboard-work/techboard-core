package work.techboard.core.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import work.techboard.core.web.rest.equalsVerifier

import java.util.UUID

class ActivityKindTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(ActivityKind::class)
        val activityKind1 = ActivityKind()
        activityKind1.id = 1L
        val activityKind2 = ActivityKind()
        activityKind2.id = activityKind1.id
        assertThat(activityKind1).isEqualTo(activityKind2)
        activityKind2.id = 2L
        assertThat(activityKind1).isNotEqualTo(activityKind2)
        activityKind1.id = null
        assertThat(activityKind1).isNotEqualTo(activityKind2)
    }
}
