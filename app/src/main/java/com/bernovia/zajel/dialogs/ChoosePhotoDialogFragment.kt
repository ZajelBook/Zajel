package com.bernovia.zajel.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentChoosePhotoDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple [Fragment] subclass.
 */
class ChoosePhotoDialogFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentChoosePhotoDialogBinding
    private lateinit var choosePhotoClickListener: ChoosePhotoClickListener

    fun setChoosePhotoClickListener(choosePhotoClickListener: ChoosePhotoClickListener) {
        this.choosePhotoClickListener = choosePhotoClickListener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_photo_dialog, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chooseFromLibraryLinearLayout.setOnClickListener {dismiss()
            choosePhotoClickListener.chooseFromLibraryOnClick() }
        binding.takePhotoLinearLayout.setOnClickListener {
            dismiss()
            choosePhotoClickListener.takePhotoOnClick() }

    }


    interface ChoosePhotoClickListener {
        fun chooseFromLibraryOnClick()
        fun takePhotoOnClick()
    }

}
