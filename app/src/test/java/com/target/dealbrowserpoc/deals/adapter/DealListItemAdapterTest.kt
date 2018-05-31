package com.target.dealbrowserpoc.deals.adapter

import android.content.Context
import com.target.dealbrowserpoc.dealbrowser.deals.DealListFragment
import com.target.dealbrowserpoc.dealbrowser.deals.adapter.DealListItemAdapter
import com.target.dealbrowserpoc.dealbrowser.deals.adapter.ViewHolder
import com.target.dealbrowserpoc.dealbrowser.deals.model.network.DealItem
import junit.framework.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DealListItemAdapterTest {

    private lateinit var dealListItemAdapter: DealListItemAdapter

    @Before
    fun setup() {
        val onFragmentInteractionListener = object: DealListFragment.OnFragmentInteractionListener {
            override fun onFragmentInteraction(item: DealItem, holder: ViewHolder, position: Int) {
            }
        }

        val context = mock(Context::class.java)
        dealListItemAdapter = DealListItemAdapter(
                DealListFragment.DealDisplay.LIST,
                context, arrayListOf(
                DealItem(
                        id = "id",
                        aisle = "aisle",
                        description = "description",
                        guid = "guid",
                        image = "image",
                        index = 0,
                        price = "price",
                        salePrice = "salePrice",
                        title = "title"
                )
        ), onFragmentInteractionListener)
    }

    @Test
    fun getRandomLetterTest() {
        //Get 1000 random letters. If one of them is not in our alphabet range,
        //fail the test
        for(i in 1..1000) {
            val letter = dealListItemAdapter.getRandomLetter()
            if (letter !in 'a'..'z' && letter !in 'A'..'Z') {
                fail("Letter is not in alphabet")
            }
        }
    }

    @Test
    fun getRandomNumberTest() {
        //Get 1000 random numbers. If one of them is not in our range,
        //fail the test
        for(i in 1..1000) {
            val number = dealListItemAdapter.getRandomNumber()
            if (number !in 1..100) {
                fail("Number is not in range")
            }
        }
    }
}