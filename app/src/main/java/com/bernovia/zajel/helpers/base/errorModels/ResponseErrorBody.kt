package com.bernovia.zajel.helpers.base.errorModels


import com.google.gson.annotations.SerializedName

data class ResponseErrorBody(@SerializedName("error") val error: String


                            )