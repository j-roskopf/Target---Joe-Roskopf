package com.target.dealbrowserpoc.dealbrowser.detail

import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.support.transition.Fade
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.dealbrowser.deals.DealListFragment
import com.target.dealbrowserpoc.dealbrowser.deals.model.network.DealItem
import kotlinx.android.synthetic.main.fragment_deatil_view.*


private const val ARG_DEAL_ITEM = "deal_item"
private const val ARG_DEAL_ITEM_TRANSITION = "deal_item_position"

class DetailViewFragment : Fragment() {
    /**
     * The [DealItem] to display
     */
    private var dealItem: DealItem? = null

    /**
     * The position of the item in the adapter we clicked on. This is useful to store so we can manage transitions
     */
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealItem = it.getParcelable(ARG_DEAL_ITEM)
            position = it.getInt(ARG_DEAL_ITEM_TRANSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deatil_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setTransitionName(view.findViewById(R.id.detailViewImage), "${DealListFragment.imageTransitionName}$position")
        displayDealItem()
    }

    /**
     * Displays the deal item!
     */
    private fun displayDealItem() {
        dealItem?.let {
            displayDealImage(it)
            if(it.salePrice != null) {
                displayDealSalePrice(it)
                displayDealRegularPrice(it, true)
            } else {
                //no sale price. stylize it like there is no sale
                displayDealRegularPrice(it, false)
            }

            displayDealBody(it)
            displayDealTitle(it)
        }
    }

    /**
     * Sets the text of the title of the deal item in the view
     *
     * @param dealItem- the [DealItem] to display
     */
    private fun displayDealTitle(dealItem: DealItem) {
        detailViewTitle.text = dealItem.title
    }

    /**
     * Sets the body of the deal item in the view
     *
     * @param dealItem - the [DealItem] to display
     */
    private fun displayDealBody(dealItem: DealItem) {
        detailViewBody.text = dealItem.description
    }

    /**
     * Display regular price of the [DealItem]
     *
     * @param dealItem - the [DealItem] to display
     * @param stylized - if we do not have a sale price, do not stylize the regular price with a strike through
     */
    private fun displayDealRegularPrice(dealItem: DealItem, stylized: Boolean) {
        if(stylized) {
            detailViewRegularPrice.paintFlags = detailViewRegularPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            detailViewRegularPrice.text = String.format(getString(R.string.regular_short), dealItem.price)
        } else {
            detailViewRegularPrice.text = dealItem.price.toString()
        }
    }

    /**
     * Displays the sale price of the [DealItem]
     *
     * @param - the [DealItem] to display
     */
    private fun displayDealSalePrice(dealItem: DealItem) {
        detailViewSalePrice.text = dealItem.salePrice
    }

    /**
     * Display the image of the [DealItem]
     *
     * @param - the [DealItem] to display
     */
    private fun displayDealImage(dealItem: DealItem) {
        Picasso.get().load(dealItem.image)
                .placeholder(R.drawable.deal_placeholder)
                .error(R.drawable.deal_error).into(detailViewImage)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param dealItem - the [DealItem] to display in the [DetailViewFragment]
         * @return A new instance of fragment DeatilViewFragment.
         */
        @JvmStatic
        fun newInstance(dealItem: DealItem, position: Int): DetailViewFragment {
            return DetailViewFragment().apply {

                //Add shared elements transitions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sharedElementEnterTransition = DetailsTransition()
                    sharedElementReturnTransition = DetailsTransition()
                }

                //add enter transition
                enterTransition = Fade()

                //add the required arguments to start this fragment
                arguments = Bundle().apply {
                    putParcelable(ARG_DEAL_ITEM, dealItem)
                    putInt(ARG_DEAL_ITEM_TRANSITION, position)
                }
            }
        }
    }
}
