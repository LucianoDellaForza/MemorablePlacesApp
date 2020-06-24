package rs.raf.luka_krivacevic_rm_96_15.presentation.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_favourite_locations.*
import rs.raf.luka_krivacevic_rm_96_15.R
import rs.raf.luka_krivacevic_rm_96_15.presentation.view.viewpager.TabPagerAdapter

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