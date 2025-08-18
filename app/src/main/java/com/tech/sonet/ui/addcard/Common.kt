package com.tech.sonet.ui.addcard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object Common {
    var img: String? = null

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }
}