package com.bernovia.zajel.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentSingleChoiceMenuBinding
import com.bernovia.zajel.helpers.BroadcastReceiversUtil
import com.bernovia.zajel.splashScreen.ui.MetaDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class SingleChoiceMenuFragment : BottomSheetDialogFragment(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: FragmentSingleChoiceMenuBinding
    private val metaDataViewModel: MetaDataViewModel by viewModel()

    private var title: String = ""
    private var selectedValue: String = ""

    private var data: ArrayList<String>? = null
    private val checkBoxArrayList = ArrayList<RadioButton>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_choice_menu, container, false)


        if (arguments != null) {
            title = arguments?.getString("title")!!
            selectedValue = arguments?.getString("selected_value")!!

            binding.titleTextView.text = title
            data = ArrayList()


            metaDataViewModel.getMetaData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (title) {
                    resources.getString(R.string.language) -> {
                        it.languages.let { it1 -> data!!.addAll(it1) }
                    }

                    resources.getString(R.string.genre) -> {
                        for (items in it.genres) {
                            data!!.add(items.name)
                        }
                    }
                }


                if (data != null) {
                    CheckboxdataIntoString(data!!)
                }


                for (current in checkBoxArrayList) {
                    current.setOnCheckedChangeListener(this)
                }
                changeTheCheckboxStatus(selectedValue)
            })

        }




        return binding.root
    }


    @SuppressLint("InflateParams") private fun CheckboxdataIntoString(data: java.util.ArrayList<String>) {
        var i = 0
        while (i < data.size) {
            var checkBox: RadioButton
            if (context != null) {
                checkBox = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.filters_radiobutton, null) as RadioButton
                checkBox.text = data[i]
                val rProfile = requireContext().resources
                val pxProfile = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, rProfile.displayMetrics).toInt()
                checkBoxArrayList.add(checkBox)
                val checkParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                checkParams.setMargins(pxProfile + 3, 10, pxProfile, 10)
                binding.radioGroup.addView(checkBox, checkParams)
            }
            i++
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView?.isPressed!!) {
            dismiss()
            BroadcastReceiversUtil.bottomSheetSelectedBroadcastIntent(title, buttonView.text.toString())
        }
    }


    private fun changeTheCheckboxStatus(filtersArrayList: String) {
        for (i in data!!.indices) {
            if (data!![i] == filtersArrayList.replace('"', ' ').trim { it <= ' ' }) {
                checkBoxArrayList[i].isChecked = true
            }
        }
    }


}
