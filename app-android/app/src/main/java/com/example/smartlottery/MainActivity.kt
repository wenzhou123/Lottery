package com.example.smartlottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smartlottery.ui.discovery.DiscoveryFragment
import com.example.smartlottery.ui.draw.DrawFragment
import com.example.smartlottery.ui.mine.MineFragment
import com.example.smartlottery.ui.trend.TrendFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val drawFragment = DrawFragment()
    private val trendFragment = TrendFragment()
    private val discoveryFragment = DiscoveryFragment()
    private val mineFragment = MineFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_draw -> {
                    replaceFragment(drawFragment)
                    true
                }
                R.id.navigation_trend -> {
                    replaceFragment(trendFragment)
                    true
                }
                R.id.navigation_discovery -> {
                    replaceFragment(discoveryFragment)
                    true
                }
                R.id.navigation_mine -> {
                    replaceFragment(mineFragment)
                    true
                }
                else -> false
            }
        }

        // Set the default fragment
        replaceFragment(drawFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}
