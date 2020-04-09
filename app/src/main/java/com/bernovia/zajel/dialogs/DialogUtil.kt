package com.bernovia.zajel.dialogs

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.bernovia.zajel.dialogs.ChoosePhotoDialogFragment.ChoosePhotoClickListener

object DialogUtil {
    private const val SHOW_CHOOSE_PHOTO_DIALOG_TAG = "show_choose_photo_dialog"
    private const val SHOW_TOKEN_MISMATCH_DIALOG_TAG = "show_token_mismatch_dialog"
    private const val SHOW_SINGLE_CHOICE_MENU = "show_single_choice_dialog"
    private const val SHOW_RATING_FRAGMENT = "show_rating_fragment"


    fun showChoosePhotoDialog(fragmentManager: FragmentManager?, choosePhotoClickListener: ChoosePhotoClickListener) {
        if (fragmentManager == null) return
        try {
            val fragment = ChoosePhotoDialogFragment()
            fragment.setChoosePhotoClickListener(choosePhotoClickListener)
            fragment.show(fragmentManager, SHOW_CHOOSE_PHOTO_DIALOG_TAG)
        }
        catch (ignore: Exception) {
        }
    }

    fun showAskForRating(fragmentManager: FragmentManager?,context :Context) {
        if (fragmentManager == null) return
        try {
            val fragment = RatingDialogFragment(context)
            fragment.show(fragmentManager, SHOW_RATING_FRAGMENT)
        }
        catch (ignore: Exception) {
        }
    }


    fun showTokenMismatch(fragmentManager: FragmentManager?) {
        if (fragmentManager == null) return
        try {
            val fragment = TokenMismatchDialogFragment()
            val args = Bundle()
            fragment.arguments = args
            fragment.show(fragmentManager, SHOW_TOKEN_MISMATCH_DIALOG_TAG)
        }
        catch (ignore: java.lang.Exception) {
        }
    }

    fun showSingleChoiceMenuFragment(fragmentManager: FragmentManager?, title: String, selectedValue: String) {
        if (fragmentManager == null) return
        try {
            val fragment = SingleChoiceMenuFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("selected_value", selectedValue)
            fragment.arguments = args
            fragment.show(fragmentManager, SHOW_SINGLE_CHOICE_MENU)
        }
        catch (ignore: java.lang.Exception) {
        }
    }


}