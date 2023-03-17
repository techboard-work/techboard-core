package work.techboard.core

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import work.techboard.core.config.AsyncSyncConfiguration
import work.techboard.core.config.EmbeddedSQL
import work.techboard.core.config.TestSecurityConfiguration

/**
 * Base composite annotation for integration tests.
 */
@kotlin.annotation.Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(classes = [TechboardApp::class, AsyncSyncConfiguration::class, TestSecurityConfiguration::class])
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
annotation class IntegrationTest
