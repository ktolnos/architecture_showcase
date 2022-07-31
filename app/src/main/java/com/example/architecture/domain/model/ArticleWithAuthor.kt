package com.example.architecture.domain.model

import com.example.architecture.data.model.Article
import com.example.architecture.data.model.Author

/**
 * We use composition to store together article and corresponding author.
 *
 * Unscoped: is created and destroyed as needed.
 */
data class ArticleWithAuthor(
    val article: Article,
    val author: Author?,
)
