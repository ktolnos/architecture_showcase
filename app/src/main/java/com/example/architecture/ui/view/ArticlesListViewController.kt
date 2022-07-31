package com.example.architecture.ui.view

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.architecture.ui.stateholders.ArticlesViewModel

/**
 * Contains logic for articles RecyclerView and configuration.
 * This is an example of how we can move any logic outside of Fragment.
 *
 * Fragment-View-scoped: will be created after onCreateView and destroyed after onDestroyView.
 * Can reference views.
 */
class ArticlesListViewController(
    private val activity: Activity,
    private val recyclerView: RecyclerView,
    private val adapter: ArticlesListAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: ArticlesViewModel,
) {
    fun setUpArticlesList() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        viewModel.articles.observe(lifecycleOwner) { newArticles ->
            adapter.submitList(newArticles)
        }
    }
}
