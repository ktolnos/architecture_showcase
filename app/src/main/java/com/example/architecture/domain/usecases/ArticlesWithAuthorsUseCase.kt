package com.example.architecture.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.architecture.data.model.Author
import com.example.architecture.data.repository.ArticleRepository
import com.example.architecture.data.repository.AuthorRepository
import com.example.architecture.domain.model.ArticleWithAuthor
import com.example.architecture.ui.stateholders.ArticlesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Contains logic about combing articles with their authors.
 *
 * This is domain-layer class, it is not responsible for storing any information.
 *
 * It is essentially stateless: despite the fact that it contains mutable LiveData,
 * LiveData's value is calculated and would be the same for the new instance of an object.
 *
 * NOTE: This class is made to demonstrate how UseCase could look like. I would suggest to move this
 * logic into data-layer and make this class Application-scoped. LiveData here can work like cache,
 * there is no reason to recalculate it's value for each ViewModel.
 *
 * Unscoped: is created and destroyed as needed. In fact, has the same lifecycle as it's only user,
 * [ArticlesViewModel].
 */
class ArticlesWithAuthorsUseCase(
    private val articleRepository: ArticleRepository,
    private val authorsRepository: AuthorRepository,
) {
    private val _articlesWithAuthors = MediatorLiveData<List<ArticleWithAuthor>>()

    /**
     * New value will be set each time articles or authors change.
     */
    val articlesWithAuthors: LiveData<List<ArticleWithAuthor>> = _articlesWithAuthors

    /**
     * Our custom coroutine scope for making calculations in background thread.
     * We will cancel it in [cancelAllOperations] to stop ongoing calculations when
     * their results are no longer required.
     */
    private val combineCoroutineScope = CoroutineScope(Job() + Dispatchers.Default)

    init {
        /**
         * Trigger [updateArticlesWithAuthorsLiveData] right now and each time
         * [articleRepository.articles] change.
         */
        _articlesWithAuthors.addSource(articleRepository.articles) {
            updateArticlesWithAuthorsLiveData()
        }
        /**
         * Trigger [updateArticlesWithAuthorsLiveData] right now and each time
         * [articleRepository.authors] change.
         */
        _articlesWithAuthors.addSource(authorsRepository.authors) {
            updateArticlesWithAuthorsLiveData()
        }
    }

    /**
     * Starts updating articles and authors in parallel.
     */
    suspend fun updateArticlesAndAuthors() {
        withContext(Dispatchers.Main) {
            listOf(launch {
                articleRepository.updateArticles()
            }, launch {
                authorsRepository.updateAuthors()
            }).joinAll()
        }
    }

    /**
     * Cancels all ongoing asynchronous operations.
     *
     * We launch background work for combining articles with authors, we should cancel it
     * if nobody uses the result.
     */
    fun cancelAllOperations() {
        combineCoroutineScope.cancel()
    }

    /**
     * Combines values of articles with corresponding authors.
     * Uses background thread because lists can be huge.
     */
    private fun updateArticlesWithAuthorsLiveData() {
        combineCoroutineScope.launch {
            val combined = combineArticlesWithAuthors()
            _articlesWithAuthors.postValue(combined) // update MediatorLiveData's value.
            // we use postValue because combineCoroutineScope uses Dispatchers.Default, so this
            // peace of code is executed on background thread.
        }
    }

    /**
     * Separate method for doing the actual calculations.
     * Note that moving this code into separate method reduced nesting and improved readability.
     */
    private fun combineArticlesWithAuthors(): List<ArticleWithAuthor> {
        val authors = authorsRepository.authors.value.orEmpty()
        val authorIdToAuthor = HashMap<Int, Author>() // use HashMap for linear O(N) complexity.
        authors.forEach { author ->
            authorIdToAuthor[author.id] = author
        }
        if (!combineCoroutineScope.isActive) return emptyList() // by default sequential
        // computations can't be cancelled, we need to manually check if current Job is active.

        val articles = articleRepository.articles.value.orEmpty()
        return articles.map { article ->
            ArticleWithAuthor(article, authorIdToAuthor[article.authorId])
        }
    }
}
