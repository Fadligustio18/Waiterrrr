package com.waiter.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.waiter.Models.Account
import com.waiter.Models.Login
import com.waiter.R
import com.waiter.Controllers.AuthControllers
import kotlinx.coroutines.launch

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstance: Bundle?){
        super.onCreate(savedInstance)
        setContentView(R.layout.login_view)
        val authController = AuthControllers();
        val loginName = findViewById<EditText>(R.id.LoginName)
        val loginPassword = findViewById<EditText>(R.id.LoginPassword)
        val loginButton = findViewById<Button>(R.id.LoginButton)
        val debugLogin = findViewById<TextView>(R.id.debugLogin)




        loginButton.setOnClickListener {
            val nameText = loginName.text.toString()
            val passwordText = loginPassword.text.toString()

            if(nameText.isEmpty() || passwordText.isEmpty()){
                Toast.makeText(this@LoginActivity, "Please Fill The Form", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val data = Login(
                    Name = nameText,
                    Password = passwordText
                )
                val response = authController.loginController(data)
                
                if (response != null) {
                    when (response.Role) {
                        "Waiter" -> {
                            startActivity(Intent(this@LoginActivity, WaiterActivity::class.java))
                            finish()
                        }
                        "Admin" -> {
                            startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                            finish()
                        }
                        "Chef" -> {
                            startActivity(Intent(this@LoginActivity, ChefActivity::class.java))
                            finish()
                        }
                        "Cashier" -> {
                            startActivity(Intent(this@LoginActivity, CashierActivity::class.java))
                            finish()
                        }
                        else -> {
                            Toast.makeText(this@LoginActivity, "Role tidak dikenali: ${response.Role}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Jika response null, berarti bisa jadi 401 atau error jaringan
                    Toast.makeText(this@LoginActivity, "Login Gagal (Cek Username/Password atau Koneksi)", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}