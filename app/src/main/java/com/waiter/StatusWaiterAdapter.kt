package com.waiter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waiter.Models.OrderListItem
import com.waiter.Models.OrderItemDetail
import com.waiter.Controllers.OrderControllers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusWaiterAdapter(
    private var orders: List<OrderListItem>,
    private val orderControllers: OrderControllers,
    private val scope: CoroutineScope
) : RecyclerView.Adapter<StatusWaiterAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTableName: TextView = view.findViewById(R.id.tvTableName)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val rvOrderItems: RecyclerView = view.findViewById(R.id.rvOrderItems)
        val btnDrop: ImageView = view.findViewById(R.id.btnDrop)
        val btnServed: View = view.findViewById(R.id.btnServed)
        val layoutStatusHeader: View = view.findViewById(R.id.layoutStatusHeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_status_waiter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        // Ganti tableName menjadi locationName
        holder.tvTableName.text = order.locationName
        holder.tvStatus.text = "Status: ${order.statusName}"

        // Sembunyikan tombol Selesai jika status belum "Ready" (3)
        if (order.statusId == 3) {
            holder.btnServed.visibility = View.VISIBLE
        } else {
            holder.btnServed.visibility = View.GONE
        }

        holder.rvOrderItems.visibility = View.GONE

        val toggleDropdown = View.OnClickListener {
            if (holder.rvOrderItems.visibility == View.GONE) {
                holder.rvOrderItems.visibility = View.VISIBLE
                holder.btnDrop.rotation = 180f
                loadOrderDetails(holder, order.id)
            } else {
                holder.rvOrderItems.visibility = View.GONE
                holder.btnDrop.rotation = 0f
            }
        }

        holder.layoutStatusHeader.setOnClickListener(toggleDropdown)
        holder.btnDrop.setOnClickListener(toggleDropdown)

        // Tombol Selesai: Mengubah status menjadi 4 (Served/Delivered)
        holder.btnServed.setOnClickListener {
            updateStatus(order.id, 4)
        }
    }

    private fun updateStatus(orderId: Int, statusId: Int) {
        scope.launch {
            try {
                val response = orderControllers.updateOrderStatus(orderId, statusId)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        orders = orders.filter { it.id != orderId }
                        notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) { }
        }
    }

    private fun loadOrderDetails(holder: ViewHolder, orderId: Int) {
        scope.launch {
            try {
                // Ganti getOrderById menjadi getOrderDetailById
                val response = orderControllers.getOrderDetailById(orderId)
                if (response.isSuccessful) {
                    val items: List<OrderItemDetail> = response.body()?.items ?: emptyList()
                    withContext(Dispatchers.Main) {
                        holder.rvOrderItems.layoutManager = LinearLayoutManager(holder.itemView.context)
                        holder.rvOrderItems.adapter = StatusOrderItemAdapter(items)
                    }
                }
            } catch (e: Exception) { }
        }
    }

    override fun getItemCount() = orders.size

    fun updateData(newOrders: List<OrderListItem>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}

class StatusOrderItemAdapter(private val items: List<OrderItemDetail>) : 
    RecyclerView.Adapter<StatusOrderItemAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text = view.findViewById<TextView>(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.text.text = "${item.menuName} x${item.quantity}"
    }

    override fun getItemCount() = items.size
}
