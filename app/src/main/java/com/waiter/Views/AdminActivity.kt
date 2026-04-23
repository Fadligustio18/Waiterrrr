package com.waiter.Views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.waiter.Controllers.AuthControllers
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.waiter.Models.UserRequest
import com.waiter.PesananFragment
import com.waiter.MejaFragment
import com.waiter.R
import com.waiter.TabelFragment
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {
    private val authController = AuthControllers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity)
        supportActionBar?.hide()

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val mainLayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.mainLayout)

        val etName = findViewById<TextInputEditText>(R.id.editTextText)
        val etPassword = findViewById<TextInputEditText>(R.id.editTextText2)
        val rgRole = findViewById<RadioGroup>(R.id.radioGroupRole)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val name = etName.text.toString()
            val password = etPassword.text.toString()
            val selectedRoleId = when (rgRole.checkedRadioButtonId) {
                R.id.radioButton5 -> 2 // Waiter
                R.id.radioButton6 -> 4 // Chef
                R.id.radioButton7 -> 3 // Cashier
                else -> 0
            }

            // Tambahkan log untuk debug
            android.util.Log.d("ADMIN_LOG", "Selected ID: $selectedRoleId")

            if (name.isEmpty() || password.isEmpty() || selectedRoleId == 0) {
                Toast.makeText(this, "Harap isi semua data (ID Role: $selectedRoleId)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userRequest = UserRequest(name, password, selectedRoleId)

            lifecycleScope.launch {
                val success = authController.registerController(userRequest)
                if (success) {
                    Toast.makeText(this@AdminActivity, "User berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    etName.text?.clear()
                    etPassword.text?.clear()
                    rgRole.clearCheck()
                } else {
                    Toast.makeText(this@AdminActivity, "Gagal menambahkan user", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        
        // Navigation Drawer Listener
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_table -> {
                    mainLayout.visibility = View.GONE
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TabelFragment()).commit()
                }
                R.id.nav_form -> {
                    mainLayout.visibility = View.VISIBLE
                    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if (fragment != null) {
                        supportFragmentManager.beginTransaction().remove(fragment).commit()
                    }
                }
                R.id.nav_logout -> {
                    Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Bottom Navigation Listener
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_work -> {
                    // Tampilkan form input (Daftar)
                    mainLayout.visibility = View.VISIBLE
                    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if (fragment != null) {
                        supportFragmentManager.beginTransaction().remove(fragment).commit()
                    }
                }
                R.id.nav_menu -> {
                    // Tampilkan halaman Pesanan
                    mainLayout.visibility = View.GONE
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PesananFragment()).commit()
                }
                R.id.nav_table -> {
                    // Tampilkan halaman Meja
                    mainLayout.visibility = View.GONE
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MejaFragment()).commit()
                }
            }
            true
        }
    }
}