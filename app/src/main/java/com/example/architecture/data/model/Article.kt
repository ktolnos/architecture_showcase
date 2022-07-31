package com.example.architecture.data.model

/**
 * Model for article in data layer.
 * E.g. this may be how it is stored in database.
 *
 * Unscoped: is created and destroyed as needed.
 */
data class Article(
    val id: Int,
    val text: String,
    val isBookmarked: Boolean = false,
    val authorId: Int,
)

