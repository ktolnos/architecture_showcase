package com.example.architecture.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.architecture.R

/**
 * Contains [ArticlesFragment].
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
