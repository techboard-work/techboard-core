package work.techboard.core.repository

import org.springframework.data.domain.Page
import work.techboard.core.domain.Activity
import java.util.Optional

interface ActivityRepositoryWithBagRelationships {
    fun fetchBagRelationships(activity: Optional<Activity>): Optional<Activity>

    fun fetchBagRelationships(activities: List<Activity>): MutableList<Activity>

    fun fetchBagRelationships(activities: Page<Activity>): Page<Activity>
}
