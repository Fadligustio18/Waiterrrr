package com.waiter.Views

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.waiter.R

class WaiterActivity : AppCompatActivity() {
    private lateinit var btnLogout: ImageView
    
    // Variabel untuk item navigasi kustom
    private lateinit var itemMenu: LinearLayout
    private lateinit var itemCart: LinearLayout
    private lateinit var itemStatus: LinearLayout
    
    private lateinit var iconMenu: ImageView
    private lateinit var iconCart: ImageView
    private lateinit var iconStatus: ImageView
    
    private lateinit var textMenu: TextView
    private lateinit var textCart: TextView
    private lateinit var textStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waiter_activity)
        supportActionBar?.hide()

        // Inisialisasi View
        btnLogout = findViewById(R.id.btnLogout)
        
        itemMenu = findViewById(R.id.item_menu)
        itemCart = findViewById(R.id.item_cart)
        itemStatus = findViewById(R.id.item_status)
        
        iconMenu = findViewById(R.id.icon_menu)
        iconCart = findViewById(R.id.icon_cart)
        iconStatus = findViewById(R.id.icon_status)
        
        textMenu = findViewById(R.id.text_menu)
        textCart = findViewById(R.id.text_cart)
        textStatus = findViewById(R.id.text_status)

        // Set fragment awal (Menu)
        if (savedInstanceState == null) {
            replaceFragment(WaiterMenuFragment())
            updateNavUI(0) // 0 untuk Menu
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "LogOut Berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Event Klik Navigasi
        itemMenu.setOnClickListener {
            replaceFragment(WaiterMenuFragment())
            updateNavUI(0)
        }

        itemCart.setOnClickListener {
            replaceFragment(CartFragment())
            updateNavUI(1)
        }

        itemStatus.setOnClickListener {
            replaceFragment(StatusFragment())
            updateNavUI(2)
        }
    }

    // Fungsi untuk memperbarui tampilan UI Navigasi (Warna & Teks)
    private fun updateNavUI(selectedIndex: Int) {
        // Ambil container utama navigasi untuk animasi
        val navContainer = findViewById<ViewGroup>(R.id.customBottomNav)
        
        // Inisialisasi animasi smooth (AutoTransition)
        val transition = AutoTransition().apply {
            duration = 250 // Durasi animasi dalam milidetik
        }
        TransitionManager.beginDelayedTransition(navContainer, transition)

        val items = listOf(itemMenu, itemCart, itemStatus)
        val icons = listOf(iconMenu, iconCart, iconStatus)
        val texts = listOf(textMenu, textCart, textStatus)
        
        val colorWhite = ContextCompat.getColor(this, R.color.white)
        val colorUnselected = ContextCompat.getColor(this, R.color.nav_unselected)

        for (i in items.indices) {
            if (i == selectedIndex) {
                // Item Aktif (Melebar & Berwarna)
                items[i].setBackgroundResource(R.drawable.nav_item_active_bg)
                icons[i].imageTintList = ColorStateList.valueOf(colorWhite)
                texts[i].visibility = View.VISIBLE
            } else {
                // Item Tidak Aktif (Mengecil & Abu-abu)
                items[i].background = null
                icons[i].imageTintList = ColorStateList.valueOf(colorUnselected)
                texts[i].visibility = View.GONE
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
