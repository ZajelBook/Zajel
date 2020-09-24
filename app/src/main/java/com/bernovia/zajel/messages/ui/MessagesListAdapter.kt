package   com.bernovia.zajel.messages.ui

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.*
import com.bernovia.zajel.helpers.DateUtil.DATE_FORMAT
import com.bernovia.zajel.helpers.DateUtil.convertDateToAmPm
import com.bernovia.zajel.helpers.StringsUtil.getLineCount
import com.bernovia.zajel.helpers.StringsUtil.validateString
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.bernovia.zajel.messages.models.Message
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for the list of repositories.
 */
class MessagesListAdapter : PagedListAdapter<Message, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    private val ONE_LINE_WHITE = 0
    private val ONE_LINE_WHITE_TAIL = 1
    private val ONE_LINE_PURPLE = 2
    private val ONE_LINE_PURPLE_TAIL = 3
    private val MULTI_LINE_WHITE = 4
    private val MULTI_LINE_WHITE_TAIL = 5
    private val MULTI_LINE_PURPLE = 6
    private val MULTI_LINE_PURPLE_TAIL = 7

    private var day: String? = "day"
    private var day1: String? = "day 1"
    private var monthString: String? = null


    private lateinit var context: Context


    private var layoutInflater: LayoutInflater? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == ONE_LINE_WHITE) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }


            val binding: OneLineWhiteChatBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.one_line_white_chat, parent, false)
            return MyViewHolderOneLineWhite(binding)

        } else if (viewType == ONE_LINE_WHITE_TAIL) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }
            val binding: OneLineWhiteTailChatBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.one_line_white_tail_chat, parent, false)
            return MyViewHolderOneLineWhiteTail(binding)

        } else if (viewType == ONE_LINE_PURPLE) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }
            val binding: OneLinePurpleChatBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.one_line_purple_chat, parent, false)
            return MyViewHolderOneLinePurple(binding)

        } else if (viewType == ONE_LINE_PURPLE_TAIL) {

            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }
            val binding: OneLinePurpleTailChatBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.one_line_purple_tail_chat, parent, false)

            return MyViewHolderOneLinePurpleTail(binding)
        } else if (viewType == MULTI_LINE_WHITE) {


            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }
            val binding: MultiLineWhiteChatBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.multi_line_white_chat, parent, false)

            return MyViewHolderMultipleLineWhite(binding)
        } else if (viewType == MULTI_LINE_WHITE_TAIL) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }
            val binding: MultiLineWhiteTailChatBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.multi_line_white_tail_chat, parent, false)

            return MyViewHolderMultipleLineWhiteTail(binding)
        } else if (viewType == MULTI_LINE_PURPLE) {

            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }
            val binding: MultiLinePurpleChatBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.multi_line_purple_chat, parent, false)

            return MyViewHolderMultipleLinePurple(binding)
        } else if (viewType == MULTI_LINE_PURPLE_TAIL) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }
            val binding: MultiLinePurpleTailChatBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.multi_line_purple_tail_chat, parent, false)

            return MyViewHolderMultipleLinePurpleTail(binding)
        } else {


            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.context)
            }
            val binding: EmptyBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.empty, parent, false)

            return emptyVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val currentObject = getItem(position)

        var previousObject: Message?
        try {
            previousObject = getItem(position - 1)
        } catch (e: Exception) {
            previousObject = null
        }

        if (currentObject != null) {
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat(DATE_FORMAT)
            try {
                val date = format.parse(currentObject.createdAt)
                day = DateFormat.format("dd", date) as String // 20
                monthString = DateFormat.format("MMM", date) as String // Jun


                val date1: Date
                if (previousObject != null) {
                    date1 = format.parse(previousObject.createdAt)
                    day1 = DateFormat.format("dd", date1) as String // 20

                } else {
                    day1 = day
                }


            } catch (e: ParseException) {
            }

            Log.e("view", viewType.toString())

            when (viewType) {
                ONE_LINE_WHITE -> {
                    val myViewHolderOneLineWhite = holder as MyViewHolderOneLineWhite

                    setTextAndDate(myViewHolderOneLineWhite.binding.timeTextView,
                        myViewHolderOneLineWhite.binding.messageTextView,
                        validateString(getItem(position)!!.createdAt),
                        validateString(getItem(position)!!.content))
                    showDateBubble(myViewHolderOneLineWhite.binding.dateTextView, position)


                }

                ONE_LINE_WHITE_TAIL -> {
                    val myViewHolderOneLineWhiteTail = holder as MyViewHolderOneLineWhiteTail


                    setTextAndDate(myViewHolderOneLineWhiteTail.binding.timeTextView,
                        myViewHolderOneLineWhiteTail.binding.messageTextView,
                        validateString(getItem(position)!!.createdAt),
                        validateString(getItem(position)!!.content))


                    showDateBubble(myViewHolderOneLineWhiteTail.binding.dateTextView, position)


                }

                ONE_LINE_PURPLE -> {
                    val myViewHolderOneLinePurple = holder as MyViewHolderOneLinePurple
                    if (getItem(position) != null) {


                        setTextAndDate(myViewHolderOneLinePurple.binding.timeTextView,
                            myViewHolderOneLinePurple.binding.messageTextView,
                            validateString(getItem(position)!!.createdAt),
                            validateString(getItem(position)!!.content))

                        showDateBubble(myViewHolderOneLinePurple.binding.dateTextView, position)


                    }
                }

                ONE_LINE_PURPLE_TAIL -> {
                    val myViewHolderOneLinePurpleTail = holder as MyViewHolderOneLinePurpleTail
                    if (getItem(position) != null) {


                        setTextAndDate(myViewHolderOneLinePurpleTail.binding.timeTextView,
                            myViewHolderOneLinePurpleTail.binding.messageTextView,
                            validateString(getItem(position)!!.createdAt),
                            validateString(getItem(position)!!.content))


                        showDateBubble(myViewHolderOneLinePurpleTail.binding.dateTextView, position)


                    }
                }
                MULTI_LINE_WHITE -> {
                    val myViewHolderMultipleLineWhite = holder as MyViewHolderMultipleLineWhite
                    if (getItem(position) != null) {


                        setTextAndDate(myViewHolderMultipleLineWhite.binding.timeTextView,
                            myViewHolderMultipleLineWhite.binding.messageTextView,
                            validateString(getItem(position)!!.createdAt),
                            validateString(getItem(position)!!.content))

                        showDateBubble(myViewHolderMultipleLineWhite.binding.dateTextView, position)


                    }

                }
                MULTI_LINE_WHITE_TAIL -> {

                    val myViewHolderMultipleLineWhiteTail = holder as MyViewHolderMultipleLineWhiteTail
                    if (getItem(position) != null) {

                        setTextAndDate(myViewHolderMultipleLineWhiteTail.binding.timeTextView,
                            myViewHolderMultipleLineWhiteTail.binding.messageTextView,
                            validateString(getItem(position)!!.createdAt),
                            validateString(getItem(position)!!.content))



                        showDateBubble(myViewHolderMultipleLineWhiteTail.binding.dateTextView, position)

                    }
                }
                MULTI_LINE_PURPLE -> {
                    val myViewHolderMultipleLinePurple = holder as MyViewHolderMultipleLinePurple
                    if (getItem(position) != null) {


                        setTextAndDate(myViewHolderMultipleLinePurple.binding.timeTextView,
                            myViewHolderMultipleLinePurple.binding.messageTextView,
                            validateString(getItem(position)!!.createdAt),
                            validateString(getItem(position)!!.content))


                    }
                }
                MULTI_LINE_PURPLE_TAIL -> {
                    val myViewHolderMultipleLinePurpleTail = holder as MyViewHolderMultipleLinePurpleTail
                    if (getItem(position) != null) {


                        setTextAndDate(myViewHolderMultipleLinePurpleTail.binding.timeTextView,
                            myViewHolderMultipleLinePurpleTail.binding.messageTextView,
                            validateString(getItem(position)!!.createdAt),
                            validateString(getItem(position)!!.content))

                        showDateBubble(myViewHolderMultipleLinePurpleTail.binding.dateTextView, position)


                    }
                }
            }

        }


    }

    private fun showDateBubble(textView: TextView, position: Int) {
        try {
            val dateText = "$day $monthString"
            textView.text = dateText
            if (day != day1) {
                textView.visibility = View.VISIBLE
            } else {
                textView.visibility = View.GONE
            }
            if (position == 0) {
                textView.visibility = View.VISIBLE
            }
        } catch (ignore: Exception) {

        }
    }


    private fun setTextAndDate(dateTextView: TextView, messageContentTextView: TextView, createdAt: String, content: String) {
        dateTextView.text = convertDateToAmPm(convertDateToAmPm(createdAt))
        messageContentTextView.text = content


    }


    override fun getItemViewType(position: Int): Int {
        try {
            val currentObject = getItem(position)!!

            var nextObject: Message?
            try {
                nextObject = getItem(position + 1)
            } catch (e: Exception) {
                nextObject = null
            }

            return if (currentObject.senderId != preferenceManager.userId) {

                try {

                    if (nextObject!!.senderId == currentObject.senderId) {

                        if (validateString(currentObject.content).length < 12 && getLineCount(validateString(currentObject.content)) == 1) {
                            ONE_LINE_WHITE
                        } else {
                            MULTI_LINE_WHITE
                        }


                    } else {
                        if (validateString(currentObject.content).length < 12 && getLineCount(validateString(currentObject.content)) == 1) {
                            ONE_LINE_WHITE_TAIL
                        } else {
                            MULTI_LINE_WHITE_TAIL
                        }


                    }
                } catch (e: Exception) {
                    if (validateString(currentObject.content).length < 12 && getLineCount(validateString(currentObject.content)) == 1) {
                        ONE_LINE_WHITE_TAIL
                    } else {
                        MULTI_LINE_WHITE_TAIL
                    }

                }


            } else {
                try {

                    if (nextObject?.senderId != currentObject.senderId) {
                        if (validateString(currentObject.content).length < 12 && getLineCount(validateString(currentObject.content)) == 1) {
                            ONE_LINE_PURPLE_TAIL
                        } else {
                            MULTI_LINE_PURPLE_TAIL
                        }

                    } else {
                        if (validateString(currentObject.content).length < 12 && getLineCount(validateString(currentObject.content)) == 1) {
                            ONE_LINE_PURPLE
                        } else {
                            MULTI_LINE_PURPLE
                        }


                    }
                } catch (e: Exception) {

                    if (validateString(currentObject.content).length < 12 && getLineCount(validateString(currentObject.content)) == 1) {
                        ONE_LINE_PURPLE_TAIL
                    } else {
                        MULTI_LINE_PURPLE_TAIL
                    }


                }

            }
        } catch (e: java.lang.Exception) {
            return 9
        }


    }

    internal inner class MyViewHolderOneLineWhite(val binding: OneLineWhiteChatBinding) : RecyclerView.ViewHolder(binding.root)

    internal inner class MyViewHolderOneLineWhiteTail(val binding: OneLineWhiteTailChatBinding) : RecyclerView.ViewHolder(binding.root)

    internal inner class MyViewHolderOneLinePurple(val binding: OneLinePurpleChatBinding) : RecyclerView.ViewHolder(binding.root)

    internal inner class MyViewHolderOneLinePurpleTail(val binding: OneLinePurpleTailChatBinding) : RecyclerView.ViewHolder(binding.root)

    internal inner class MyViewHolderMultipleLineWhite(val binding: MultiLineWhiteChatBinding) : RecyclerView.ViewHolder(binding.root)

    internal inner class MyViewHolderMultipleLineWhiteTail(val binding: MultiLineWhiteTailChatBinding) : RecyclerView.ViewHolder(binding.root)

    internal inner class MyViewHolderMultipleLinePurple(val binding: MultiLinePurpleChatBinding) : RecyclerView.ViewHolder(binding.root)

    internal inner class MyViewHolderMultipleLinePurpleTail(val binding: MultiLinePurpleTailChatBinding) : RecyclerView.ViewHolder(binding.root)


    internal inner class emptyVH(val binding: EmptyBinding) : RecyclerView.ViewHolder(binding.root)


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem.id == newItem.id
        }
    }
}