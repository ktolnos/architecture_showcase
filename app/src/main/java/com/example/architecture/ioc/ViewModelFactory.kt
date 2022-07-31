package com.example.architecture.ioc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.architecture.data.repository.ArticleRepository
import com.example.architecture.domain.usecases.ArticlesWithAuthorsUseCase
import com.example.architecture.ui.stateholders.ArticlesViewModel

/**
 * Responsible for creating ViewModels. This class is required if our ViewModels have non-empty
 * constructors, because otherwise the system doesn't know what to pass there.
 *
 * Application-scoped: lives as long as an application itself. Is not recreated on configuration
 * changes or on navigation.
 *
 * We can't use fragment-scoped classes here because ViewModel can survive fragment destruction and
 * recreation.
 *
 * Note that we use [articlesWithAuthorsUseCaseProvider] - a function that creates new
 * [ArticlesWithAuthorsUseCase] for each [ArticlesViewModel]. This is done to demonstrate that
 * use cases can be unscoped i.e. a new one can be created each time it is required.
 */
class ViewModelFactory(
    private val articleRepository: ArticleRepository,
    private val articlesWithAuthorsUseCaseProvider: () -> ArticlesWithAuthorsUseCase,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        ArticlesViewModel::class.java -> ArticlesViewModel(
            articleRepository,
            articlesWithAuthorsUseCaseProvider(),
        )
        else -> throw IllegalArgumentException("${modelClass.simpleName} cannot be provided.")
    } as T
}
