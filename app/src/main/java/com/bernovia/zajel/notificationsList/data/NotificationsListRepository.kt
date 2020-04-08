package com.bernovia.zajel.notificationsList.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.bernovia.zajel.api.ApiServicesRx
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing
import com.bernovia.zajel.notificationsList.models.Notification
import io.reactivex.Completable
import io.reactivex.Single

interface NotificationsListRepository {
    companion object {
        const val SIZE_PAGE = 30
    }

    fun getListable(): Listing<Notification>


    open class NotificationsListRepositoryImpl(
        private val service: ApiServicesRx, private val dao: NotificationsDao) : NotificationsListRepository {


        val bc: GenericBoundaryCallback<Notification> by lazy {
            GenericBoundaryCallback({ dao.deleteAllNotificationsList() }, { notificationsList(it) }, { insertAllNotifications(it) }, false)
        }

        override fun getListable(): Listing<Notification> {
            return object : Listing<Notification> {
                override fun getBoundaryCallback(): LiveData<GenericBoundaryCallback<Notification>> {
                    return liveData(bc)
                }

                override fun getDataSource(): LiveData<PagedList<Notification>> {
                    return dao.allNotificationsPaginated().map { it }.toLiveData(pageSize = SIZE_PAGE, boundaryCallback = bc)
                }
            }
        }


        private fun insertAllNotifications(list: List<Notification>): Completable {
            return dao.insertAllNotifications(list.map { it })
        }

        fun notificationsList(page: Int): Single<List<Notification>> {
            return service.notificationsList(SIZE_PAGE, page).map { it }
        }

    }

}