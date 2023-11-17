package com.kristinaefros.challenge.utils.extensions

import android.net.Uri
import android.webkit.URLUtil
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.kristinaefros.challenge.BuildConfig


fun ImageView.setImageUri(uri: String?) {
    if (uri != null) {
        this.setImageDrawable(null)
        if (URLUtil.isValidUrl(uri)) {
            Glide.with(this).load(uri).into(this)
        } else {
            Glide.with(this).load(Uri.parse(uri)).signature(ObjectKey(BuildConfig.VERSION_CODE)).into(this)
        }
    } else {
        this.setImageDrawable(null)
    }
}