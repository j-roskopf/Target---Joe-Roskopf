package com.target.dealbrowserpoc.dealbrowser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.target.dealbrowserpoc.dealbrowser.deals.DealListFragment
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, DealListFragment.newInstance())
                    .commit()
        }
    }
}
