package com.bernovia.zajel.addBook

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.BuildConfig
import com.bernovia.zajel.R
import com.bernovia.zajel.addBook.data.AddBookViewModel
import com.bernovia.zajel.addBook.updateBook.UpdateBookViewModel
import com.bernovia.zajel.bookList.ui.BooksListViewModel
import com.bernovia.zajel.databinding.FragmentAddBookBinding
import com.bernovia.zajel.dialogs.ChoosePhotoDialogFragment
import com.bernovia.zajel.dialogs.DialogUtil
import com.bernovia.zajel.helpers.BroadcastReceiversUtil.BOTTOM_SHEET_SELECT_VALUE
import com.bernovia.zajel.helpers.BroadcastReceiversUtil.registerTheReceiver
import com.bernovia.zajel.helpers.ImageUtil
import com.bernovia.zajel.helpers.ImageUtil.CAMERA_PERMISSION_REQUEST_CODE
import com.bernovia.zajel.helpers.ImageUtil.CAMERA_REQUEST_CODE
import com.bernovia.zajel.helpers.ImageUtil.PICK_FILE_PERMISSION_REQUEST_CODE
import com.bernovia.zajel.helpers.ImageUtil.PICK_FILE_REQUEST_CODE
import com.bernovia.zajel.helpers.ImageUtil.createImageFile
import com.bernovia.zajel.helpers.ImageUtil.getFileImageFromCamera
import com.bernovia.zajel.helpers.ImageUtil.getFileImageFromGallery
import com.bernovia.zajel.helpers.ImageUtil.getFileName
import com.bernovia.zajel.helpers.ImageUtil.openCropActivityInFragment
import com.bernovia.zajel.helpers.ImageUtil.showFileChooser
import com.bernovia.zajel.helpers.InputFilterMinMax
import com.bernovia.zajel.helpers.NavigateUtil.closeFragment
import com.bernovia.zajel.helpers.TextWatcherAdapter
import com.bernovia.zajel.helpers.ValidateUtil.validateEmptyField
import com.bernovia.zajel.splashScreen.models.Genre
import com.bernovia.zajel.splashScreen.ui.MetaDataViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 */
class AddBookFragment : Fragment(), ChoosePhotoDialogFragment.ChoosePhotoClickListener, TextWatcherAdapter.TextWatcherListener {
    lateinit var binding: FragmentAddBookBinding
    private var imageDetails: ImageUtil.ImageDetails? = null
    private var isFromCamera = false
    private var fileName: String? = null
    private var photoFile: File? = null
    private lateinit var genres: ArrayList<Genre>
    private var bookId: Int = 0

    private val addBookViewModel: AddBookViewModel by viewModel()
    private val metaDataViewModel: MetaDataViewModel by viewModel()
    private val booksListViewModel: BooksListViewModel by viewModel()
    private val updateBookViewModel: UpdateBookViewModel by viewModel()


