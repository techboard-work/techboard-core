package work.techboard.core.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import work.techboard.core.domain.Event
import work.techboard.core.repository.EventRepository
import work.techboard.core.service.EventService
import java.util.Optional

/**
 * Service Implementation for managing [Event].
 */
@Service
@Transactional
class EventServiceImpl(
    private val eventRepository: EventRepository,
) : EventService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(event: Event): Event {
        log.debug("Request to save Event : $event")
        return eventRepository.save(event)
    }

    override fun update(event: Event): Event {
        log.debug("Request to update Event : {}", event)
        return eventRepository.save(event)
    }

    override fun partialUpdate(event: Event): Optional<Event> {
        log.debug("Request to partially update Event : {}", event)

        return eventRepository.findById(event.id)
            .map {

                if (event.message != null) {
                    it.message = event.message
                }
                if (event.receivedOn != null) {
                    it.receivedOn = event.receivedOn
                }
                if (event.link != null) {
                    it.link = event.link
                }

                it
            }
            .map { eventRepository.save(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<Event> {
        log.debug("Request to get all Events")
        return eventRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<Event> {
        log.debug("Request to get Event : $id")
        return eventRepository.findById(id)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete Event : $id")

        eventRepository.deleteById(id)
    }
}
