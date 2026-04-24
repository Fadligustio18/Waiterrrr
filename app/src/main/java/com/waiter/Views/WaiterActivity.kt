package com.waiter.Views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.waiter.R

class WaiterActivity : AppCompatActivity() {
    private lateinit var btnLogout: ImageView
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waiter_activity)
        supportActionBar?.hide()

        btnLogout = findViewById(R.id.btnLogout)
        bottomNav = findViewById(R.id.bottomNav)

        // Set fragment awal (Halaman Pesanan/Menu)
        if (savedInstanceState == null) {
            replaceFragment(WaiterMenuFragment())
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "LogOut Berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Navigasi antar fragment
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menu -> {
                    replaceFragment(WaiterMenuFragment())
                    true
                }
                R.id.nav_cart -> {
                    replaceFragment(CartFragment())
                    true
                }
                R.id.nav_notif -> {
                    replaceFragment(StatusFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}