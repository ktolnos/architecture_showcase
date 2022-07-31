package com.example.architecture.data.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.architecture.data.datasource.HardCodedDataSource
import com.example.architecture.data.model.Author
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @see ArticleRepository for more detailed documentation.
 *
 * Application-scoped: lives as long as an application itself. Is not recreated on configuration
 * changes or on navigation.
 */
class AuthorRepository(
    private val dataSource: HardCodedDataSource,
) {
    private val _authors = MutableLiveData<List<Author>>(emptyList())
    val authors: LiveData<List<Author>> = _authors

    /**
     * Loads author list. Result can be observed through [authors].
     */
    @MainThread
    suspend fun updateAuthors() {
        val loadedList = withContext(Dispatchers.IO) { dataSource.loadAuthors() }
        _authors.value = loadedList
    }
}
