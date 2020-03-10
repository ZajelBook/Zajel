package com.bernovia.zajel.helpers


object StringsUtil {

    fun uppercaseFirstCharacter(string: String?): String? {
        return try {
            if (string != null) {
                val upperString = string.substring(0, 1).toUpperCase() + string.substring(1)
                upperString
            } else {
                string
            }
        } catch (e: Exception) {
            string
        }
    }


    fun validateString(string: String?): String {
        return if (string != null && string != "" && string != "null" && string != "Null") {
            string
        } else {
            ""
        }

    }


}