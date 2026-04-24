package com.waiter.Views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waiter.R
import com.waiter.TableOrderAdapter
import com.waiter.ViewModels.CartViewModel

class CartFragment : Fragment(R.layout.fragment_cart) {
    
    private val cartViewModel: CartViewModel by activityViewModels()
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
                // Logika checkout per meja
                Toast.makeText(requireContext(), "Checkout Meja ${tableOrder.table.name}", Toast.LENGTH_SHORT).show()
                // cartViewModel.clearCartForTable(tableOrder.table.id)
            }
        )
        
        rvCart.adapter = tableOrderAdapter
        
        cartViewModel.allTableOrders.observe(viewLifecycleOwner) { orders ->
            tableOrderAdapter.updateData(orders)
        }
    }
}