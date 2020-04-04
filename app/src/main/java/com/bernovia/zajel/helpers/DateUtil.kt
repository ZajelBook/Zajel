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

    @SuppressLint("SimpleDateFormat") fun timeAgo(timeStamp: String?): String? {
        val ago: String
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm ZZZZZ")
        sdf.timeZone = TimeZone.getDefault()
        ago = try {
            val time = sdf.parse(timeStamp).time
            val prettyTime = PrettyTime(Locale.getDefault())
            prettyTime.format(Date(time))
        }
        catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
        return ago
    }

    fun openDatePickerAndUpdateText(editText: EditText, fragmentManager: FragmentManager) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val calendar1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        val currentYear = calendar.get(Calendar.YEAR)
        calendar.set(Calendar.YEAR, currentYear - 18)
        val start = calendar.timeInMillis

        calendar.set(Calendar.YEAR, currentYear + 62)
        val end = calendar.timeInMillis

        val datePicker =
            MaterialDatePicker.Builder.datePicker().setCalendarConstraints(CalendarConstraints.Builder().setStart(start).setEnd(end).setOpenAt(calendar1.timeInMillis).build()).setSelection(calendar1.timeInMillis).build()

        datePicker.show(fragmentManager, datePicker.toString())
        datePicker.addOnPositiveButtonClickListener {
            editText.setText(getDateFromMilliSeconds(it))

        }
    }

    fun convertDateToAmPm(timestamp: String?): String? {
        return try {
            val utcFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ")
            utcFormat.timeZone = TimeZone.getDefault()
            val date = utcFormat.parse(timestamp)
            val deviceFormat: DateFormat = SimpleDateFormat("h:mm a")
            deviceFormat.timeZone = TimeZone.getDefault() //Device timezone
            deviceFormat.format(date)
        }
        catch (e: Exception) {
            timestamp
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