    private val onSelectedValue: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (isAdded) {
                when (intent.getStringExtra("title")) {
                    resources.getString(R.string.language) -> binding.languageEditText.setText(intent.getStringExtra("value"))
                    resources.getString(R.string.genre) -> binding.genreEditText.setText(intent.getStringExtra("value"))
                    resources.getString(R.string.status) -> binding.statusEditText.setText(intent.getStringExtra("value"))

                }
            }
        }
    }

    companion object {
        fun newInstance(bookId: Int): AddBookFragment {
            val args = Bundle()
            val fragment = AddBookFragment()
            args.putInt("book_id", bookId)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_book, container, false)
        return binding.root
    }

    private fun setData() {
        if (bookId != 0) {
            booksListViewModel.getBookById(bookId).observe(viewLifecycleOwner, Observer {
                ImageUtil.renderImage(it.image, binding.bookImageView, R.drawable.newsletter_placeholder, requireContext())
                binding.titleEditText.setText(it.title)
                binding.authorEditText.setText(it.author)
                binding.publishYearEditText.setText(it.publishedAt)
                binding.pageCountEditText.setText(it.pageCount.toString())
                binding.descriptionEditText.setText(it.description)
                binding.languageEditText.setText(it.language)
                binding.genreEditText.setText(it.genre)
                binding.statusEditText.setText(it.status?.capitalize())

            })
            binding.addButton.text = getString(R.string.update)
            binding.pageTitleTextView.text = getString(R.string.update_book)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            bookId = arguments?.getInt("book_id")!!
            setData()
        }

        registerTheReceiver(onSelectedValue, BOTTOM_SHEET_SELECT_VALUE)


        binding.bookImageView.setOnClickListener { DialogUtil.showChoosePhotoDialog(requireActivity().supportFragmentManager, this) }
        binding.titleEditText.addTextChangedListener(TextWatcherAdapter(binding.titleEditText, this))
        binding.authorEditText.addTextChangedListener(TextWatcherAdapter(binding.authorEditText, this))
        binding.pageCountEditText.addTextChangedListener(TextWatcherAdapter(binding.pageCountEditText, this))
        binding.publishYearEditText.addTextChangedListener(TextWatcherAdapter(binding.publishYearEditText, this))
        binding.descriptionEditText.addTextChangedListener(TextWatcherAdapter(binding.descriptionEditText, this))
        binding.languageEditText.addTextChangedListener(TextWatcherAdapter(binding.languageEditText, this))
        binding.genreEditText.addTextChangedListener(TextWatcherAdapter(binding.genreEditText, this))
        binding.statusEditText.addTextChangedListener(TextWatcherAdapter(binding.statusEditText, this))

        binding.addButton.setOnClickListener { submitForm() }

        binding.publishYearEditText.filters = arrayOf<InputFilter>(InputFilterMinMax("0", Calendar.getInstance().get(Calendar.YEAR).toString()))


        binding.genreEditText.setOnClickListener {
            DialogUtil.showSingleChoiceMenuFragment(requireActivity().supportFragmentManager, resources.getString(R.string.genre), binding.genreEditText.text.toString())
        }
        binding.statusEditText.setOnClickListener {
            DialogUtil.showSingleChoiceMenuFragment(requireActivity().supportFragmentManager, resources.getString(R.string.status), binding.statusEditText.text.toString())
        }


        binding.languageEditText.setOnClickListener {
            DialogUtil.showSingleChoiceMenuFragment(requireActivity().supportFragmentManager, resources.getString(R.string.language), binding.languageEditText.text.toString())
        }

        binding.backImageButton.setOnClickListener { closeFragment(requireActivity().supportFragmentManager, this) }

        metaDataViewModel.getMetaData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it?.genres != null) {
                genres = ArrayList()
                genres.addAll(it.genres)
            } else {
                metaDataViewModel.insertMetaDataInLocal()

            }
        })

    }

    private fun getGenreIdFromText(value: String): Int? {
        for (items in genres) {
            if (value == items.name) {
                return items.id
            }
        }
        return null
    }

    override fun chooseFromLibraryOnClick() {
        if (context != null) if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PICK_FILE_PERMISSION_REQUEST_CODE)
        } else {
            startActivityForResult(Intent.createChooser(showFileChooser(), resources.getString(R.string.choose_file_to_upload)), PICK_FILE_REQUEST_CODE)
        }


    }

    override fun takePhotoOnClick() {
        if (context != null) if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            if (activity != null) imageDetails = dispatchTakePictureIntent(requireActivity().packageManager)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PICK_FILE_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(Intent.createChooser(showFileChooser(), resources.getString(R.string.choose_file_to_upload)), PICK_FILE_REQUEST_CODE)
                }
                return
            }
            CAMERA_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (activity != null) imageDetails = dispatchTakePictureIntent(requireActivity().packageManager)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST_CODE) {
                if (data == null) {
                    return
                }
                if (data.data != null) {
                    isFromCamera = false
                    openCropActivityInFragment(this, requireContext(), data.data)
                    fileName = getFileName(data.data!!, requireContext())
                }
            }
            if (requestCode == CAMERA_REQUEST_CODE) {
                isFromCamera = true
                openCropActivityInFragment(this, requireContext(), imageDetails?.uri)
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {


                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri
                photoFile = if (isFromCamera) {
                    getFileImageFromCamera(requireContext(), resultUri)
                } else {
                    getFileImageFromGallery(requireContext(), resultUri, fileName)
                }
                Glide.with(requireContext()).load(resultUri).into(binding.bookImageView)


            }
        }
    }

    private fun dispatchTakePictureIntent(packageManager: PackageManager): ImageUtil.ImageDetails? {
        var photoURI: Uri? = null
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        return if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile(requireContext())
            }
            catch (ex: IOException) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
            ImageUtil.ImageDetails(photoFile, photoURI)
        } else {
            null
        }
    }

    private fun submitForm() {
        if (!validateEmptyField(binding.titleEditText, binding.titleTextInputLayout, requireActivity(), resources.getString(R.string.empty_title))) return
        if (!validateEmptyField(binding.authorEditText, binding.authorTextInputLayout, requireActivity(), resources.getString(R.string.empty_author))) return
        if (!validateEmptyField(binding.publishYearEditText, binding.publishYearTextInputLayout, requireActivity(), resources.getString(R.string.empty_publish_date))) return
        if (!validateEmptyField(binding.pageCountEditText, binding.pageCountTextInputLayout, requireActivity(), resources.getString(R.string.empty_page_count))) return
        if (!validateEmptyField(binding.descriptionEditText, binding.descriptionTextInputLayout, requireActivity(), resources.getString(R.string.empty_description))) return
        if (!validateEmptyField(binding.languageEditText, binding.languageTextInputLayout, requireActivity(), resources.getString(R.string.empty_language))) return
        if (!validateEmptyField(binding.genreEditText, binding.genreTextInputLayout, requireActivity(), resources.getString(R.string.empty_genre))) return
        if (!validateEmptyField(binding.statusEditText, binding.statusTextInputLayout, requireActivity(), resources.getString(R.string.empty_status))) return

        if (photoFile == null && bookId == 0) {
            Snackbar.make(binding.root, resources.getString(R.string.empty_photo), Snackbar.LENGTH_LONG).show()
        } else {


            if (bookId != 0) {
                if (photoFile == null) {
                    updateBookViewModel.setBookIdAndImage(bookId, null)
                } else {
                    val photoRequesBody: RequestBody = photoFile!!.asRequestBody("image/*".toMediaTypeOrNull())
                    val bookCover: MultipartBody.Part
                    bookCover = try {
                        MultipartBody.Part.createFormData("image", photoFile!!.name, photoRequesBody)
                    }
                    catch (e: java.lang.Exception) {
                        MultipartBody.Part.createFormData("image", "img", photoRequesBody)
                    }
                    updateBookViewModel.setBookIdAndImage(bookId, bookCover)
                }
            } else {
                val photoRequesBody: RequestBody = photoFile!!.asRequestBody("image/*".toMediaTypeOrNull())
                val bookCover: MultipartBody.Part
                bookCover = try {
                    MultipartBody.Part.createFormData("image", photoFile!!.name, photoRequesBody)
                }
                catch (e: java.lang.Exception) {
                    MultipartBody.Part.createFormData("image", "img", photoRequesBody)
                }
                addBookViewModel.setImage(bookCover)
            }


            val titleRequestBody = binding.titleEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val authorRequestBody = binding.authorEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val publishYearRequestBody = binding.publishYearEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val pageCountRequestBody = binding.pageCountEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val descriptionRequestBody = binding.descriptionEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageRequestBody = binding.languageEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val genreRequestBody = getGenreIdFromText(binding.genreEditText.text.toString()).toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val statusRequestBody = binding.statusEditText.text.toString().toLowerCase().toRequestBody("multipart/form-data".toMediaTypeOrNull())


            val map: HashMap<String, RequestBody> = HashMap()
            map["title"] = titleRequestBody
            map["author"] = authorRequestBody
            map["description"] = descriptionRequestBody
            map["language"] = languageRequestBody
            map["page_count"] = pageCountRequestBody
            map["published_at"] = publishYearRequestBody
            map["genre_id"] = genreRequestBody
            map["status"] = statusRequestBody


            if (bookId != 0) {
                updateBookViewModel.getDataFromRetrofit(map).observe(viewLifecycleOwner, Observer {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), getString(R.string.book_updated_success), Toast.LENGTH_SHORT).show()
                        closeFragment(requireActivity().supportFragmentManager, this)
                        booksListViewModel.updateBook(it?.body()!!)
                    }
                })

            } else {
                addBookViewModel.getDataFromRetrofit(map).observe(viewLifecycleOwner, Observer {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), getString(R.string.book_added_success), Toast.LENGTH_SHORT).show()
                        closeFragment(requireActivity().supportFragmentManager, this)
                    }
                })
            }
        }
    }

    override fun onTextChanged(view: EditText?, text: String?) {
        when (view?.id) {
            R.id.title_EditText -> validateEmptyField(binding.titleEditText, binding.titleTextInputLayout, requireActivity(), resources.getString(R.string.empty_title))
            R.id.author_EditText -> validateEmptyField(binding.authorEditText, binding.authorTextInputLayout, requireActivity(), resources.getString(R.string.empty_author))
            R.id.publish_year_EditText -> validateEmptyField(binding.publishYearEditText, binding.publishYearTextInputLayout, requireActivity(), resources.getString(R.string.empty_publish_date))
            R.id.description_EditText -> validateEmptyField(binding.descriptionEditText, binding.descriptionTextInputLayout, requireActivity(), resources.getString(R.string.empty_description))
            R.id.page_count_EditText -> validateEmptyField(binding.pageCountEditText, binding.pageCountTextInputLayout, requireActivity(), resources.getString(R.string.empty_page_count))
            R.id.language_EditText -> validateEmptyField(binding.languageEditText, binding.languageTextInputLayout, requireActivity(), resources.getString(R.string.empty_language))
            R.id.genre_EditText -> validateEmptyField(binding.genreEditText, binding.genreTextInputLayout, requireActivity(), resources.getString(R.string.empty_genre))
            R.id.status_EditText -> validateEmptyField(binding.statusEditText, binding.statusTextInputLayout, requireActivity(), resources.getString(R.string.empty_status))

        }
    }

}
