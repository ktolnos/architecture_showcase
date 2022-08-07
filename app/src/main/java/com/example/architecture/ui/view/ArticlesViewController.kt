package com.example.architecture.ui.view

import android.app.Activity
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.architecture.R
import com.example.architecture.ui.stateholders.ArticlesViewModel

/**
 * Contains logic for articles screen views configuration.
 * This is an example of how we can move any logic outside of Fragment.
 *
 * Fragment-View-scoped: will be created after onCreateView and destroyed after onDestroyView.
 * Can reference views.
 */
class ArticlesViewController(
    private val activity: Activity,
    rootView: View,
    private val adapter: ArticlesListAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: ArticlesViewModel,
) {
    /**
     * If there were more views, logic for finding and accessing them could be moved to separate
     * class e.g. ArticlesFragmentViewHolder. Alternatively, we could use Data Binding
     * https://developer.android.com/topic/libraries/data-binding
     */
    private val recyclerView: RecyclerView = rootView.findViewById(R.id.articles_recycler)
    private val swipeRefreshLayout: SwipeRefreshLayout =
        rootView.findViewById(R.id.articles_swipe_refresh)

    /**
     * Configure views of the articles screen.
     * This method could have been called directly from the constructor of this class.
     */
    fun setUpViews() {
        setUpArticlesList()
        setUpSwipeToRefresh()
    }

    /**
     * Connects [recyclerView] with [adapter] and [adapter] with the data from [viewModel].
     */
    fun setUpArticlesList() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        viewModel.articles.observe(lifecycleOwner) { newArticles ->
            adapter.submitList(newArticles)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    fun setUpSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.updateArticles()
        }
    }
}
