package com.target.dealbrowserpoc.dealbrowser.deals.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.dealbrowser.deals.DealListFragment
import com.target.dealbrowserpoc.dealbrowser.deals.model.network.DealItem
import java.util.*

class DealListItemAdapter(private val currentDisplay: DealListFragment.DealDisplay, private val context: Context, private val dealItems: List<DealItem>, private val fragmentInteractionListener: DealListFragment.OnFragmentInteractionListener?) : RecyclerView.Adapter<ViewHolder>() {

    /**
     * Used to generated a random letter / number for aisle
     */
    private val random = Random()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(currentDisplay == DealListFragment.DealDisplay.LIST) {
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.deal_list_item, parent, false))
        } else {
            //we are in grid! our list layout doesn't look the best in grid mode. return our grid item layout
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.deal_list_item_grid, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dealItem = dealItems[position]

        //set transition
        ViewCompat.setTransitionName(holder.productImage, "${DealListFragment.imageTransitionName}${holder.adapterPosition}")

        setImage(holder, dealItem)

        setTitle(holder, dealItem)

        setSalePrice(holder, dealItem)

        setOnClickForBaseLayout(holder, position)

        setRandomAisle(holder)

        setShippingClick(holder)
    }

    /**
     * Handles on click for the shipping view
     */
    private fun setShippingClick(holder: ViewHolder) {
        holder.shipping.setOnClickListener {
            //TODO - Handle shipping clicked
            Snackbar.make(it, "Shipping clicked", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Gives a random aisle for a location in the store. Only for demo purposes
     */
    private fun setRandomAisle(holder: ViewHolder) {
        holder.aisle.text = context.getString(R.string.aisle_placeholder, getRandomLetter(), getRandomNumber())


        holder.aisle.setOnClickListener {
            //TODO - Handle Aisle clicked
            Snackbar.make(it, "Aisle clicked", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Handles the onclick for the baselayout for the [DealItem]
     */
    private fun setOnClickForBaseLayout(holder: ViewHolder, position: Int) {
        holder.baseLayout.setOnClickListener {
            fragmentInteractionListener?.onFragmentInteraction(dealItems[position], holder, position)
        }
    }

    /**
     * Sets the sale price for the [DealItem]
     */
    private fun setSalePrice(holder: ViewHolder, dealItem: DealItem) {
        holder.price.text = dealItem.price
    }

    /**
     * Sets the title for the [DealItem]
     */
    private fun setTitle(holder: ViewHolder, dealItem: DealItem) {
        holder.title.text = dealItem.title
    }

    /**
     * Sets the image for the [DealItem]
     */
    private fun setImage(holder: ViewHolder, dealItem: DealItem) {
        Picasso.get().load(dealItem.image)
                .placeholder(R.drawable.deal_placeholder)
                .error(R.drawable.deal_error).into(holder.productImage)
    }

    /**
     * Amount of items in our list
     */
    override fun getItemCount(): Int {
        return dealItems.size
    }

    /**
     * Gets a random upper case letter from [A-Z]
     */
    fun getRandomLetter() = (random.nextInt(26) + 'a'.toInt()).toChar().toUpperCase()

    /**
     * Gets a random number from 1-100
     */
    fun getRandomNumber() = random.nextInt(99) + 1
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var productImage = view.findViewById(R.id.deal_list_item_image_view) as ImageView
    var title = view.findViewById(R.id.deal_list_item_title) as TextView
    var price = view.findViewById(R.id.deal_list_item_price) as TextView
    var baseLayout = view.findViewById(R.id.deal_list_item) as ConstraintLayout
    var aisle = view.findViewById(R.id.deal_list_item_aisle_text) as TextView
    var shipping = view.findViewById(R.id.deal_list_item_shipping_text) as TextView
}