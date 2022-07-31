package com.example.architecture.ui.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.architecture.data.model.Article
import com.example.architecture.data.repository.ArticleRepository
import com.example.architecture.domain.model.ArticleWithAuthor
import com.example.architecture.domain.usecases.ArticlesWithAuthorsUseCase
import com.example.architecture.ui.model.ArticleItemModel
import kotlinx.coroutines.launch

private const val ARTICLE_TEXT_DISPLAY_LENGTH = 300
private const val ARTICLE_TOO_LONG_DOTS = "..."

/**
 * The view model for article list.
 * Responsible for initiating data loading, transforming data into UI format
 * and handling user actions.
 *
 * This is quite a lot of responsibilities, so probably it is a good idea to offload e.g.
 * data transformation to the separate class, if this one gets bigger.
 *
 * ViewModel survives configuration changes, but can be destroyed if user completely leaves the
 * corresponding screen (if the screen stops being in the backstack and the fragment is
 * permanently destroyed).
 *
 * @see ViewModel
 */
class ArticlesViewModel(
    private val articleRepository: ArticleRepository,
    private val articlesWithAuthorsUseCase: ArticlesWithAuthorsUseCase,
) : ViewModel() {
    /**
     * Uses [articlesWithAuthorsUseCase] to receive updates on articles with corresponding action.
     * First [map] call is transforming LiveData value from List<[ArticleWithAuthor]> into
     * List<[ArticleItemModel]>.
     * Second [map] transforms each item of the list from [ArticleWithAuthor]
     * into [ArticleItemModel].
     *
     * All transformations will take place only if someone observes [articles] livedata.
     */
    val articles = articlesWithAuthorsUseCase.articlesWithAuthors.map { list ->
        list.map { it.transformToItemModel() }
    }

    init {
        /**
         * Start loading the articles and authors as soon as this ViewModel is created.
         * [viewModelScope] will automatically cancel an execution if the ViewModel
         * is destroyed (onClear).
         */
        viewModelScope.launch {
            articlesWithAuthorsUseCase.updateArticlesAndAuthors()
        }
    }

    /**
     * Handles bookmark action: calculates target bookmarked state and calls the repository to make
     * changes in the stored data. Data will be updated through [articles] LiveData.
     */
    fun onBookmarkPressed(article: ArticleItemModel) {
        articleRepository.changeArticleBookmarkedState(article.id, !article.isBookmarked)
    }

    /**
     * Handles delete action: calls the repository to make changes in the stored data.
     */
    fun onDeletePressed(article: ArticleItemModel) {
        articleRepository.deleteArticle(article.id)
    }

    /**
     * Transforms the data-layer model into UI-layer model.
     */
    private fun ArticleWithAuthor.transformToItemModel() = ArticleItemModel(
        article.id,
        article.text.substringBefore(' '),
        getArticleText(article),
        author?.name.orEmpty(),
        article.isBookmarked,
    )

    /**
     * Formats the text for the displaying.
     * This is just for demo purposes, same effect could be achieved by using ellipsize property
     * of the TextView.
     */
    private fun getArticleText(article: Article): String {
        val withoutTitle = article.text.substringAfter(' ')
        if (withoutTitle.length <= ARTICLE_TEXT_DISPLAY_LENGTH) {
            return withoutTitle
        }
        return withoutTitle.substring(
            0..ARTICLE_TEXT_DISPLAY_LENGTH - ARTICLE_TOO_LONG_DOTS.length
        ) + ARTICLE_TOO_LONG_DOTS
    }
}
