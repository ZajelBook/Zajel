package com.bernovia.zajel.messages.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.bernovia.zajel.api.ApiServicesRx
import com.bernovia.zajel.helpers.apiCallsHelpers.liveData
import com.bernovia.zajel.helpers.paginationUtils.GenericBoundaryCallback
import com.bernovia.zajel.helpers.paginationUtils.Listing
import com.bernovia.zajel.messages.models.Message
import io.reactivex.Completable
import io.reactivex.Single

interface MessagesRepository {
    companion object {
        const val SIZE_PAGE = 30
    }

    fun getListable(): Listing<Message?>
    fun setConversationId(conversationId: Int)


    open class MessagesRepositoryImpl(
        private val service: ApiServicesRx, private val dao: MessagesDao) : MessagesRepository {

        private var conversationId: Int = 0
        override fun setConversationId(conversationId: Int) {
            this.conversationId = conversationId

        }


        val bc: GenericBoundaryCallback<Message?> by lazy {
            GenericBoundaryCallback({ dao.deleteAllMessagesById(conversationId) }, { messagesList(it) }, { insertAllMessagesList(it) })
        }


        override fun getListable(): Listing<Message?> {
            return object : Listing<Message?> {


                override fun getBoundaryCallback(): LiveData<GenericBoundaryCallback<Message?>> {
                    return liveData(bc)

                }

                override fun getDataSource(): LiveData<PagedList<Message?>> {
                    return dao.allMessagesPaginated(conversationId).map { it }.toLiveData(pageSize = SIZE_PAGE, boundaryCallback = bc)
                }


            }

        }


        private fun insertAllMessagesList(list: List<Message?>): Completable {
            return dao.insertAllMessages(list.map { it })
        }


        private fun messagesList(page: Int): Single<List<Message?>> {
            return service.messagesList(SIZE_PAGE, page, conversationId).map { it }
        }

    }


}