package com.example.architecture.ioc

import androidx.fragment.app.Fragment
import com.example.architecture.ui.stateholders.ArticlesViewModel
import com.example.architecture.ui.view.ArticleItemDiffCalculator
import com.example.architecture.ui.view.ArticlesFragment
import com.example.architecture.ui.view.ArticlesListAdapter

/**
 * Container for all fragment-scoped classes. This container is created after fragment's
 * onCreate and released when the fragment is destroyed.
 *
 * This is done by storing reference to this container in [ArticlesFragment.fragmentComponent].
 * We can use dependencies from [applicationComponent] since it lives longer than fragment.
 *
 * Classes in this container may reference activity or fragment, but hey can't reference views,
 * as views can be recreated after onDestroyView.
 */
class ArticlesFragmentComponent(
    val applicationComponent: ApplicationComponent,
    val fragment: Fragment,
    val viewModel: ArticlesViewModel,
) {
    val adapter = ArticlesListAdapter(viewModel, ArticleItemDiffCalculator())
}
