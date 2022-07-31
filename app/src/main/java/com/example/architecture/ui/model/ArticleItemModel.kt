package com.example.architecture.ui.model

/**
 * This is a model, that is used for displaying UI.
 * It contains minimal amount of data.
 * Each field requires as little postprocessing by UI as possible.
 *
 * Note that this is NOT a best-practice to create separate model for UI. It may be perfectly
 * fine to use the one from data-layer.
 *
 * Unscoped: is created and destroyed as needed.
 */
data class ArticleItemModel(
    val id: Int,
    val title: String,
    val text: String,
    val authorName: String,
    val isBookmarked: Boolean,
)
