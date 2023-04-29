package work.techboard.core.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import work.techboard.core.IntegrationTest
import work.techboard.core.domain.Tag
import work.techboard.core.repository.TagRepository
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

/**
 * Integration tests for the [TagResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagResourceIT {
    @Autowired
    private lateinit var tagRepository: TagRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restTagMockMvc: MockMvc

    private lateinit var theTag: Tag

    @BeforeEach
    fun initTest() {
        theTag = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTag() {
        val databaseSizeBeforeCreate = tagRepository.findAll().size
        // Create the Tag
        restTagMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        ).andExpect(status().isCreated)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1)
        val testTag = tagList[tagList.size - 1]

        assertThat(testTag.tag).isEqualTo(DEFAULT_TAG)
        assertThat(testTag.order).isEqualTo(DEFAULT_ORDER)
        assertThat(testTag.color).isEqualTo(DEFAULT_COLOR)
        assertThat(testTag.active).isEqualTo(DEFAULT_ACTIVE)
        assertThat(testTag.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testTag.icon).isEqualTo(DEFAULT_ICON)
        assertThat(testTag.link).isEqualTo(DEFAULT_LINK)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTagWithExistingId() {
        // Create the Tag with an existing ID
        theTag.id = 1L

        val databaseSizeBeforeCreate = tagRepository.findAll().size
        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        ).andExpect(status().isBadRequest)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkTagIsRequired() {
        val databaseSizeBeforeTest = tagRepository.findAll().size
        // set the field null
        theTag.tag = null

        // Create the Tag, which fails.

        restTagMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        ).andExpect(status().isBadRequest)

        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkOrderIsRequired() {
        val databaseSizeBeforeTest = tagRepository.findAll().size
        // set the field null
        theTag.order = null

        // Create the Tag, which fails.

        restTagMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        ).andExpect(status().isBadRequest)

        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkColorIsRequired() {
        val databaseSizeBeforeTest = tagRepository.findAll().size
        // set the field null
        theTag.color = null

        // Create the Tag, which fails.

        restTagMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        ).andExpect(status().isBadRequest)

        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeTest)
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun checkActiveIsRequired() {
        val databaseSizeBeforeTest = tagRepository.findAll().size
        // set the field null
        theTag.active = null

        // Create the Tag, which fails.

        restTagMockMvc.perform(
            post(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        ).andExpect(status().isBadRequest)

        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTags() {
        // Initialize the database
        tagRepository.saveAndFlush(theTag)

        // Get all the tagList
        restTagMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theTag.id?.toInt())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getTag() {
        // Initialize the database
        tagRepository.saveAndFlush(theTag)

        val id = theTag.id
        assertNotNull(id)

        // Get the tag
        restTagMockMvc.perform(get(ENTITY_API_URL_ID, theTag.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(theTag.id?.toInt()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingTag() {
        // Get the tag
        restTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putExistingTag() {
        // Initialize the database
        tagRepository.saveAndFlush(theTag)

        val databaseSizeBeforeUpdate = tagRepository.findAll().size

        // Update the tag
        val updatedTag = tagRepository.findById(theTag.id).get()
        // Disconnect from session so that the updates on updatedTag are not directly saved in db
        em.detach(updatedTag)
        updatedTag.tag = UPDATED_TAG
        updatedTag.order = UPDATED_ORDER
        updatedTag.color = UPDATED_COLOR
        updatedTag.active = UPDATED_ACTIVE
        updatedTag.description = UPDATED_DESCRIPTION
        updatedTag.icon = UPDATED_ICON
        updatedTag.link = UPDATED_LINK

        restTagMockMvc.perform(
            put(ENTITY_API_URL_ID, updatedTag.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedTag))
        ).andExpect(status().isOk)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
        val testTag = tagList[tagList.size - 1]
        assertThat(testTag.tag).isEqualTo(UPDATED_TAG)
        assertThat(testTag.order).isEqualTo(UPDATED_ORDER)
        assertThat(testTag.color).isEqualTo(UPDATED_COLOR)
        assertThat(testTag.active).isEqualTo(UPDATED_ACTIVE)
        assertThat(testTag.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testTag.icon).isEqualTo(UPDATED_ICON)
        assertThat(testTag.link).isEqualTo(UPDATED_LINK)
    }

    @Test
    @Transactional
    fun putNonExistingTag() {
        val databaseSizeBeforeUpdate = tagRepository.findAll().size
        theTag.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(
            put(ENTITY_API_URL_ID, theTag.id).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        )
            .andExpect(status().isBadRequest)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchTag() {
        val databaseSizeBeforeUpdate = tagRepository.findAll().size
        theTag.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        ).andExpect(status().isBadRequest)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamTag() {
        val databaseSizeBeforeUpdate = tagRepository.findAll().size
        theTag.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(
            put(ENTITY_API_URL).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(theTag))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateTagWithPatch() {
        tagRepository.saveAndFlush(theTag)

        val databaseSizeBeforeUpdate = tagRepository.findAll().size

// Update the tag using partial update
        val partialUpdatedTag = Tag().apply {
            id = theTag.id

            order = UPDATED_ORDER
            color = UPDATED_COLOR
            active = UPDATED_ACTIVE
            description = UPDATED_DESCRIPTION
            icon = UPDATED_ICON
        }

        restTagMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedTag.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedTag))
        )
            .andExpect(status().isOk)

// Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
        val testTag = tagList.last()
        assertThat(testTag.tag).isEqualTo(DEFAULT_TAG)
        assertThat(testTag.order).isEqualTo(UPDATED_ORDER)
        assertThat(testTag.color).isEqualTo(UPDATED_COLOR)
        assertThat(testTag.active).isEqualTo(UPDATED_ACTIVE)
        assertThat(testTag.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testTag.icon).isEqualTo(UPDATED_ICON)
        assertThat(testTag.link).isEqualTo(DEFAULT_LINK)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateTagWithPatch() {
        tagRepository.saveAndFlush(theTag)

        val databaseSizeBeforeUpdate = tagRepository.findAll().size

// Update the tag using partial update
        val partialUpdatedTag = Tag().apply {
            id = theTag.id

            tag = UPDATED_TAG
            order = UPDATED_ORDER
            color = UPDATED_COLOR
            active = UPDATED_ACTIVE
            description = UPDATED_DESCRIPTION
            icon = UPDATED_ICON
            link = UPDATED_LINK
        }

        restTagMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedTag.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedTag))
        )
            .andExpect(status().isOk)

// Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
        val testTag = tagList.last()
        assertThat(testTag.tag).isEqualTo(UPDATED_TAG)
        assertThat(testTag.order).isEqualTo(UPDATED_ORDER)
        assertThat(testTag.color).isEqualTo(UPDATED_COLOR)
        assertThat(testTag.active).isEqualTo(UPDATED_ACTIVE)
        assertThat(testTag.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testTag.icon).isEqualTo(UPDATED_ICON)
        assertThat(testTag.link).isEqualTo(UPDATED_LINK)
    }

    @Throws(Exception::class)
    fun patchNonExistingTag() {
        val databaseSizeBeforeUpdate = tagRepository.findAll().size
        theTag.id = count.incrementAndGet()

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(
            patch(ENTITY_API_URL_ID, theTag.id).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(theTag))
        )
            .andExpect(status().isBadRequest)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchTag() {
        val databaseSizeBeforeUpdate = tagRepository.findAll().size
        theTag.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet()).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(theTag))
        )
            .andExpect(status().isBadRequest)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamTag() {
        val databaseSizeBeforeUpdate = tagRepository.findAll().size
        theTag.id = count.incrementAndGet()

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(
            patch(ENTITY_API_URL).with(csrf())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(theTag))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Tag in the database
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteTag() {
        // Initialize the database
        tagRepository.saveAndFlush(theTag)
        val databaseSizeBeforeDelete = tagRepository.findAll().size
        // Delete the tag
        restTagMockMvc.perform(
            delete(ENTITY_API_URL_ID, theTag.id).with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val tagList = tagRepository.findAll()
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_TAG = "AAAAAAAAAA"
        private const val UPDATED_TAG = "BBBBBBBBBB"

        private const val DEFAULT_ORDER: Int = 1
        private const val UPDATED_ORDER: Int = 2

        private const val DEFAULT_COLOR = "AAAAAAAAAA"
        private const val UPDATED_COLOR = "BBBBBBBBBB"

        private const val DEFAULT_ACTIVE: Boolean = false
        private const val UPDATED_ACTIVE: Boolean = true

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_ICON = "AAAAAAAAAA"
        private const val UPDATED_ICON = "BBBBBBBBBB"

        private const val DEFAULT_LINK = "AAAAAAAAAA"
        private const val UPDATED_LINK = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/tags"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Tag {
            val tag = Tag(
                tag = DEFAULT_TAG,

                order = DEFAULT_ORDER,

                color = DEFAULT_COLOR,

                active = DEFAULT_ACTIVE,

                description = DEFAULT_DESCRIPTION,

                icon = DEFAULT_ICON,

                link = DEFAULT_LINK

            )

            return tag
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Tag {
            val tag = Tag(
                tag = UPDATED_TAG,

                order = UPDATED_ORDER,

                color = UPDATED_COLOR,

                active = UPDATED_ACTIVE,

                description = UPDATED_DESCRIPTION,

                icon = UPDATED_ICON,

                link = UPDATED_LINK

            )

            return tag
        }
    }
}
