package com.bernovia.zajel.helpers

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bernovia.zajel.R

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

        when (animationType) {
            AnimationType.PUSH -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left, R.anim.slide_in_from_left, R.anim.slide_out_from_right)
            AnimationType.FADE -> fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            AnimationType.PUSHFROMRIGHT -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_from_right, R.anim.slide_in_from_right, R.anim.slide_out_from_left)
            AnimationType.SLIDEINUP -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_bottom, R.anim.slide_out_up, R.anim.slide_out_bottom)
        }


        //fragmentTransaction.add(containerID, newFragment, newFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss()
    }


    /**
     * Replaces one fragment with another. The previous fragment is not added to the backstack.
     */
    fun addFragment(
        fragmentManager: FragmentManager, @IdRes containerID: Int, newFragment: Fragment?, animationType: AnimationType?) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        when (animationType) {
            AnimationType.PUSH -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left, R.anim.slide_in_from_left, R.anim.slide_out_from_right)
            AnimationType.SLIDEINUPSLOW -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_up_slow, R.anim.slide_out_bottom, R.anim.slide_out_up, R.anim.slide_out_bottom)
            AnimationType.SLIDEINUP -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_bottom, R.anim.slide_out_up, R.anim.slide_out_bottom)
            AnimationType.PUSHFAST -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right_short_time,
                R.anim.slide_out_from_left_short_time,
                R.anim.slide_in_from_left,
                R.anim.slide_out_from_right_short_time)
        }


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
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left, R.anim.slide_in_from_left, R.anim.slide_out_from_right)
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