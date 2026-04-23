package com.waiter.Views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.waiter.R

class ChefActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chef_activity)
        supportActionBar?.hide()

    }
}