package com.bernovia.zajel.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.DatePicker
import android.widget.EditText
import com.bernovia.zajel.helpers.ZajelUtil.hideKeyboard
import org.ocpsoft.prettytime.PrettyTime
import java.lang.reflect.InvocationTargetException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    val myCalendar = Calendar.getInstance()

    fun dateListener(editText: EditText): OnDateSetListener {

        return OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateDateLabel(myCalendar, editText)
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun timeAgo(timeStamp: String?): String? {
        val ago: String
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("GMT+00:00")
        ago = try {
            val time = sdf.parse(timeStamp).time
            val prettyTime = PrettyTime(Locale.getDefault())
            prettyTime.format(Date(time))
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
        return ago
    }

    fun showDatePicker(
        context: Activity?,
        myCalendar: Calendar,
        date: OnDateSetListener?
    ) {
        hideKeyboard(context)
        val dpd = DatePickerDialog(
            context!!, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
        val today = Date() // Get today's date
        myCalendar.time = today // Set calendar to today's date
        myCalendar.add(Calendar.YEAR, -18) // Subtract 18 years
        myCalendar[Calendar.MONTH] = 11 // Set month to 12 (december)
        myCalendar[Calendar.DAY_OF_MONTH] = 31 // Set day to 31 (last day of december)
        val maxDate = myCalendar.time.time // Twice!
        dpd.datePicker.maxDate = maxDate //Set max date
        val myCalendar1 = Calendar.getInstance()
        val today1 = Date() // Get today's date
        myCalendar1.time = today1 // Set calendar to today's date
        myCalendar1.add(Calendar.YEAR, -70) // Subtract 70 years
        myCalendar1[Calendar.MONTH] = 11 // Set month to 12 (december)
        myCalendar1[Calendar.DAY_OF_MONTH] = 31 // Set day to 31 (last day of december)
        val mindate = myCalendar1.time.time // Twice!
        dpd.datePicker.minDate = mindate //Set min date
        openYearView(dpd.datePicker)
        dpd.setTitle("Select Date")
        dpd.show()
    }

    fun openYearView(datePicker: DatePicker) {
        try {
            val mDelegateField =
                datePicker.javaClass.getDeclaredField("mDelegate")
            mDelegateField.isAccessible = true
            val delegate = mDelegateField[datePicker]
            val setCurrentViewMethod =
                delegate.javaClass.getDeclaredMethod(
                    "setCurrentView",
                    Int::class.javaPrimitiveType
                )
            setCurrentViewMethod.isAccessible = true
            setCurrentViewMethod.invoke(delegate, 1)
        } catch (ignore: NoSuchFieldException) {
        } catch (ignore: IllegalAccessException) {
        } catch (ignore: NoSuchMethodException) {
        } catch (ignore: InvocationTargetException) {
        }
    }


    fun updateDateLabel(
        myCalendar: Calendar,
        editText: EditText
    ): SimpleDateFormat? {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf =
            SimpleDateFormat(myFormat, Locale.US)
        editText.setText(sdf.format(myCalendar.time))
        return sdf
    }

}