package work.techboard.core.repository

import java.util.Collections
import java.util.Optional

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import org.hibernate.annotations.QueryHints
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

import work.techboard.core.domain.Activity

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
class ActivityRepositoryWithBagRelationshipsImpl(
    @PersistenceContext
    private val entityManager: EntityManager
): ActivityRepositoryWithBagRelationships {
 
    override fun fetchBagRelationships(activity: Optional<Activity>): Optional<Activity> {
        return activity
            .map(this::fetchTags)
        
    }
 
    override fun fetchBagRelationships(activities: Page<Activity>): Page<Activity> {
        return PageImpl<Activity>(fetchBagRelationships(activities.content), activities.pageable, activities.totalElements)
    }
 
    override fun fetchBagRelationships(activities: List<Activity>): MutableList<Activity> {
        return Optional
            .of(activities)
            .map(this::fetchTags)
            .orElse(Collections.emptyList())
            .toMutableList()
    }

    fun fetchTags(result: Activity): Activity {
        return entityManager
            .createQuery(
                "select activity from Activity activity left join fetch activity.tags where activity is :activity",
                Activity::class.java
            )
            .setParameter("activity", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .singleResult
    }

    fun fetchTags(activities: List<Activity>): List<Activity> {
        val order = mutableMapOf<Any, Int>()
        activities.indices.map {
            order[activities[it].id as Any] = it
        }
        val result = entityManager
            .createQuery(
                "select distinct activity from Activity activity left join fetch activity.tags where activity in :activities",
                Activity::class.java
            )
            .setParameter("activities", activities)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .resultList
        return result.sortedBy { order[it] }
    }
}
