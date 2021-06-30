package com.bernovia.zajel.bookList.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.R
import com.bernovia.zajel.actions.SendRequestRequestBody
import com.bernovia.zajel.actions.cancelRequest.CancelRequestViewModel
import com.bernovia.zajel.actions.sendRequest.SendRequestViewModel
import com.bernovia.zajel.databinding.FragmentBookDetailsBinding
import com.bernovia.zajel.helpers.ImageUtil
import com.bernovia.zajel.helpers.NavigateUtil.closeFragment
import com.bernovia.zajel.helpers.StringsUtil.validateString
import com.bernovia.zajel.helpers.ZajelUtil.isLoggedIn
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class BookDetailsFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentBookDetailsBinding
    private val booksListViewModel: BooksListViewModel by viewModel()
    private val sendRequestViewModel: SendRequestViewModel by viewModel()
    private val cancelRequestViewModel: CancelRequestViewModel by viewModel()


    companion object {
        fun newInstance(bookId: Int): BookDetailsFragment {
            val args = Bundle()
            args.putInt("book_id", bookId)
            val fragment = BookDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_book_details, container, false)
        if (arguments != null && arguments?.getInt("book_id") != null) {
            booksListViewModel.getBookById(arguments?.getInt("book_id")!!)
                .observe(viewLifecycleOwner, Observer {
                    binding.bookDetails = it
                    if (it?.image != null) {
                        ImageUtil.renderBlurImage(
                            validateString(it.image),
                            requireContext(),
                            50,
                            binding.backgroundImageView,
                            R.drawable.newsletter_placeholder
                        )
                        ImageUtil.renderImageWithNoPlaceHolder(
                            validateString(it.image),
                            binding.bookImageView,
                            requireContext()
                        )
                        if (it.requested) {
                            binding.borrowBookButton.text = resources.getString(R.string.cancel)

                        } else {
                            binding.borrowBookButton.text = resources.getString(R.string.borrow)

                        }

                    } else {
                        booksListViewModel.getBookAndInsertInLocal(arguments?.getInt("book_id")!!)
                    }


                    if (it != null)
                        if ((it.userId == preferenceManager.userId) || it.isMock
                            || it.status?.toLowerCase()== "unavailable") {
                            binding.borrowBookButton.visibility = View.GONE
                        }

                    if (!isLoggedIn()) {
                        binding.borrowBookButton.visibility = View.GONE
                    }

                })
        }

        binding.borrowBookButton.setOnClickListener {
            if (binding.bookDetails != null) {
                if (binding.bookDetails!!.requested) {
                    cancelRequestViewModel.getDataFromRetrofit(binding.bookDetails!!.id)
                        .observe(viewLifecycleOwner, Observer {
                            binding.borrowBookButton.text = resources.getString(R.string.borrow)
                            booksListViewModel.updateRequested(binding.bookDetails!!.id, false)
                        })
                } else {
                    sendRequestViewModel.getDataFromRetrofit(SendRequestRequestBody(binding.bookDetails!!.id))
                        .observe(viewLifecycleOwner, Observer {
                            binding.borrowBookButton.text = resources.getString(R.string.cancel)
                            booksListViewModel.updateRequested(binding.bookDetails!!.id, true)

                        })
                }
            }
        }


        if (Locale.getDefault().language == "ar") {
            binding.authorNameTextView.gravity = Gravity.END
            binding.bookNameTextView.gravity = Gravity.END

        } else {
            binding.authorNameTextView.gravity = Gravity.START
            binding.bookNameTextView.gravity = Gravity.START


        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.descriptionTextView.movementMethod = ScrollingMovementMethod()
        binding.backImageButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_ImageButton -> closeFragment(requireActivity().supportFragmentManager, this)
        }

    }

}
