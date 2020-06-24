package rs.raf.luka_krivacevic_rm_96_15.presentation.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import rs.raf.luka_krivacevic_rm_96_15.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initListeners()
    }


    private fun initListeners() {
        addLocationBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        favouriteLocationsBtn.setOnClickListener {
            val intent = Intent(this, FavouriteLocationsActivity::class.java)
            startActivity(intent)
        }
    }
}