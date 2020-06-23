package rs.raf.memorableplacesapp.presentation.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_favourite_locations.*
import rs.raf.memorableplacesapp.R
import rs.raf.memorableplacesapp.presentation.view.viewpager.TabPagerAdapter

class FavouriteLocationsActivity : AppCompatActivity(R.layout.activity_favourite_locations) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initTabs()
    }

    private fun initTabs() {
        viewPager.adapter = TabPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}