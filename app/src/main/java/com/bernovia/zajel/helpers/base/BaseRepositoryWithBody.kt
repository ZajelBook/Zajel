package com.bernovia.zajel.helpers.base

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bernovia.zajel.App
import com.bernovia.zajel.api.ApiServicesCoRoutines
import com.bernovia.zajel.helpers.NetworkUtil
import com.bernovia.zajel.helpers.base.errorModels.ResponseErrorBody
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Response

abstract class BaseRepositoryWithBody<T, V>(@PublishedApi internal val service: ApiServicesCoRoutines, val showToast: Boolean) {

    abstract fun loadData(body: V): LiveData<Any>

    inline fun <reified T : Any> fetchData(crossinline call: (ApiServicesCoRoutines) -> Deferred<Response<T>>): LiveData<Any> {
        val result = MutableLiveData<Any>()

        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = response
                    } else {
                        result.value = response


                        val gson = Gson()
                        val message = gson.fromJson(response.errorBody()!!.charStream(), ResponseErrorBody::class.java)


                        if (message != null) {
                            if (showToast) Toast.makeText(App.context, message.error, Toast.LENGTH_SHORT).show()

                        }

                    }
                } catch (e: Throwable) {
                    NetworkUtil.handleApiError(e)

                }
            }
        }

        return result
    }

}