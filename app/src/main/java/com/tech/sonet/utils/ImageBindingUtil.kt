package com.tech.sonet.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tech.sonet.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object ImageBindingUtil {


    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(lay: AppCompatImageView, image: String?) {
        if (image != null) {
            Glide.with(lay.context).load(image).into(lay)
        }else{
           lay.setImageResource(R.drawable.placeholder)
        }
    }

    @BindingAdapter("loadImageFirst")
    @JvmStatic
    fun loadImageFirst(lay: AppCompatImageView, image: String?) {
        if (image != null) {
            Glide.with(lay.context).load(image).into(lay)
        }else{

            Glide.with(lay.context).load(R.drawable.asa).into(lay)
        }
    }
    @BindingAdapter("checkOnLine")
    @JvmStatic
    fun checkOnLine(lay: SwitchCompat, image: Int?) {
        if (image != null) {
          if(image==1){
              lay.trackDrawable =
                  ContextCompat.getDrawable(lay.context, R.drawable.switch_button_background)

              lay.isChecked =true

          }else {
              lay.isChecked= false
              lay.trackDrawable =
                  ContextCompat.getDrawable(lay.context, R.drawable.toggle_btn_background)
          }
        }
    }

    @BindingAdapter("loadAccept")
    @JvmStatic
    fun loadAccept(tv: TextView, type: Int?) {
        if (type != null) {
            if (type == 1) {
                tv.text = "Matched"
                tv.isEnabled = false
                tv.isClickable = false
            } else if (type == 2) {
                tv.text = "Not Matched"
            }
        }
    }
    @BindingAdapter("loadAccepted")
    @JvmStatic
    fun loadAccepted(tv: TextView, type: Int?) {
        if (type != null) {
            if (type == 1) {
                tv.text = "Matched"
                tv.isEnabled = false
                tv.isClickable = false
            }else if(type == 2){
                tv.text = "Not Matched"
                tv.isEnabled = false
                tv.isClickable = false
            }
        }
    }

    @BindingAdapter("loadRequest")
    @JvmStatic
    fun loadRequest(tv: TextView, type: Int?) {
        if (type != null) {
            if (type == 3 ) {
                tv.text = "Like Sent"
                tv.isEnabled = false
                tv.isClickable = false
            }else if (type == 4){
                tv.text = "Like "
                tv.isEnabled = false
                tv.isClickable = false
//                tv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#898989"))
            }else if (type == 2){
                tv.text = "Not Matched"
                tv.isEnabled = false
                tv.isClickable = false
            }
            else if (type == 1){
                tv.text = "Matched"
                tv.isEnabled = false
                tv.isClickable = false
            }
            else{
                tv.text = "Like"
                tv.isEnabled = true
                tv.isClickable = true
            }


        }  else{
            tv.text = "Like"
            tv.isEnabled = true
            tv.isClickable = true
        }
    }

    @BindingAdapter("loadCons")
    @JvmStatic
    fun loadCons(layout : ConstraintLayout, type : Int?) {
        if (type != null) {
            if (type == 3) {
                layout.isEnabled = false
                layout.isClickable = false
            } else if (type == 4){
                layout.isEnabled = false
                layout.isClickable = false
                layout.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#898989"))
            } else if (type == 2){
                layout.isEnabled = false
                layout.isClickable = false
                layout.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#898989"))
            }
            else if (type  == 1 ){
                layout.isEnabled = false
                layout.isClickable = false
                layout.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#898989"))
            }
            else{
                layout.isEnabled = true
                layout.isClickable = true
            }
        } else{
            layout.isEnabled = true
            layout.isClickable = true
        }
    }
    @BindingAdapter("loadScreenShot")
    @JvmStatic
    fun loadScreenShot(tv: TextView, type: Int?) {
        if (type != null) {
            if (type == 1) {
                tv.visibility = View.VISIBLE
            } else   {
                tv.visibility = View.GONE
            }
        }
    }

    @BindingAdapter("loadScreenShot2")
    @JvmStatic
    fun loadScreenShot2(tv: ConstraintLayout, type: Int?) {
        if (type != null) {
            if (type == 3) {
                tv.visibility = View.GONE
            } else {
                tv.visibility = View.VISIBLE
            }
        }
    }



    @SuppressLint("SetTextI18n")
    @BindingAdapter("roundOff")
    @JvmStatic
    fun roundOff(tv: TextView, no: String?) {
        if (no != null) {
               tv.text = String.format("%.0f", no?.toDouble()) + " ft"
        }
    }

    @BindingAdapter("setDistanceToFeet")
    @JvmStatic
    fun setDistanceToFeet(tv: TextView, no: String?) {
        no?.toDoubleOrNull()?.let { distanceInMeters ->
            val distanceInFeet = distanceInMeters * 3.28084 // Convert meters to feet
            tv.text = "%.2f ft".format(distanceInFeet) // Format the result to 2 decimal places
        } ?: run {
            tv.text = "0.00 ft" // Default value for null or invalid input
        }
    }

    @BindingAdapter("changeDateFormat")
    @JvmStatic
    fun changeDateFormat(text: TextView, date: String?) {
        if (!date.isNullOrEmpty()) {
            try {
                // Define the input and output date formats
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")

                val outputFormat = SimpleDateFormat("MMMM dd HH:mm", Locale.getDefault())

                // Parse and format the date
                val parsedDate = inputFormat.parse(date)
                val formattedDate = parsedDate?.let { outputFormat.format(it) } ?: ""

                // Set the formatted date on the TextView
                text.text = formattedDate
            } catch (e: Exception) {
                // Handle any parsing errors by setting default or empty text
                text.text = ""
            }
        } else {
            // Set empty text if the input date is null or empty
            text.text = ""
        }
    }



    inline fun <reified T> parseJson(json: String): T? {
        return try {
            val gson = Gson()
            gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
}