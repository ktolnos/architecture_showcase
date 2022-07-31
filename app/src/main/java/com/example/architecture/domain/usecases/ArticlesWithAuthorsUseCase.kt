package com.example.architecture.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.architecture.data.model.Author
import com.example.architecture.data.repository.ArticleRepository
import com.example.architecture.data.repository.AuthorRepository
import com.example.architecture.domain.model.ArticleWithAuthor
import com.example.architecture.ui.stateholders.ArticlesViewModel
import kotlinx.coroutines.Dispatchers
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
     * Combines values of articles with corresponding authors.
     */
    private fun updateArticlesWithAuthorsLiveData() {
        val authors = authorsRepository.authors.value.orEmpty()
        val authorIdToAuthor = HashMap<Int, Author>() // use HashMap for linear O(N) complexity.
        for (author in authors) {
            authorIdToAuthor[author.id] = author
        }

        val articles = articleRepository.articles.value.orEmpty()

        _articlesWithAuthors.value = articles.map { article -> // update MediatorLiveData's value.
            ArticleWithAuthor(article, authorIdToAuthor[article.authorId])
        }
    }
}
