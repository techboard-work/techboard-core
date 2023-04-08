package work.techboard.core.service.impl

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import work.techboard.core.domain.Activity
import work.techboard.core.repository.ActivityRepository
import work.techboard.core.service.ActivityService
import java.time.Instant
import kotlin.test.assertEquals

/**
 * Unit tests for [ActivityServiceImpl].
 */
class ActivityServiceImplTest {

    private val activityRepository: ActivityRepository = mock(ActivityRepository::class.java)
    val service: ActivityService = ActivityServiceImpl(activityRepository)
    private val base = Instant.ofEpochMilli(1000000)

    private fun createList(): List<Activity> {
        return listOf(
            createList(1001),
            createList(1003, true),
            createList(1005, true),
            createList(1007),
            createList(1009, false, base.plusSeconds(1000)),
            createList(1011, true, base.plusSeconds(1000)),
            createList(1013, true, base.plusSeconds(1000)),
            createList(1015, false, base.plusSeconds(1000)),
        )
    }

    private fun createList(id: Long, severe: Boolean = false, finished: Instant? = null) =
        Activity(
            id,
            "name$id",
            Instant.ofEpochMilli(base.toEpochMilli() + id),
            finished,
            "http://link/$id",
            if (severe) 1 else 5
        )

    @Test
    fun `test that DnD goes first`() {
        val timestamp = Instant.ofEpochMilli(0)
        Mockito.`when`(activityRepository.findCurrentIn(timestamp, listOf(100))).thenReturn(createList())
        val result: List<Activity> = service.getCurrentActivities(timestamp, listOf(100))
        val ids = result.map { a -> a.id!!.toInt() }
        assertEquals(8, result.size)
        val expected = listOf(1003, 1005, 1011, 1013, 1001, 1007, 1009, 1015)
        assertEquals(expected, ids)
    }
}
