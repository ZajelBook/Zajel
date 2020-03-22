package com.bernovia.zajel.dialogs

import androidx.fragment.app.FragmentManager
import com.bernovia.zajel.dialogs.ChoosePhotoDialogFragment.ChoosePhotoClickListener

object DialogUtil {
    const val SHOW_CHOOSE_PHOTO_DIALOG_TAG = "show_choose_photo_dialog"

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

}