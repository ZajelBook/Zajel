package com.bernovia.zajel.helpers.paginationUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.bernovia.zajel.helpers.apiCallsHelpers.NetworkState


/**
 * encapsulated all methods for paginate in this class
 **/
interface Listing<T> {

    fun getBoundaryCallback(): LiveData<GenericBoundaryCallback<T>>
    fun getDataSource(): LiveData<PagedList<T>>
    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap(getBoundaryCallback()) { it.networkState }
}