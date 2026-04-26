package com.waiter.Views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.waiter.R

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waiter.CashierOrderAdapter
import com.waiter.Controllers.OrderControllers
import kotlinx.coroutines.launch

class CashierActivity : AppCompatActivity() {
    private lateinit var btnLogout: ImageView
    private lateinit var rvCashierOrders: RecyclerView
    private lateinit var cashierAdapter: CashierOrderAdapter
    private val orderControllers = OrderControllers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cashier_activity)
        supportActionBar?.hide()

        rvCashierOrders = findViewById(R.id.rvCashierOrders)
        rvCashierOrders.layoutManager = LinearLayoutManager(this)

        cashierAdapter = CashierOrderAdapter(
            orders = emptyList(),
            orderControllers = orderControllers,
            scope = lifecycleScope,
            onPaymentDone = {
                fetchServedOrders()
            }
        )
        rvCashierOrders.adapter = cashierAdapter

        fetchServedOrders()

        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            Toast.makeText(this, "LogOut Berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun fetchServedOrders() {
        lifecycleScope.launch {
            try {
                // Kasir memantau status 4 (Served/Diantar)
                val response = orderControllers.getOrdersByStatus(4)
                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    cashierAdapter.updateData(orders)
                }
            } catch (e: Exception) {
                Toast.makeText(this@CashierActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}