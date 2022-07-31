package com.example.architecture.ui.view

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.architecture.R
import com.example.architecture.ui.model.ArticleItemModel
import com.example.architecture.ui.stateholders.ArticlesViewModel

/**
 * RecyclerView.ViewHolder implementation for [ArticlesListAdapter].
 *
 * Unscoped: is created and destroyed as needed by adapter.
 */
class ArticleViewHolder(
    itemView: View,
    private val viewModel: ArticlesViewModel,
) : RecyclerView.ViewHolder(itemView) {
    /**
     * Note that view references should be cached for faster bind.
     * Bind can be called several time with different data.
     */
    private val title = itemView.findViewById<TextView>(R.id.article_title)
    private val text = itemView.findViewById<TextView>(R.id.article_text)
    private val author = itemView.findViewById<TextView>(R.id.article_author)
    private val bookmarked = itemView.findViewById<AppCompatImageView>(R.id.article_bookmarked)
    private val delete = itemView.findViewById<AppCompatImageView>(R.id.article_delete)

    /**
     * Set the data from the UI-model int actual view.
     */
    fun bind(article: ArticleItemModel) {
        title.text = article.title
        text.text = article.text
        author.text = article.authorName
        bindBookmarks(article)
        delete.setOnClickListener { viewModel.onDeletePressed(article) }
    }

    /**
     * Note that [bind] would be too long and less readable if we wrote this logic there directly.
     */
    private fun bindBookmarks(article: ArticleItemModel) {
        val bookmarkedImage = if (article.isBookmarked)
            R.drawable.ic_baseline_star_24
        else
            R.drawable.ic_baseline_star_outline_24

        bookmarked.setImageResource(bookmarkedImage)
        bookmarked.setOnClickListener { viewModel.onBookmarkPressed(article) }
    }
}
