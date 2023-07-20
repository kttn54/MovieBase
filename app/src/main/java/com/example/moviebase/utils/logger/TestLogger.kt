package com.example.moviebase.utils.logger

import android.util.Log

class TestLogger: Logger {
    override fun d(tag: String, message: String) {
        println("DEBUG: $tag: $message")
    }

    override fun e(tag: String, message: String) {
        println("ERROR: $tag: $message")
    }
}