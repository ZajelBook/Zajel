package com.bernovia.zajel.messages.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentMessagesListBinding
import com.bernovia.zajel.helpers.StringsUtil.validateString
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.bernovia.zajel.messages.models.Message
import com.bernovia.zajel.messages.sendMessage.SendMessageRequestBody
import com.bernovia.zajel.messages.sendMessage.SendMessageViewModel
import com.google.gson.Gson
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
    lateinit var consumer: Consumer
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
        messagesListViewModel.setConversationId(requireArguments().getInt("conversation_id"))
        messagesListViewModel.refreshPage().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        sendMessageViewModel.setConversationId(requireArguments().getInt("conversation_id"))

        messagesListViewModel.dataSource.observe(viewLifecycleOwner, Observer {
            Log.e("test", it.size.toString() + "a")
            messagesListAdapter.submitList(it)
            object : CountDownTimer(100, 100) {

                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    binding.messagesRecyclerView.scrollToPosition(it.size - 1)
                }
            }.start()
        })

        binding.messagesRecyclerView.apply {
            adapter = messagesListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


        val uri = URI("wss://zajel.mylestone.life/cable")

        val options = Consumer.Options()

        val query: MutableMap<String, String> = HashMap()
        query["uid"] = validateString(preferenceManager.uid)
        query["client"] = validateString(preferenceManager.client)
        query["access-token"] = validateString(preferenceManager.accessToken)
        options.query = query

        consumer = ActionCable.createConsumer(uri, options)


        // 2. Create subscription
        val sendMessageChannel = Channel("ConversationChannel")
        sendMessageChannel.addParam("id", requireArguments().getInt("conversation_id"))
        sendMessageChannel.addParam("command", "subscribe")


//        {"object":{"content":"hi","sender_type":"User","sender_id":2,"sender_name":"Mohamad Mokresh","created_at":"2020-03-30 15:17"}}
        val subscription = consumer.subscriptions.create(sendMessageChannel)

        subscription.onConnected {
            Log.e("connec", "con")
            // Called when the subscription has been successfully completed
        }.onRejected {
            Log.e("reject", "rej")

            // Called when the subscription is rejected by the server
        }.onReceived {
            val jsonObject = it.asJsonObject
            val message = Gson().fromJson(jsonObject?.getAsJsonObject("object"), Message::class.java)
            messagesListViewModel.insertMessage(message)

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
                    binding.messageEditText.setText("")
                })
            }

        }
    }

    override fun onStop() {
        super.onStop()
        consumer.disconnect()
    }

}
