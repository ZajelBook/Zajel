package com.bernovia.zajel.helpers

import android.annotation.SuppressLint
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import org.ocpsoft.prettytime.PrettyTime
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    const val DATE_FORMAT= "yyyy-MM-dd'T'HH:mm:ss.SSS"

    @SuppressLint("SimpleDateFormat") fun timeAgo(timeStamp: String?): String? {
        val ago: String
        val sdf = SimpleDateFormat(DATE_FORMAT)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        ago = try {
            val time = sdf.parse(timeStamp).time
            val prettyTime = PrettyTime(Locale.getDefault())
            prettyTime.format(Date(time))
        }
        catch (e: ParseException) {
            ""
        }
        return ago
    }



    fun convertDateToAmPm(timestamp: String?): String? {
        return try {
            val utcFormat: DateFormat = SimpleDateFormat(DATE_FORMAT)
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = utcFormat.parse(timestamp)
            val deviceFormat: DateFormat = SimpleDateFormat("h:mm a")
            deviceFormat.timeZone = TimeZone.getDefault() //Device timezone
            deviceFormat.format(date)
        }
        catch (e: Exception) {
            timestamp
        }
    }


    fun openDatePickerAndUpdateText(editText: EditText, fragmentManager: FragmentManager) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val currentYear = calendar.get(Calendar.YEAR)
        calendar.set(Calendar.YEAR, currentYear - 70)
        val start = calendar.timeInMillis
        calendar.set(Calendar.YEAR, currentYear - 18)
        val end = calendar.timeInMillis
        val datePicker = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(CalendarConstraints.Builder().setStart(start).setEnd(end).setOpenAt(end).build()).setSelection(end).build()
        datePicker.show(fragmentManager, datePicker.toString())
        datePicker.addOnPositiveButtonClickListener {
            editText.setText(getDateFromMilliSeconds(it))

        }
    }


    @SuppressLint("SimpleDateFormat") private fun getDateFromMilliSeconds(milliSeconds: Long): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

}