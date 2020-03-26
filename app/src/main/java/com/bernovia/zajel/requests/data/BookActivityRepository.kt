package com.bernovia.zajel.requests.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.bernovia.zajel.api.ApiServicesRx
import com.bernovia.zajel.bookList.data.BooksRepository
import com.bernovia.zajel.helpers.PreferenceManager
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing
import com.bernovia.zajel.requests.models.BookActivity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface BookActivityRepository {

    companion object {
        const val SIZE_PAGE = 30
    }

    fun getSentRequestsListable(): Listing<BookActivity?>
    fun getReceiveRequestsListable(): Listing<BookActivity?>

    fun updateBookActivityStatus(bookActivityId: Int, value: String)
    fun deleteBookActivity(item: BookActivity)


    open class BookActivityRepositoryImpl(
        private val service: ApiServicesRx, private val dao: BookActivitiesDao) : BookActivityRepository {
        private val preferenceManager = PreferenceManager.instance

        val receiveRequestsBoundaryCallback: GenericBoundaryCallback<BookActivity?> by lazy {
            GenericBoundaryCallback({ dao.deleteAllReceivedRequestsList(preferenceManager.userId) }, { receivedRequests(it) }, { insertAllBookActivities(it) })
        }

        val sentRequestsBoundaryCallback: GenericBoundaryCallback<BookActivity?> by lazy {
            GenericBoundaryCallback({ dao.deleteAllSentRequestsList(preferenceManager.userId) }, { sentRequests(it) }, { insertAllBookActivities(it) })
        }


        override fun getSentRequestsListable(): Listing<BookActivity?> {
            return object : Listing<BookActivity?> {
                override fun getBoundaryCallback(): LiveData<GenericBoundaryCallback<BookActivity?>> {
                    return liveData(sentRequestsBoundaryCallback)

                }

                override fun getDataSource(): LiveData<PagedList<BookActivity?>> {
                    return dao.getAllSendRequestsPaginated(preferenceManager.userId).map { it }.toLiveData(pageSize = BooksRepository.SIZE_PAGE, boundaryCallback = sentRequestsBoundaryCallback)
                }
            }
        }

        override fun getReceiveRequestsListable(): Listing<BookActivity?> {
            return object : Listing<BookActivity?> {
                override fun getBoundaryCallback(): LiveData<GenericBoundaryCallback<BookActivity?>> {
                    return liveData(receiveRequestsBoundaryCallback)

                }

                override fun getDataSource(): LiveData<PagedList<BookActivity?>> {
                    return dao.getAllReceivedRequestsPaginated(preferenceManager.userId).map { it }.toLiveData(pageSize = BooksRepository.SIZE_PAGE, boundaryCallback = receiveRequestsBoundaryCallback)
                }


            }
        }

        override fun updateBookActivityStatus(bookActivityId: Int, value: String) {
            dao.updateStatus(bookActivityId, value).subscribeOn(Schedulers.io()).subscribe()
        }

        override fun deleteBookActivity(item: BookActivity) {
            dao.deleteBookActivity(item).subscribeOn(Schedulers.io()).subscribe()
        }


        private fun insertAllBookActivities(list: List<BookActivity?>): Completable {
            return dao.insertAllBookActivitiesList(list.map { it })
        }


        private fun sentRequests(page: Int): Single<List<BookActivity?>> {
            return service.requestsList("sent", SIZE_PAGE, page).map { it }
        }


        private fun receivedRequests(page: Int): Single<List<BookActivity?>> {
            return service.requestsList("received", SIZE_PAGE, page).map { it }
        }

    }


}