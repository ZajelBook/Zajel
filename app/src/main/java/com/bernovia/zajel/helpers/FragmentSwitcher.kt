package com.bernovia.zajel.helpers

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentSwitcher {
    private fun FragmentSwitcher() {}

    /**
     * Switches from one fragment to another using a specified animation.
     * The previous fragment is added to the backstack.
     */
    fun switchFragment(
        fragmentManager: FragmentManager, containerID: Int, newFragment: Fragment?, animationType: AnimationType?) {
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(containerID, newFragment!!)
        fragmentTransaction.addToBackStack("onBoarding")

        //fragmentTransaction.add(containerID, newFragment, newFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss()
    }


    /**
     * Replaces one fragment with another. The previous fragment is not added to the backstack.
     */
    fun addFragment(
        fragmentManager: FragmentManager, @IdRes containerID: Int, newFragment: Fragment?, animationType: AnimationType?) {
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(containerID, newFragment!!)
        fragmentTransaction.addToBackStack("add_fragment")
        fragmentTransaction.commit()
    }


    fun replaceFragmentWithNoAnimation(
        fragmentManager: FragmentManager, @IdRes containerID: Int, newFragment: Fragment?) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(containerID, newFragment!!)
        fragmentTransaction.commit()
    }

    fun replaceFragmentWithPopStack(
        fragmentManager: FragmentManager, @IdRes containerID: Int, newFragment: Fragment?) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(containerID, newFragment!!)
        fragmentTransaction.addToBackStack("fragment_with_pop_stack")
        fragmentTransaction.commit()
    }


    /**
     * Contains animation types for fragment switching.
     */
    enum class AnimationType {
        PUSH, PUSHFROMRIGHT, FADE, SLIDEINUP, PUSHFAST, SLIDEINUPSLOW
    }

}