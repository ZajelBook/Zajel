package com.bernovia.zajel.helpers

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.bernovia.zajel.R
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

object ValidateUtil {

    fun validateEmail(
        editText: EditText,
        textInputLayout: TextInputLayout,
        activity: Activity
    ): Boolean {
        val email = editText.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !isEmailValid(email)) {
            textInputLayout.error = activity.getString(R.string.invalid_email)
            requestFocus(editText, activity)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }


    fun validatePassword(
        editText: EditText,
        textInputLayout: TextInputLayout,
        activity: Activity
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty() || editText.text
                .toString().length < 8
        ) {
            textInputLayout.error = activity.getString(R.string.password_not_right)
            requestFocus(editText, activity)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    fun validateFieldsDidnotMatch(
        editText: EditText, textInputLayout: TextInputLayout
        , activity: Activity, editText1: EditText, errorMessage: String?
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty() || editText.text
                .toString() != editText1.text.toString()
        ) {
            textInputLayout.error = errorMessage
            requestFocus(editText, activity)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }


    fun validateEmptyField(
        editText: EditText,
        textInputLayout: TextInputLayout,
        activity: Activity,
        msg: String?
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            textInputLayout.error = msg
            requestFocus(editText, activity)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern =
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun requestFocus(view: View, context: Activity) {
        if (view.requestFocus()) {
            context.window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }
}