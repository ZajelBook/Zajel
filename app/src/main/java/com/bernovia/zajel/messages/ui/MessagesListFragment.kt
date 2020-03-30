package com.bernovia.zajel.messages.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentMessagesListBinding
import com.bernovia.zajel.helpers.StringsUtil.validateString
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.bernovia.zajel.messages.sendMessage.SendMessageRequestBody
import com.bernovia.zajel.messages.sendMessage.SendMessageViewModel
import com.hosopy.actioncable.ActionCable
import com.hosopy.actioncable.Channel
import com.hosopy.actioncable.Consumer
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URI


/**
 * A simple [Fragment] subclass.
 */
class MessagesListFragment : Fragment() {
    lateinit var binding: FragmentMessagesListBinding
    private val messagesListViewModel: MessagesListViewModel by viewModel()

    private val sendMessageViewModel: SendMessageViewModel by viewModel()

    companion object {
        fun newInstance(conversationId: Int): MessagesListFragment {
            val args = Bundle()
            val fragment = MessagesListFragment()
            args.putInt("conversation_id", conversationId)
            fragment.arguments = args
            return fragment
        }
    }


    private val messagesListAdapter: MessagesListAdapter by lazy {
        MessagesListAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        messagesListViewModel.refreshPage().observe(viewLifecycleOwner, Observer { it.refreshPage() })
//        messagesListViewModel.setConversationId(requireArguments().getInt("conversation_id"))
        sendMessageViewModel.setConversationId(requireArguments().getInt("conversation_id"))

//        messagesListViewModel.dataSource.observe(viewLifecycleOwner, Observer {
//            messagesListAdapter.submitList(it)
//        })

//        binding.messagesRecyclerView.apply {
//            adapter = messagesListAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//        }


        val uri = URI("wss://zajel.mylestone.life/cable")

        val options = Consumer.Options()

        val query: MutableMap<String, String> = HashMap()
        query["uid"] = validateString(preferenceManager.uid)
        query["client"] = validateString(preferenceManager.client)
        query["access-token"] = validateString(preferenceManager.accessToken)
        options.query = query

        val consumer = ActionCable.createConsumer(uri, options)


        // 2. Create subscription
        val sendMessageChannel = Channel("ConversationChannel")
        sendMessageChannel.addParam("id", requireArguments().getInt("conversation_id"))
        sendMessageChannel.addParam("command", "subscribe")

//        sendMessageChannel.addParam("identifier", "{\\\"channel\\\":\\\"ConversationChannel\\\", \\\"id\\\": \\\"" +  + "\\\"}")


        val subscription = consumer.subscriptions.create(sendMessageChannel)

        subscription.onConnected {
            Log.e("connec", "con")
            // Called when the subscription has been successfully completed
        }.onRejected {
            Log.e("reject", "rej")

            // Called when the subscription is rejected by the server
        }.onReceived {
            Log.e("onReceived", it.toString())
            // Called when the subscription receives data from the server
        }.onDisconnected {
            // Called when the subscription has been closed
        }.onFailed {
            Log.e("onFailed", it.message)

            // Called when the subscription encounters any error
        }

        consumer.connect()

        binding.sendImageView.setOnClickListener {
            if (binding.messageEditText.text.toString() != "") {
                sendMessageViewModel.getDataFromRetrofit(SendMessageRequestBody(binding.messageEditText.text.toString())).observe(viewLifecycleOwner, Observer {

                })
            }

        }
    }

}
