package com.bernovia.zajel.helpers

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


object ImageUtil {


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

    fun renderImageWithNoPlaceHolder(photoUrl: String?, imageView: ImageView, context: Context) {
        if (photoUrl != null || photoUrl != "") {
            Glide.with(context).load(photoUrl).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        }

    }


}