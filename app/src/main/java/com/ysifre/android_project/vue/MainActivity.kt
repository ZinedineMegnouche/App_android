package com.ysifre.android_project.vue

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ysifre.android_project.GetAlbumIdNetwork
import com.ysifre.android_project.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}