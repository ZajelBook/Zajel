package com.bernovia.zajel.editProfile.data

import androidx.lifecycle.LiveData
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.editProfile.models.EditProfileRequestBody
import com.bernovia.zajel.helpers.UpdateValuesResponseBody
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class EditProfileRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<UpdateValuesResponseBody, EditProfileRequestBody>(service, true) {
    override fun loadData(body: EditProfileRequestBody): LiveData<Any> {
        return fetchData {
            service.editUserProfile(body)
        }
    }
}