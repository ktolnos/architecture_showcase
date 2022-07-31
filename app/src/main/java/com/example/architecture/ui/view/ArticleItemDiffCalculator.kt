package com.example.architecture.ui.view

import androidx.recyclerview.widget.DiffUtil
import com.example.architecture.ui.model.ArticleItemModel

/**
 * Required for correct animations in recycler.
 *
 * Unscoped: is created and destroyed as needed. This class is stateless so we can create a new one
 * whenever we want to.
 */
class ArticleItemDiffCalculator : DiffUtil.ItemCallback<ArticleItemModel>() {
    override fun areItemsTheSame(oldItem: ArticleItemModel, newItem: ArticleItemModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleItemModel, newItem: ArticleItemModel): Boolean {
        return oldItem == newItem
    }
}
