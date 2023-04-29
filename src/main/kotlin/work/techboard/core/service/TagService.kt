package work.techboard.core.service

import work.techboard.core.domain.Tag
import work.techboard.core.repository.TagRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * Service Implementation for managing [Tag].
 */
@Service
@Transactional
class TagService(
            private val tagRepository: TagRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a tag.
     *
     * @param tag the entity to save.
     * @return the persisted entity.
     */
    fun save(tag: Tag): Tag {
        log.debug("Request to save Tag : $tag")
        return tagRepository.save(tag)
    }

    /**
        * Update a tag.
        *
        * @param tag the entity to save.
        * @return the persisted entity.
    */
     fun update(tag: Tag): Tag{
            log.debug("Request to update Tag : {}", tag);
            return tagRepository.save(tag)
        }

    /**
        * Partially updates a tag.
        *
        * @param tag the entity to update partially.
        * @return the persisted entity.
        */
    fun partialUpdate(tag: Tag): Optional<Tag> {
        log.debug("Request to partially update Tag : {}", tag)


         return tagRepository.findById(tag.id)
            .map {

                  if (tag.tag!= null) {
                     it.tag = tag.tag
                  }
                  if (tag.order!= null) {
                     it.order = tag.order
                  }
                  if (tag.color!= null) {
                     it.color = tag.color
                  }
                  if (tag.active!= null) {
                     it.active = tag.active
                  }
                  if (tag.description!= null) {
                     it.description = tag.description
                  }
                  if (tag.icon!= null) {
                     it.icon = tag.icon
                  }
                  if (tag.link!= null) {
                     it.link = tag.link
                  }

               it
            }
            .map { tagRepository.save(it) }


    }

    /**
     * Get all the tags.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(): MutableList<Tag> {
        log.debug("Request to get all Tags")
        return tagRepository.findAll()
    }


    /**
     * Get one tag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<Tag> {
        log.debug("Request to get Tag : $id")
        return tagRepository.findById(id)
    }

    /**
     * Delete the tag by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long): Unit {
        log.debug("Request to delete Tag : $id")

        tagRepository.deleteById(id)
    }
}
