package com.example.architecture.data.datasource

import com.example.architecture.data.model.Article
import com.example.architecture.data.model.Author
import kotlinx.coroutines.delay

/**
 * Data source contains logic for loading data from somewhere, e.g. from network or from database.
 * It should contain only logic about one source of the data, it should not use both network and
 * database.
 *
 * Application-scoped: lives as long as an application itself. Is not recreated on configuration
 * changes or on navigation.
 */
class HardCodedDataSource {
    private val articles = listOf(
        Article(
            0,
            """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dignissim dui tortor, a sagittis urna tincidunt convallis. Fusce lectus nisl, ullamcorper sit amet ligula vitae, congue pulvinar sem. Phasellus faucibus nunc in ipsum pellentesque viverra. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Quisque bibendum eros in luctus mattis. Donec maximus at nunc id blandit. In blandit ex a dignissim ullamcorper. Duis ultrices nisi sem, nec lobortis elit auctor in. Sed pharetra porta tellus, vel elementum sapien hendrerit id. Nullam fringilla ex vitae fringilla consequat. Praesent id nulla at nisl accumsan vehicula eu sit amet eros. Nullam viverra diam vel turpis dignissim, vel lobortis massa mollis. Morbi sed ante non neque posuere gravida at eget dui.
            """.trimIndent(),
            false,
            1,
        ),
        Article(
            1,
            """
                In euismod ante vitae auctor vestibulum. Quisque id nulla sed ipsum pharetra eleifend. Nam non accumsan massa. Nam iaculis sapien sit amet eleifend accumsan. Mauris placerat nibh eu vehicula faucibus. Vivamus gravida neque lorem, nec imperdiet quam maximus in. Aliquam ex sem, mattis sit amet sem eu, sodales vestibulum purus. Fusce tellus nunc, scelerisque fermentum egestas et, lobortis id sapien. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas auctor arcu et nisi scelerisque, sit amet mollis mi ultricies.            """.trimIndent(),
            false,
            0,
        ),
        Article(
            2,
            """
                Sed lacinia massa iaculis efficitur ullamcorper. Aliquam vel malesuada nibh. Nullam pulvinar dolor augue, nec hendrerit dolor sodales ac. Maecenas quis auctor tortor, vitae cursus mi. Donec dapibus placerat elit et convallis. Curabitur viverra rhoncus urna, ut dignissim tellus. Aliquam at pulvinar nisi. Interdum et malesuada fames ac ante ipsum primis in faucibus. Cras finibus lorem eget dolor rhoncus, quis maximus ante vestibulum. Interdum et malesuada fames ac ante ipsum primis in faucibus. Aliquam erat volutpat. Aliquam id iaculis nisi, suscipit vulputate quam. Interdum et malesuada fames ac ante ipsum primis in faucibus.
            """.trimIndent(),
            true,
            0,
        ),
        Article(
            3,
            """
                Fusce vitae orci sed urna blandit consectetur. Cras vestibulum quis lorem ac ultricies. Donec turpis tellus, aliquam ut tortor nec, volutpat faucibus justo. Nunc a felis tempus, dapibus nulla id, hendrerit metus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis rhoncus placerat erat, sed vulputate mauris maximus vitae. Morbi risus dui, dictum commodo tortor id, imperdiet iaculis eros. Suspendisse sed nisi mollis, accumsan nibh sed, suscipit est. In vitae dolor at metus viverra vulputate at sodales felis. Vivamus at diam vel risus pellentesque rutrum.
            """.trimIndent(),
            false,
            0,
        ),
        Article(
            4,
            """
                Mauris sapien mi, porta non pharetra ac, consequat ut arcu. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Duis non diam felis. Quisque non leo venenatis erat congue ornare sed ac arcu. Mauris ultrices neque a leo gravida, sit amet scelerisque ligula finibus. Cras auctor nibh at risus pellentesque tincidunt. Integer accumsan, odio sodales laoreet aliquam, magna nunc eleifend ante, id rutrum velit urna id nisi.
            """.trimIndent(),
            true,
            2,
        ),
    )

    private val authors = listOf(
        Author(
            0,
            "John Smith",
        ),
        Author(
            1,
            "John Doe",
        ),
        Author(
            2,
            "Patrick",
        ),
    )

    /**
     * Simulates articles loading with small delay.
     */
    suspend fun loadArticles(): List<Article> {
        delay(300L)
        return articles
    }

    /**
     * Simulates authors loading with bigger delay.
     */
    suspend fun loadAuthors(): List<Author> {
        delay(1300L)
        return authors
    }
}
