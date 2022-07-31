package com.example.architecture.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.architecture.R
import com.example.architecture.ui.model.ArticleItemModel
import com.example.architecture.ui.stateholders.ArticlesViewModel

/**
 * Simple ListAdapter for our article list.
 *
 * Fragment-scoped: lives as long as containing fragment. Can reference activity,
 * fragment, ViewModel, can't reference fragment's views.
 *
 * @see [ListAdapter].
 */
class ArticlesListAdapter(
    private val viewModel: ArticlesViewModel,
    articleDiffCalculator: ArticleItemDiffCalculator,
) : ListAdapter<ArticleItemModel, ArticleViewHolder>(articleDiffCalculator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.article_item,
            parent,
            false
        )
        return ArticleViewHolder(itemView, viewModel)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
