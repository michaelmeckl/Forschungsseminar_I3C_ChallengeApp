package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.Utils
import com.example.challengecovid.adapter.CategoriesAdapter
import com.example.challengecovid.adapter.CategoryClickListener
import com.example.challengecovid.model.ChallengeCategory
import com.example.challengecovid.viewmodels.CategoryViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {

    private lateinit var categoryViewmodel: CategoryViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_categories, container, false)

        // get the datasource (repository)
        val categoryRepository = RepositoryController.getCategoryRepository()

        // init viewmodel with datasource
        categoryViewmodel = getViewModel { CategoryViewModel(categoryRepository) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init the recycler adapter and setup a click listener on the elements
        categoriesAdapter = CategoriesAdapter(object : CategoryClickListener {
            override fun onCategoryClick(itemView: View, category: ChallengeCategory) {
                showCategoryDetails(itemView, category)
            }
        })

        // calculate the number of columns to use for the grid layout
        val numberOfColumns = this.context?.let { Utils.calculateNumberOfColumns(it) } ?: DEFAULT_NUMBER_COLUMNS

        // setup the Grid Layout with the recycler adapter
        recycler_category_list.apply {
            setHasFixedSize(true) //can improve performance if changes in content do not change the layout size of the RecyclerView
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(activity, numberOfColumns)

            /*
            //FIXME: this leaks memory for some reason and shows the overview fragment for a blink at first
            // postpone the transitions to await loading of all list items before the shared element transitions returns
            // (otherwise the transition would only work on Exit but not on Return to this view!)
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // remove the listener instantly to prevent leaks!
                    recycler_category_list.viewTreeObserver.removeOnPreDrawListener(this)
                    // signals that the UI has been laid out and is ready for the transition
                    startPostponedEnterTransition()
                    return true
                }
            })
             */
        }
    }

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    private fun showCategoryDetails(itemView: View, category: ChallengeCategory) {
        val extras = FragmentNavigatorExtras(itemView to itemView.transitionName)
        val action = CategoriesFragmentDirections.actionCategoryToDetail(
            title = category.title,
            description = category.description,
            imageName = category.categoryIcon
        )

        // set an exit transition
        exitTransition = TransitionInflater.from(context).inflateTransition(R.transition.grid_exit_transition)

        // navigate to another fragment on click
        requireActivity().findNavController(R.id.nav_host_fragment).navigate(action, extras)
    }

    private fun setupObservers() {
        categoryViewmodel.allCategories.observe(viewLifecycleOwner, {
            it?.let {
                categoriesAdapter.submitList(it)  // update recyclerItems that have changed
            }
        })
    }


    companion object {
        private const val DEFAULT_NUMBER_COLUMNS = 2
    }
}