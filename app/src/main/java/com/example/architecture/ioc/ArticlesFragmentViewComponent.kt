package com.example.architecture.ioc

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.architecture.R
import com.example.architecture.ui.view.ArticlesFragment
import com.example.architecture.ui.view.ArticlesListViewController

/**
 * Container for all Fragment-View-scoped classes. This container is created after fragment's
 * onCreateView and released after onDestroyView.
 *
 * This is done by storing reference to this container in [ArticlesFragment.fragmentViewComponent].
 * We can use dependencies from [fragmentComponent] since it lives longer than fragment's views.
 *
 * Classes in this container may reference activity, fragment and views.
 */
class ArticlesFragmentViewComponent(
    fragmentComponent: ArticlesFragmentComponent,
    root: View,
    lifecycleOwner: LifecycleOwner,
) {
    /**
     * If there were more views, logic for finding and accessing them could be moved to separate
     * class e.g. ArticlesFragmentViewHolder. Alternatively, we could use Data Binding
     * https://developer.android.com/topic/libraries/data-binding
     */
    private val recycler: RecyclerView = root.findViewById(R.id.articles_recycler)

    /**
     * Public getter because we want to access this class from the Fragment.
     */
    val articlesListViewController = ArticlesListViewController(
        fragmentComponent.fragment.requireActivity(),
        recycler,
        fragmentComponent.adapter,
        lifecycleOwner,
        fragmentComponent.viewModel,
    )
}
