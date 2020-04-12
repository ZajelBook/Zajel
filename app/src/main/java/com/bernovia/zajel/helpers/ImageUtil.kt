package com.bernovia.zajel.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object ImageUtil {
    const val PICK_FILE_REQUEST_CODE = 1
    const val CAMERA_REQUEST_CODE = 2
    const val PICK_FILE_PERMISSION_REQUEST_CODE = 4444
    const val CAMERA_PERMISSION_REQUEST_CODE = 5555

    fun showFileChooser(): Intent? {
        val intent = Intent()
//        intent.setType("*/*");
        intent.type = "image/*"
        //allows to select data and return it
        intent.action = Intent.ACTION_PICK
        return intent
    }


    fun renderBlurImage(photoUrl: String, context: Context, radius: Int, imageView: ImageView, placeholder: Int) {
        if (photoUrl != "") {
            Glide.with(context).load(photoUrl).fitCenter().apply(bitmapTransform(BlurTransformation(radius, 1))).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        }
    }


    fun renderImageWithCenterInside(photoUrl: String?, imageView: ImageView, placeholder: Int, context: Context) {
        if (photoUrl == "" || photoUrl == null) {
            Glide.with(context).load(placeholder).fitCenter().centerInside().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        } else {
            Glide.with(context).load(photoUrl).placeholder(placeholder).error(placeholder).fitCenter().centerInside().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        }

    }


    fun renderImage(photoUrl: String?, imageView: ImageView, placeholder: Int, context: Context) {
        if (photoUrl == "" || photoUrl == null) {
            Glide.with(context).load(placeholder).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        } else {
            Glide.with(context).load(photoUrl).placeholder(placeholder).error(placeholder).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        }

    }

    fun openCropActivityInFragment(fragment: Fragment?, context: Context?, uri: Uri?) {
        if (context != null) CropImage.activity(uri).setAllowFlipping(false).setMinCropWindowSize(900, 1200).start(context,
            fragment!!)
    }

    fun resize(image: Bitmap?, maxWidth: Int, maxHeight: Int): Bitmap? {
        var image = image
        return if (maxHeight > 0 && maxWidth > 0) {
            val width = image?.width
            val height = image?.height
            val ratioBitmap = width?.toFloat()?.div(height!!.toFloat())
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap!!) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap!!).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }

            if (image != null) image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            image
        } else {
            image
        }
    }

    fun getFileImageFromCamera(context: Context?, uri: Uri?): File? {
        val photo = File(context!!.cacheDir, "ANDROID_CAMERA.jpg")
        val inputStream: InputStream
        try {
            if (context != null) {
                inputStream = BufferedInputStream(Objects.requireNonNull(context.contentResolver.openInputStream(uri!!)))
                val bitmap1: Bitmap? = decodeSampledBitmapFromResource(inputStream, inputStream, 900, 1200)
                var bitmap: Bitmap? = null
                try {
                    bitmap = rotateImageIfRequired(context, bitmap1, uri)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
                val stream = ByteArrayOutputStream()
                resize(bitmap, 900, 1200)?.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                val imageInByte = stream.toByteArray()
                //create a file to write bitmap data
                try {
                    photo.createNewFile()
                    val fos = FileOutputStream(photo)
                    fos.write(imageInByte)
                    fos.flush()
                    fos.close()
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return photo
    }

    fun decodeSampledBitmapFromResource(
        was: InputStream?, `is`: InputStream, reqWidth: Int, reqHeight: Int): Bitmap? {
        val bitmap_decode: Bitmap?
        try {
            `is`.mark(`is`.available())
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(was, null, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        try {
            `is`.reset()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        bitmap_decode = BitmapFactory.decodeStream(`is`, null, options)
        return bitmap_decode
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    @Throws(IOException::class) fun rotateImageIfRequired(context: Context, img: Bitmap?, selectedImage: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(selectedImage)
        val ei: ExifInterface
        ei = if (Build.VERSION.SDK_INT > 23) ExifInterface(input!!) else ExifInterface(selectedImage.path!!)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap?, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        if (img != null) {
            val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
            img.recycle()
            return rotatedImg

        } else {
            return null
        }

    }


    fun getFileImageFromGallery(context: Context?, uri: Uri?, fileName: String?): File? {
        return if (context != null) {
            val photo = File(context.cacheDir, fileName)
            val selectedFilePath: String = FilePath.getPath(context, uri)
            if (selectedFilePath != null) {
                val imgFile = File(selectedFilePath)
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                val stream = ByteArrayOutputStream()
                resize(bitmap, 900, 1200)?.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                val imageInByte = stream.toByteArray()
                //create a file to write bitmap data
                try {
                    photo.createNewFile()
                    val fos = FileOutputStream(photo)
                    fos.write(imageInByte)
                    fos.flush()
                    fos.close()
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            photo
        } else {
            null
        }
    }

    fun getFileName(uri: Uri, context: Context): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            var cut = 0
            if (result != null) {
                cut = result!!.lastIndexOf('/')
            }
            if (cut != -1) {
                if (result != null) {
                    result = result!!.substring(cut + 1)
                }
            }
        }
        return result
    }

    fun renderImageWithNoPlaceHolder(photoUrl: String?, imageView: ImageView, context: Context) {
        if (photoUrl != null || photoUrl != "") {
            Glide.with(context).load(photoUrl).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        }

    }

    @SuppressLint("SimpleDateFormat") @Throws(IOException::class) fun createImageFile(context: Context): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */)

        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = image.absolutePath
        return image
    }

    class ImageDetails(var file: File?, var uri: Uri?)

}