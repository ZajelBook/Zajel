package com.bernovia.zajel.editProfile.data

import androidx.lifecycle.LiveData
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.editProfile.models.GetProfileResponseBody
import com.bernovia.zajel.helpers.base.BaseRepositoryWithBody

class GetProfileRepository(service: ApiServicesCoRoutines) : BaseRepositoryWithBody<GetProfileResponseBody, Int>(service, true) {

    override fun loadData(body: Int): LiveData<Any> {
        return fetchData {
            service.getUserProfileAsync(body)
        }
    }
}