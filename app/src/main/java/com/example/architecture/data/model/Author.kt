package com.example.architecture.data.model

/**
 * Model for author in data layer.
 * E.g. this may be how it is stored in database.
 *
 * Unscoped: is created and destroyed as needed.
 */
data class Author(
    val id: Int,
    val name: String,
)
