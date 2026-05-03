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
import com.waiter.Models.CartItem
import com.waiter.Models.TableOrder
import com.ncorti.slidetoact.SlideToActView
import java.text.NumberFormat
import java.util.Locale

class TableOrderAdapter(
    private var tableOrders: List<TableOrder>,
    private val onQuantityChanged: (tableId: Int, item: CartItem, delta: Int) -> Unit,
    private val onDelete: (tableId: Int, item: CartItem) -> Unit,
    private val onCheckout: (TableOrder) -> Unit
) : RecyclerView.Adapter<TableOrderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTableName: TextView = view.findViewById(R.id.tvTableName)
        val ivExpand: ImageView = view.findViewById(R.id.ivExpand)
        val rvItems: RecyclerView = view.findViewById(R.id.rvItems)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
        val btnCheckout: SlideToActView = view.findViewById(R.id.btnCheckout)
        val layoutTableHeader: LinearLayout = view.findViewById(R.id.layoutTableHeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tableOrder = tableOrders[position]
        holder.tvTableName.text = "Meja ${tableOrder.table.name}"
        
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        holder.tvTotal.text = "Total: ${formatter.format(tableOrder.totalPrice).replace("Rp", "Rp ")}"

        // Setup Nested RecyclerView for items
        val itemAdapter = CartAdapter(
            items = tableOrder.items,
            onQuantityChanged = { item, delta -> 
                onQuantityChanged(tableOrder.table.id, item, delta) 
            },
            onDelete = { item -> 
                onDelete(tableOrder.table.id, item) 
            }
        )
        holder.rvItems.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvItems.adapter = itemAdapter

        // Handle Expand/Collapse
        holder.rvItems.visibility = if (tableOrder.isExpanded) View.VISIBLE else View.GONE
        holder.ivExpand.rotation = if (tableOrder.isExpanded) 180f else 0f

        holder.layoutTableHeader.setOnClickListener {
            tableOrder.isExpanded = !tableOrder.isExpanded
            notifyItemChanged(position)
        }

        // Reset slider ke posisi awal agar bisa digeser lagi nanti
        holder.btnCheckout.setCompleted(false, false)
        
        holder.btnCheckout.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                onCheckout(tableOrder)
                // Kembalikan slider ke posisi awal setelah checkout diproses
                view.setCompleted(false, true)
            }
        }
    }

    override fun getItemCount() = tableOrders.size

    fun updateData(newOrders: List<TableOrder>) {
        tableOrders = newOrders
        notifyDataSetChanged()
    }
}