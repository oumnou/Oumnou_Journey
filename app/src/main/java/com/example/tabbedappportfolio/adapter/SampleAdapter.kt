package com.example.tabbedappportfolio.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.tabbedappportfolio.fragment.FragmentFour
import com.example.tabbedappportfolio.fragment.FragmentOne
import com.example.tabbedappportfolio.fragment.FragmentThree
import com.example.tabbedappportfolio.fragment.FragmentTwo
class SampleAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments: MutableList<Fragment> = mutableListOf()


        init {
        fragments.add(FragmentOne.newInstance())
        fragments.add(FragmentTwo.newInstance())
        fragments.add(FragmentThree.newInstance())
        fragments.add(FragmentFour.newInstance())
    }

//    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment = when (position) {
            0 -> FragmentOne()
            1 -> FragmentTwo()
            2 -> FragmentThree()
            3 -> FragmentFour()
            else -> throw IllegalArgumentException("Invalid position")
        }

        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "My Life"
        1 -> "My study"
        2 -> "Chat-boot"
        3 -> "To Do"
        else -> ""
    }

    override fun getCount(): Int = fragments.size

}
