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
    private val recycler: RecyclerView = root.findViewById(R.id.articles_recycler)
    val articlesListViewController = ArticlesListViewController(
        fragmentComponent.fragment.requireActivity(),
        recycler,
        fragmentComponent.adapter,
        lifecycleOwner,
        fragmentComponent.viewModel,
    )
}
