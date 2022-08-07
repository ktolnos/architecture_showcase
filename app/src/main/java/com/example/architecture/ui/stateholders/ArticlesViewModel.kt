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
import java.util.Locale

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
        // Start loading the articles and authors as soon as this ViewModel is created.
        updateArticles()
    }

    /**
     * Update articles list.
     * [viewModelScope] will automatically cancel an execution if the ViewModel
     * is destroyed (onClear).
     */
    fun updateArticles() {
        viewModelScope.launch {
            articlesWithAuthorsUseCase.updateArticlesAndAuthors()
        }
    }

    /**
     * Handles bookmark action: calculates target bookmarked state and calls the repository to make
     * changes in the stored data. Data will be updated through [articles] LiveData.
     */
    fun onBookmarkPressed(article: ArticleItemModel) {
        viewModelScope.launch {
            articleRepository.changeArticleBookmarkedState(article.id, !article.isBookmarked)
        }
    }

    /**
     * Handles delete action: calls the repository to make changes in the stored data.
     * Data will be updated through [articles] LiveData.
     */
    fun onDeletePressed(article: ArticleItemModel) {
        viewModelScope.launch {
            articleRepository.deleteArticle(article.id)
        }
    }

    override fun onCleared() { // you can read JavaDoc for this method by pressing Ctrl+Q or Cmd+J
        articlesWithAuthorsUseCase.cancelAllOperations() // nobody will use it after onCleared.
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
     * @return the text to display as article's body.
     *
     * This is just for demo purposes,
     * it would be better idea to store title and text in separate fields.
     */
    private fun getArticleText(article: Article): String = article.text
        .substringAfter(' ')
        .replaceFirstChar {  // capitalize first char
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else
                it.toString()
        }
}
