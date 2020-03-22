package com.bernovia.zajel.addBook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.BuildConfig
import com.bernovia.zajel.R
import com.bernovia.zajel.addBook.data.AddBookViewModel
import com.bernovia.zajel.addBook.models.AddBookRequestBody
import com.bernovia.zajel.databinding.FragmentAddBookBinding
import com.bernovia.zajel.dialogs.ChoosePhotoDialogFragment
import com.bernovia.zajel.dialogs.DialogUtil
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
import com.bernovia.zajel.helpers.TextWatcherAdapter
import com.bernovia.zajel.helpers.ValidateUtil.validateEmptyField
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class AddBookFragment : Fragment(), ChoosePhotoDialogFragment.ChoosePhotoClickListener, TextWatcherAdapter.TextWatcherListener {
    lateinit var binding: FragmentAddBookBinding
    private var imageDetails: ImageUtil.ImageDetails? = null
    private var isFromCamera = false
    private var fileName: String? = null
    private var photoFile: File? = null

    private val addBookViewModel: AddBookViewModel by viewModel()


    companion object {
        fun newInstance(): AddBookFragment {
            val args = Bundle()
            val fragment = AddBookFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_book, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bookImageView.setOnClickListener { DialogUtil.showChoosePhotoDialog(requireActivity().supportFragmentManager, this) }
        binding.titleEditText.addTextChangedListener(TextWatcherAdapter(binding.titleEditText, this))
        binding.authorEditText.addTextChangedListener(TextWatcherAdapter(binding.authorEditText, this))
        binding.pageCountEditText.addTextChangedListener(TextWatcherAdapter(binding.pageCountEditText, this))
        binding.publishYearEditText.addTextChangedListener(TextWatcherAdapter(binding.publishYearEditText, this))
        binding.descriptionEditText.addTextChangedListener(TextWatcherAdapter(binding.descriptionEditText, this))
        binding.languageEditText.addTextChangedListener(TextWatcherAdapter(binding.languageEditText, this))
        binding.genreEditText.addTextChangedListener(TextWatcherAdapter(binding.genreEditText, this))
        binding.addButton.setOnClickListener { submitForm() }


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


//                if (context != null) if (!isFromCamera) {
//                    try {
//                        photoFile = getFileImageFromGallery(context, resultUri, fileName)
//                        val photoRequesBody: RequestBody = photoFile!!.asRequestBody("image/*".toMediaTypeOrNull())
//                        val identity: MultipartBody.Part
//                        identity = try {
//                            MultipartBody.Part.createFormData("image", photoFile.name, photoRequesBody)
//                        }
//                        catch (e: java.lang.Exception) {
//                            MultipartBody.Part.createFormData("image", "img", photoRequesBody)
//                        }
//
////                        uploadPhotoViewModel.getDataFromRetrofit(identity).observe(viewLifecycleOwner, Observer {
////                            profileViewModel.insertUserProfileInLocal()
////
////                        })
//
//                    }
//                    catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                } else {
//                    try {
//                        photoFile = getFileImageFromCamera(context, resultUri)
//                        val photoRequesBody: RequestBody = photoFile!!.asRequestBody("image/*".toMediaTypeOrNull())
//                        val identity: MultipartBody.Part
//                        identity = try {
//                            MultipartBody.Part.createFormData("image", photoFile.name, photoRequesBody)
//                        }
//                        catch (e: java.lang.Exception) {
//                            MultipartBody.Part.createFormData("image", "img", photoRequesBody)
//                        }
//
////                        uploadPhotoViewModel.getDataFromRetrofit(identity).observe(viewLifecycleOwner, Observer {
////                            profileViewModel.insertUserProfileInLocal()
////
////                        })
//
//                    }
//                    catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
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
        if (photoFile == null) {
            Snackbar.make(binding.root, resources.getString(R.string.empty_photo), Snackbar.LENGTH_LONG).show()
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


           val titleRequestBody = binding.titleEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val authorRequestBody = binding.authorEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val publishYearRequestBody = binding.publishYearEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val pageCountRequestBody = binding.pageCountEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val descriptionRequestBody = binding.descriptionEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageRequestBody = binding.languageEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val genreRequestBody = binding.genreEditText.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())


            val map: HashMap<String, RequestBody> = HashMap()
            map.put("title", titleRequestBody)
            map.put("author", authorRequestBody)
            map.put("description", descriptionRequestBody)
            map.put("language", languageRequestBody)
            map.put("page_count", pageCountRequestBody)
            map.put("published_at", publishYearRequestBody)
            map.put("genre_id", genreRequestBody)


            addBookViewModel.getDataFromRetrofit(map).observe(viewLifecycleOwner, Observer {

            })

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

        }


    }

}
