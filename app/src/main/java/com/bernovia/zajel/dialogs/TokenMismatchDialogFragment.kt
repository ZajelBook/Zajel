package com.bernovia.zajel.dialogs


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bernovia.zajel.R
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.databinding.FragmentTokenMissmatchDialogBinding


/**
 * A simple [Fragment] subclass.
 */
class TokenMismatchDialogFragment : DialogFragment() {


    @SuppressLint("RestrictedApi", "InflateParams")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        //Set the custom view

        val binding = DataBindingUtil.inflate<FragmentTokenMissmatchDialogBinding>(LayoutInflater.from(context), R.layout.fragment_token_missmatch_dialog, null, false)


        if (getDialog() != null && dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        dialog.setContentView(binding.root)



        binding.okayButton.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)

            if (activity != null)
                activity?.finish()
        }


    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params
    }

}
