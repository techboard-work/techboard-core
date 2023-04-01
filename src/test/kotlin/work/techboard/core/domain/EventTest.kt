package work.techboard.core.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import work.techboard.core.web.rest.equalsVerifier

class EventTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Event::class)
        val event1 = Event()
        event1.id = 1L
        val event2 = Event()
        event2.id = event1.id
        assertThat(event1).isEqualTo(event2)
        event2.id = 2L
        assertThat(event1).isNotEqualTo(event2)
        event1.id = null
        assertThat(event1).isNotEqualTo(event2)
    }
}
