package com.waiter.Views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.waiter.R

class CashierActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cashier_activity)
        supportActionBar?.hide()
    }
}