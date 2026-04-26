package com.waiter.Views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waiter.R

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.waiter.Controllers.OrderControllers
import com.waiter.Models.OrderListItem
import com.waiter.StatusWaiterAdapter
import kotlinx.coroutines.launch

class StatusFragment : Fragment(R.layout.fragment_status) {

    private lateinit var rvStatus: RecyclerView
    private lateinit var statusAdapter: StatusWaiterAdapter
    private val orderControllers = OrderControllers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvStatus = view.findViewById(R.id.rvStatus)
        rvStatus.layoutManager = LinearLayoutManager(requireContext())
        
        statusAdapter = StatusWaiterAdapter(
            orders = emptyList(),
            orderControllers = orderControllers,
            scope = viewLifecycleOwner.lifecycleScope
        )
        rvStatus.adapter = statusAdapter

        fetchOrderStatus()
    }

    private fun fetchOrderStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Ambil status 1 (Pending), 2 (Cooking), dan 3 (Ready) untuk dipantau Waiter
                val resp1 = orderControllers.getOrdersByStatus(1)
                val resp2 = orderControllers.getOrdersByStatus(2)
                val resp3 = orderControllers.getOrdersByStatus(3)

                val allOrders = mutableListOf<OrderListItem>()
                if (resp1.isSuccessful) resp1.body()?.let { allOrders.addAll(it) }
                if (resp2.isSuccessful) resp2.body()?.let { allOrders.addAll(it) }
                if (resp3.isSuccessful) resp3.body()?.let { allOrders.addAll(it) }

                statusAdapter.updateData(allOrders.sortedByDescending { it.id })
                
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}