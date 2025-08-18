package com.tech.sonet.ui.base.location

import android.location.Location

interface LocationResultListener {
    fun getLocation(location: Location)
}