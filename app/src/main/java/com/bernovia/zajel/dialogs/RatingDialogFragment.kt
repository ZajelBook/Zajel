package com.bernovia.zajel.dialogs

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bernovia.zajel.R

/**
 * A simple [Fragment] subclass.
 */
class RatingDialogFragment(private val context1: Context) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_rating_dialog, container, false)
        val ratingBar = rootView.findViewById<RatingBar>(R.id.add_review_ratingbar)
        ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar1: RatingBar, rating: Float, fromUser: Boolean ->

                if (ratingBar1.rating == 4f || ratingBar1.rating == 5f) {
                    askRatingAppStore()
                    dismiss()
                } else {
                    askRatingFeedback()
                    dismiss()
                }
            }
        isCancelable = true
        return rootView
    }


    override fun onResume() {
        if (dialog != null && dialog?.window != null) {
            val params: WindowManager.LayoutParams = dialog!!.window!!.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog!!.window?.attributes = params
        }
        super.onResume()
    }

    private fun askRatingFeedback() {
        if (activity != null) {
            val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            alertDialogBuilder.setTitle(resources.getString(R.string.leave_feedback))
            alertDialogBuilder.setMessage(resources.getString(R.string.leave_feedback_message))
            alertDialogBuilder.setPositiveButton(resources.getString(R.string.yes)) { arg0: DialogInterface?, arg1: Int ->

                val emailIntent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "zajelbooks@gmail.com", null
                    )
                )
                context1.startActivity(Intent.createChooser(emailIntent, "Send email..."))


            }
            alertDialogBuilder.setNegativeButton(resources.getString(R.string.no)) { dialog: DialogInterface?, which: Int -> }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun askRatingAppStore() {
        if (activity != null) {
            if (isAdded) {
                val alertDialogBuilder = AlertDialog.Builder(requireActivity())
                alertDialogBuilder.setTitle(resources.getString(R.string.thanks))
                alertDialogBuilder.setMessage(resources.getString(R.string.rate_message))
                alertDialogBuilder.setPositiveButton(resources.getString(R.string.rate_us)) { arg0: DialogInterface?, arg1: Int ->

                    // go to app store
                    val uri = Uri.parse("market://details?id=" + context1.packageName)
                    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    goToMarket.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    try {
                        context1.startActivity(goToMarket)
                    } catch (e: Exception) {
                        context1.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + context1.packageName)
                            )
                        )
                    }
                }
                alertDialogBuilder.setNegativeButton(resources.getString(R.string.no_thanks)) { dialog: DialogInterface?, which: Int -> }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
    }

}
