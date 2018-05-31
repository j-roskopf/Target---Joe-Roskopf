package com.target.dealbrowserpoc.dealbrowser.deals

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.transition.Fade
import android.support.transition.TransitionInflater
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.dealbrowser.deals.adapter.DealListItemAdapter
import com.target.dealbrowserpoc.dealbrowser.deals.adapter.ViewHolder
import com.target.dealbrowserpoc.dealbrowser.deals.model.network.DealItem
import com.target.dealbrowserpoc.dealbrowser.deals.model.network.DealResponse
import com.target.dealbrowserpoc.dealbrowser.deals.network.DealService
import com.target.dealbrowserpoc.dealbrowser.detail.DetailViewFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.deal_error_layout.*
import kotlinx.android.synthetic.main.detail_list_fragment.*
import javax.inject.Inject

private const val AMT_COLUMNS = 2

class DealListFragment : Fragment() {

    @Inject
    lateinit var dealService: DealService

    /**
     * Our ViewModel
     */
    private lateinit var viewModel: DealViewModel

    /**
     * Current display of our [RecyclerView]
     */
    private var currentDisplay = DealDisplay.LIST

    /**
     * UI elements to be shown while we are loading deals.
     */
    private val progressUI by lazy { arrayOf(dealsProgressLayout) }

    /**
     * UI elements to be shown when there is an error
     */
    private val errorUI by lazy { arrayOf(dealsErrorLayout) }

    /**
     * UI elements to be shown when there are no results
     */
    private val noResultsUI by lazy { arrayOf(dealsNoResultsLayout) }

    /**
     * UI elements to be shown when results have returned with no error
     */
    private val resultsUI by lazy { arrayOf(dealsRecyclerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_list_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_deal_list_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_grid_button -> {
                toggleDisplay()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(activity!!, FavoriteViewModelFactory(dealService)).get(DealViewModel::class.java)

        fetchDeals()

        setupRetryButton()
    }

    /**
     * Make API call to fetch the deals.
     *
     * This method is also responsible for displaying the correct layout based on the response of the API call
     */
    private fun fetchDeals() {
        //show loading
        showProgress()

        viewModel.getDealResponse().observe(activity as LifecycleOwner, Observer<DealResponse> { response ->
            if (response != null) {
                if (response.data != null) {
                    if (response.data!!.isNotEmpty()) {
                        showResults()
                        displayResults(response.data!!)
                    } else {
                        showNoResults()
                    }
                } else {
                    showError()
                }
            } else {
                showError()
            }
        })
    }

    /**
     * Setup onclick for our Retry Button
     */
    private fun setupRetryButton() {
        dealsErrorRetryButton.setOnClickListener {
            fetchDeals()
        }
    }

    /**
     * Display results of [DealItem] inside the RecyclerView
     */
    private fun displayResults(data: List<DealItem>) {
        val listAdapter = DealListItemAdapter(currentDisplay, context!!, data, object : OnFragmentInteractionListener {
            override fun onFragmentInteraction(item: DealItem, holder: ViewHolder, position: Int) {

                val detailViewFragment = DetailViewFragment.newInstance(item, position)

                exitTransition = Fade()

                //There seems to be a bug in the support lib causing a flicker to happen when the fragment manager replaces a fragment
                //https://issuetracker.google.com/issues/74051124
                activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, detailViewFragment)
                        .addSharedElement(holder.productImage, "${DealListFragment.imageTransitionName}$position")
                        .addToBackStack(null)
                        .commit()

            }

        })
        when (currentDisplay) {
            DealDisplay.LIST -> dealsRecyclerView.layoutManager = LinearLayoutManager(context)
            DealDisplay.GRID -> dealsRecyclerView.layoutManager = GridLayoutManager(context, AMT_COLUMNS)
        }


        dealsRecyclerView.adapter = listAdapter
    }

    /**
     * Toggles the deal display between grid / list.
     *
     * This method is also responsible for redisplaying the results
     */
    private fun toggleDisplay() {
        currentDisplay = when (currentDisplay) {
            DealDisplay.LIST -> DealDisplay.GRID
            DealDisplay.GRID -> DealDisplay.LIST
        }

        viewModel.getDealResponse().value?.data?.let {
            displayResults(it)
        }
    }

    /**
     * Method to be called after results return successfully.
     *
     * Hides all irrelevant UI and shows relevant UI
     */
    private fun showResults() {
        resultsUI.forEach { it.visibility = View.VISIBLE }
        hideNoResults()
        hideError()
        hideProgress()
    }

    /**
     * Method to be called to hide any UI pertaining to when the results did not return successfully
     */
    private fun hideResults() = resultsUI.forEach { it.visibility = View.GONE }

    /**
     * Method to be called after no results return successfully.
     *
     * Hides all irrelevant UI and shows relevant UI
     */
    private fun showNoResults() {
        noResultsUI.forEach { it.visibility = View.VISIBLE }
        hideResults()
        hideError()
        hideProgress()
    }

    /**
     * Method to be called to hide any UI pertaining to when the results returned successfully
     * but contained no results
     */
    private fun hideNoResults() = noResultsUI.forEach { it.visibility = View.GONE }

    /**
     * Method to be called after our API call returns an error
     *
     * Hides all irrelevant UI and shows relevant UI
     */
    private fun showError() {
        errorUI.forEach { it.visibility = View.VISIBLE }
        hideProgress()
        hideResults()
        hideNoResults()
    }

    /**
     * Method to be called to hide any UI pertaining to when an error is displayed
     */
    private fun hideError() = errorUI.forEach { it.visibility = View.GONE }

    /**
     * Method to be called while we are loading our UI
     *
     * Hides all irrelevant UI and shows relevant UI
     */
    private fun showProgress() {
        progressUI.forEach { it.visibility = View.VISIBLE }
        hideResults()
        hideNoResults()
        hideError()
    }

    /**
     * Method to be called to hide any UI pertaining to the progress state
     */
    private fun hideProgress() = progressUI.forEach { it.visibility = View.GONE }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment [DealListFragment].
     */
    companion object {

        const val imageTransitionName = "dealItemImage"

        @JvmStatic
        fun newInstance(): DealListFragment {
            return DealListFragment()
        }
    }

    /**
     * Interface callback for when an [DealListItemAdapter] [DealItem] is clicked
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(item: DealItem, holder: ViewHolder, position: Int)
    }

    /**
     * Enum class describing the current display of our deals. Whether they are currently in list mode or grid more
     */
    enum class DealDisplay {
        LIST,
        GRID
    }


}