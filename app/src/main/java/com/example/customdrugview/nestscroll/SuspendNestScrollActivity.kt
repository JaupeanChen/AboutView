package com.example.customdrugview.nestscroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.customdrugview.R

class SuspendNestScrollActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private val fragments = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suspend_nest_scroll)

        fragments.add(ScrollingFragment.newInstance(0xfffdedbc.toInt()))
        fragments.add(ScrollingFragment.newInstance(0xFFB1F1F5.toInt()))
        fragments.add(ScrollingFragment.newInstance(0x30303333))

        viewPager2 = findViewById(R.id.view_pager)
        viewPager2.offscreenPageLimit = 2
        viewPager2.adapter = object : FragmentStateAdapter(supportFragmentManager, this.lifecycle) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
    }

}