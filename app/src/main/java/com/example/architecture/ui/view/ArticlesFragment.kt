package com.example.architecture.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.architecture.App
import com.example.architecture.R
import com.example.architecture.ioc.ArticlesFragmentComponent
import com.example.architecture.ioc.ArticlesFragmentViewComponent
import com.example.architecture.ui.stateholders.ArticlesViewModel

/**
 * Main fragment of the application.
 * Contains no logic, only some IoC-container management.
 */
class ArticlesFragment : Fragment() {

    private val applicationComponent
        get() = App.get(requireContext()).applicationComponent
    private lateinit var fragmentComponent: ArticlesFragmentComponent
    private var fragmentViewComponent: ArticlesFragmentViewComponent? = null

    /**
     * Note that we don't create the ViewModel directly, by viewModels will create it through
     * viewModelFactory if there is no already created [ArticlesViewModel] for this fragment.
     */
    private val viewModel: ArticlesViewModel by viewModels { applicationComponent.viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent = ArticlesFragmentComponent(
            applicationComponent,
            fragment = this,
            viewModel = viewModel,
        )
    }

    /**
     * Note that view-creation code could have been offloaded to some other class
     * if it wasn't literally single line of code.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        fragmentViewComponent = ArticlesFragmentViewComponent(
            fragmentComponent,
            root = view,
            lifecycleOwner = viewLifecycleOwner,
        ).apply {
            articlesViewController.setUpArticlesList()
        }
        return view
    }

    /**
     * [ArticlesFragmentViewComponent] can reference views, so it should be nulled to avoid memory
     * leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
    }
}
