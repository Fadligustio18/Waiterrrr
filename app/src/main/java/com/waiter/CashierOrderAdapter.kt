package com.waiter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waiter.Models.OrderListItem
import com.waiter.Models.OrderItemDetail
import com.waiter.Controllers.OrderControllers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

class CashierOrderAdapter(
    private var orders: List<OrderListItem>,
    private val orderControllers: OrderControllers,
    private val scope: CoroutineScope,
    private val onPaymentDone: () -> Unit
) : RecyclerView.Adapter<CashierOrderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTableName: TextView = view.findViewById(R.id.tvTableName)
        val tvTotalItems: TextView = view.findViewById(R.id.tvTotalItems)
        val tvTotalPrice: TextView = view.findViewById(R.id.tvTotalPrice)
        val rvOrderItems: RecyclerView = view.findViewById(R.id.rvOrderItems)
        val btnDone: Button = view.findViewById(R.id.btnDone)
        val btnDrop: ImageView = view.findViewById(R.id.btnDrop)
        val layoutCashierHeader: View = view.findViewById(R.id.layoutCashierHeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_status_cashier, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.tvTableName.text = order.tableName
        
        // Sembunyikan daftar item di awal
        holder.rvOrderItems.visibility = View.GONE

        // Load detail untuk menghitung total
        scope.launch {
            try {
                val response = orderControllers.getOrderById(order.id)
                if (response.isSuccessful) {
                    val items = response.body()?.items ?: emptyList()
                    val totalItem = items.sumOf { it.quantity }
                    val totalPrice = items.sumOf { it.priceAtOrder * it.quantity }

                    withContext(Dispatchers.Main) {
                        holder.tvTotalItems.text = "Total Item: $totalItem"
                        holder.tvTotalPrice.text = "Total Harga: ${formatRupiah(totalPrice)}"
                        
                        holder.rvOrderItems.layoutManager = LinearLayoutManager(holder.itemView.context)
                        holder.rvOrderItems.adapter = CashierOrderItemAdapter(items)
                    }
                }
            } catch (e: Exception) { }
        }

        val toggleDropdown = View.OnClickListener {
            if (holder.rvOrderItems.visibility == View.GONE) {
                holder.rvOrderItems.visibility = View.VISIBLE
                holder.btnDrop.rotation = 180f
            } else {
                holder.rvOrderItems.visibility = View.GONE
                holder.btnDrop.rotation = 0f
            }
        }

        holder.layoutCashierHeader.setOnClickListener(toggleDropdown)
        holder.btnDrop.setOnClickListener(toggleDropdown)

        // Tombol Selesai Pembayaran (Status ID 5 atau selesaikan order)
        holder.btnDone.setOnClickListener {
            processPayment(order.id, holder.itemView)
        }
    }

    private fun processPayment(orderId: Int, view: View) {
        scope.launch {
            try {
                // Update status ke 5 (Selesai/Lunas)
                val response = orderControllers.updateOrderStatus(orderId, 5)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(view.context, "Done! Terimakasih", Toast.LENGTH_LONG).show()
                        onPaymentDone()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(view.context, "Gagal memproses pembayaran", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(number.toDouble())
    }

    override fun getItemCount() = orders.size

    fun updateData(newOrders: List<OrderListItem>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}

class CashierOrderItemAdapter(private val items: List<OrderItemDetail>) : 
    RecyclerView.Adapter<CashierOrderItemAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text = view.findViewById<TextView>(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.text.text = "${item.menuName} (${item.quantity}x) - Rp ${item.priceAtOrder * item.quantity}"
    }

    override fun getItemCount() = items.size
}
