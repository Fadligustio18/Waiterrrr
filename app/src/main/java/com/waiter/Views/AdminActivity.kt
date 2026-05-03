package com.waiter.Views

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.waiter.MejaFragment
import com.waiter.PesananFragment
import com.waiter.R
import com.waiter.TabelFragment

class AdminActivity : AppCompatActivity() {

    private lateinit var btnLogout: ImageView
    
    // Variabel untuk item navigasi kustom
    private lateinit var itemWork: LinearLayout
    private lateinit var itemMenu: LinearLayout
    private lateinit var itemTable: LinearLayout
    private lateinit var itemMember: LinearLayout
    
    private lateinit var iconWork: ImageView
    private lateinit var iconMenu: ImageView
    private lateinit var iconTable: ImageView
    private lateinit var iconMember: ImageView
    
    private lateinit var textWork: TextView
    private lateinit var textMenu: TextView
    private lateinit var textTable: TextView
    private lateinit var textMember: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity)
        supportActionBar?.hide()

        // Inisialisasi View
        btnLogout = findViewById(R.id.btnLogout)
        
        itemWork = findViewById(R.id.item_work)
        itemMenu = findViewById(R.id.item_menu)
        itemTable = findViewById(R.id.item_table)
        itemMember = findViewById(R.id.item_member)
        
        iconWork = findViewById(R.id.icon_work)
        iconMenu = findViewById(R.id.icon_menu)
        iconTable = findViewById(R.id.icon_table)
        iconMember = findViewById(R.id.icon_member)
        
        textWork = findViewById(R.id.text_work)
        textMenu = findViewById(R.id.text_menu)
        textTable = findViewById(R.id.text_table)
        textMember = findViewById(R.id.text_member)

        // Set Fragment default saat pertama kali dibuka (Register/Work)
        if (savedInstanceState == null) {
            replaceFragment(RegisterFragment())
            updateNavUI(0)
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Event Klik Navigasi
        itemWork.setOnClickListener {
            replaceFragment(RegisterFragment())
            updateNavUI(0)
        }

        itemMenu.setOnClickListener {
            replaceFragment(PesananFragment())
            updateNavUI(1)
        }

        itemTable.setOnClickListener {
            replaceFragment(MejaFragment())
            updateNavUI(2)
        }

        itemMember.setOnClickListener {
            replaceFragment(TabelFragment())
            updateNavUI(3)
        }
    }

    // Fungsi untuk memperbarui tampilan UI Navigasi (Warna & Teks) mirip Waiter
    private fun updateNavUI(selectedIndex: Int) {
        val navContainer = findViewById<ViewGroup>(R.id.customBottomNav)
        
        val transition = AutoTransition().apply {
            duration = 250
        }
        TransitionManager.beginDelayedTransition(navContainer, transition)

        val items = listOf(itemWork, itemMenu, itemTable, itemMember)
        val icons = listOf(iconWork, iconMenu, iconTable, iconMember)
        val texts = listOf(textWork, textMenu, textTable, textMember)
        
        val colorWhite = ContextCompat.getColor(this, R.color.white)
        val colorUnselected = ContextCompat.getColor(this, R.color.nav_unselected)

        for (i in items.indices) {
            if (i == selectedIndex) {
                // Item Aktif
                items[i].setBackgroundResource(R.drawable.nav_item_active_bg)
                icons[i].imageTintList = ColorStateList.valueOf(colorWhite)
                texts[i].visibility = View.VISIBLE
            } else {
                // Item Tidak Aktif
                items[i].background = null
                icons[i].imageTintList = ColorStateList.valueOf(colorUnselected)
                texts[i].visibility = View.GONE
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
