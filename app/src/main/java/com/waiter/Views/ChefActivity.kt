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
import com.waiter.ChefOrderAdapter
import com.waiter.Controllers.OrderControllers
import com.waiter.Models.OrderListItem
import kotlinx.coroutines.launch

class ChefActivity : AppCompatActivity() {
    private lateinit var btnLogout: ImageView
    private lateinit var rvChefOrders: RecyclerView
    private lateinit var chefOrderAdapter: ChefOrderAdapter
    private val orderControllers = OrderControllers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chef_activity)
        supportActionBar?.hide()

        rvChefOrders = findViewById(R.id.rvChefOrders)
        rvChefOrders.layoutManager = LinearLayoutManager(this)
        
        chefOrderAdapter = ChefOrderAdapter(
            orders = emptyList(),
            orderControllers = orderControllers,
            scope = lifecycleScope,
            onStatusUpdate = {
                fetchOrders() // Refresh data saat status diubah
            }
        )
        rvChefOrders.adapter = chefOrderAdapter

        fetchOrders()

        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            Toast.makeText(this, "LogOut Berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    // D:/App_Projek/waiterrrrrrrrr/app/src/main/java/com/waiter/Views/ChefActivity.kt

    // D:/App_Projek/waiterrrrrrrrr/app/src/main/java/com/waiter/Views/ChefActivity.kt

    private fun fetchOrders() {
        lifecycleScope.launch {
            try {
                // Ambil data Pending (1)
                val respPending = orderControllers.getOrdersByStatus(1)
                // Ambil data Masak (2)
                val respCooking = orderControllers.getOrdersByStatus(2)

                val allOrders = mutableListOf<OrderListItem>()

                if (respPending.isSuccessful) respPending.body()?.let { allOrders.addAll(it) }
                if (respCooking.isSuccessful) respCooking.body()?.let { allOrders.addAll(it) }

                // Tampilkan gabungan status 1 dan 2
                chefOrderAdapter.updateData(allOrders.sortedBy { it.id })

            } catch (e: Exception) {
                Toast.makeText(this@ChefActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}