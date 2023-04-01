package work.techboard.core.service
import work.techboard.core.domain.Event
import java.util.Optional

/**
 * Service Interface for managing [Event].
 */
interface EventService {

    /**
     * Save a event.
     *
     * @param event the entity to save.
     * @return the persisted entity.
     */
    fun save(event: Event): Event

    /**
     * Updates a event.
     *
     * @param event the entity to update.
     * @return the persisted entity.
     */
    fun update(event: Event): Event

    /**
     * Partially updates a event.
     *
     * @param event the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(event: Event): Optional<Event>

    /**
     * Get all the events.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Event>

    /**
     * Get the "id" event.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<Event>

    /**
     * Delete the "id" event.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
