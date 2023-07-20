package com.example.moviebase.utils.logger

import android.util.Log

class AndroidLogger: Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}