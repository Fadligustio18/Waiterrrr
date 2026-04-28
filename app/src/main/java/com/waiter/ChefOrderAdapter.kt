package com.waiter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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

class ChefOrderAdapter(
    private var orders: List<OrderListItem>,
    private val orderControllers: OrderControllers,
    private val scope: CoroutineScope,
    private val onStatusUpdate: () -> Unit
) : RecyclerView.Adapter<ChefOrderAdapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTableName: TextView = view.findViewById(R.id.tvTableName)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val rvOrderItems: RecyclerView = view.findViewById(R.id.rvOrderItems)
        val btnPending: Button = view.findViewById(R.id.btnPending) // Tambahkan ini
        val btnCooking: Button = view.findViewById(R.id.btnCooking)
        val btnReady: Button = view.findViewById(R.id.btnReady)
        val btnDrop: ImageView = view.findViewById(R.id.btnDrop)
        val layoutChefHeader: LinearLayout = view.findViewById(R.id.layoutChefHeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chef, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.tvTableName.text = order.locationName
        holder.tvStatus.text = "Status: ${order.statusName}"

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

        holder.layoutChefHeader.setOnClickListener(toggleDropdown)
        holder.btnDrop.setOnClickListener(toggleDropdown)

        // Tombol Tunda (Status ID 1) - Kembali ke antrean
        holder.btnPending.setOnClickListener {
            updateStatus(order.id, 1)
        }

        // Tombol Masak (Status ID 2) - Mulai Memasak
        holder.btnCooking.setOnClickListener {
            updateStatus(order.id, 2)
        }

        // Tombol Siap (Status ID 3) - Selesai Masak & Siap Diantar
        holder.btnReady.setOnClickListener {
            updateStatus(order.id, 3)
        }
    }

    private fun loadOrderDetails(holder: ViewHolder, orderId: Int) {
        scope.launch {
            try {
                val response = orderControllers.getOrderDetailById(orderId)
                if (response.isSuccessful) {
                    val items: List<OrderItemDetail> = response.body()?.items ?: emptyList()
                    withContext(Dispatchers.Main) {
                        holder.rvOrderItems.layoutManager = LinearLayoutManager(holder.itemView.context)
                        holder.rvOrderItems.adapter = ChefOrderItemAdapter(items)
                    }
                }
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    private fun updateStatus(orderId: Int, statusId: Int) {
        scope.launch {
            val response = orderControllers.updateOrderStatus(orderId, statusId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    onStatusUpdate()
                }
            }
        }
    }

    override fun getItemCount() = orders.size

    fun updateData(newOrders: List<OrderListItem>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}

class ChefOrderItemAdapter(private val items: List<OrderItemDetail>) : 
    RecyclerView.Adapter<ChefOrderItemAdapter.ViewHolder>() {
    
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
