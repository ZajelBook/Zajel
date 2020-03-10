package com.bernovia.zajel.helpers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TextWatcherAdapter(
    private var editText: EditText,
    private var listener: TextWatcherListener
) : TextWatcher {


    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        listener.onTextChanged(editText,
            s.toString().trim { it <= ' ' }
        )
    }

    override fun beforeTextChanged(
        s: CharSequence?, start: Int, count: Int, after: Int
    ) {
        // pass
    }

    override fun afterTextChanged(s: Editable?) {
        // pass
    }

    interface TextWatcherListener {
        fun onTextChanged(view: EditText?, text: String?)
    }

}
