package com.waiter.Views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.waiter.ChefOrderAdapter
import com.waiter.Controllers.OrderControllers
import com.waiter.Models.OrderListItem
import com.waiter.R
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
                fetchOrders()
            }
        )
        rvChefOrders.adapter = chefOrderAdapter

        fetchOrders()

        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }


    private fun fetchOrders() {
        lifecycleScope.launch {
            try {
                Log.d("CHEF_DEBUG", "Fetching orders...")
                
                // Status 1: Pending, Status 2: Cooking
                // Sesuai Swagger baru, tidak perlu parameter id (0) lagi
                val respPending = orderControllers.getOrdersByStatus(1)
                val respCooking = orderControllers.getOrdersByStatus(2)


                val allOrders = mutableListOf<OrderListItem>()

                if (respPending.isSuccessful) {
                    respPending.body()?.let { 
                        Log.d("CHEF_DEBUG", "Found ${it.size} pending orders")
                        allOrders.addAll(it) 
                    }
                } else {
                    Log.e("CHEF_DEBUG", "Failed to fetch pending: ${respPending.errorBody()?.string()}")
                }

                if (respCooking.isSuccessful) {
                    respCooking.body()?.let { 
                        Log.d("CHEF_DEBUG", "Found ${it.size} cooking orders")
                        allOrders.addAll(it) 
                    }
                }

                // Update UI
                chefOrderAdapter.updateData(allOrders.sortedBy { it.id })
                
                if (allOrders.isEmpty()) {
                    Log.w("CHEF_DEBUG", "No orders found for status 1 or 2")
                }

            } catch (e: Exception) {
                Log.e("CHEF_DEBUG", "Exception: ${e.message}")
                Toast.makeText(this@ChefActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
