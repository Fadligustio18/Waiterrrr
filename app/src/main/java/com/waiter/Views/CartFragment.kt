package com.waiter.Views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waiter.Controllers.OrderControllers
import com.waiter.Models.OrderDetails
import com.waiter.Models.OrderItemDetails
import com.waiter.Models.OrderRequest
import com.waiter.R
import com.waiter.TableOrderAdapter
import com.waiter.ViewModels.CartViewModel
import kotlinx.coroutines.launch

class CartFragment : Fragment(R.layout.fragment_cart) {
    
    private val cartViewModel: CartViewModel by activityViewModels()
    private val orderControllers = OrderControllers()
    private lateinit var tableOrderAdapter: TableOrderAdapter
    private lateinit var rvCart: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        rvCart = view.findViewById(R.id.rvCart)
        rvCart.layoutManager = LinearLayoutManager(requireContext())
        
        tableOrderAdapter = TableOrderAdapter(
            tableOrders = emptyList(),
            onQuantityChanged = { tableId, item, delta ->
                cartViewModel.updateQuantity(tableId, item, delta)
            },
            onDelete = { tableId, item ->
                cartViewModel.removeFromCart(tableId, item)
            },
            onCheckout = { tableOrder ->
                val orderItems = tableOrder.items.map { 
                    OrderItemDetails(
                        menuId = it.menu.id,
                        quantity = it.quantity,
                        priceAtOrder = it.menu.price,
                        menuName = it.menu.name
                    )
                }
                
                val orderRequest = OrderRequest(
                    order = OrderDetails(
                        userId = 1, // IDEALNYA: Ambil dari Session/SharedPreferences
                        customerName = "Pelanggan Meja ${tableOrder.table.name}",
                        statusId = 1, // Status 1 = Pending (untuk Chef)
                        locationId = tableOrder.table.id
                    ),
                    items = orderItems
                )

                lifecycleScope.launch {
                    try {
                        val response = orderControllers.createOrder(orderRequest)
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Pesanan dikirim ke Chef!", Toast.LENGTH_SHORT).show()
                            cartViewModel.clearCartForTable(tableOrder.table.id)
                        } else {
                            Toast.makeText(requireContext(), "Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
        
        rvCart.adapter = tableOrderAdapter
        
        cartViewModel.allTableOrders.observe(viewLifecycleOwner) { orders ->
            tableOrderAdapter.updateData(orders)
        }
    }
}