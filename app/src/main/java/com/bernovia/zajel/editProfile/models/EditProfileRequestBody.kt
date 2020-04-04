package com.bernovia.zajel.editProfile.models


import com.google.gson.annotations.SerializedName

class EditProfileRequestBody(


) {
    @SerializedName("latitude")
    var latitude: Double? = null

    @SerializedName("longitude")
    var longitude: Double? = null

    @SerializedName("fcm_token")
    var fcmToken: String? = null

    @SerializedName("birth_date")
    var birthDate: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    @SerializedName("phone_number")
    var phoneNumber: String? = null


    constructor(latitude: Double, longitude: Double?, fcmToken: String?) : this() {
        this.latitude = latitude
        this.longitude = longitude
        this.fcmToken = fcmToken

    }

    constructor(fcmToken: String?, birthDate: String, firstName: String, lastName: String, phoneNumber: String) : this() {
        this.fcmToken = fcmToken
        this.birthDate = birthDate
        this.firstName = firstName
        this.lastName = lastName
        this.phoneNumber = phoneNumber

    }


}