package com.example.architecture.data.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.architecture.data.datasource.HardCodedDataSource
import com.example.architecture.data.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository is the part of data layer.
 * It contains logic about retrieving and updating articles.
 * It can store latest articles value and provide a way to observe it's changes.
 *
 * Application-scoped: lives as long as an application itself. Is not recreated on configuration
 * changes or on navigation.
 */
class ArticleRepository(
    private val dataSource: HardCodedDataSource,
) {
    /**
     * Private variable with mutable LiveData, so it can be modified only within this class.
     */
    private val _articles = MutableLiveData<List<Article>>(emptyList())

    /**
     * Public getter with usual LiveData, this interface does not allow modification.
     * This variable can be observed to receive updates on latest articles list.
     */
    val articles: LiveData<List<Article>> = _articles

    /**
     * Loads articles from data source and sets the new value to _articles LiveData.
     * Other classes can call observe on [articles] to receive update when data is loaded.
     */
    @MainThread // this annotation marks that this method should be called from UI thread.
    suspend fun updateArticles() {
        val loadedList = withContext(Dispatchers.IO) { dataSource.loadArticles() }
        _articles.value = loadedList // setValue can only be called from UI thread.
        // Use postValue if you want to change value from other threads.
    }

    /**
     * This method changes bookmarked state article with id=[articleId] to [bookmarked].
     *
     * As list and it's items are immutable, we have to create the new one and copy the contents,
     * doing necessary modification.
     * Even if everything was modifiable, we would still have to call setValue on _articles
     * LiveData.
     *
     * Note that this method could have done modifications to database
     * or perform network calls if necessary.
     */
    fun changeArticleBookmarkedState(articleId: Int, bookmarked: Boolean) {
        _articles.value = articles.value.orEmpty().map { article ->
            if (article.id == articleId) article.copy(isBookmarked = bookmarked)
            else article
        }
    }

    /**
     * Delete an article with id=[articleId].
     * Same as above, list is unmodifieable, so we have to create a new one.
     */
    fun deleteArticle(articleId: Int) {
        _articles.value = articles.value.orEmpty().filter { it.id != articleId }
    }
}
