package com.waiter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waiter.Services.Client
import com.waiter.Models.MenuResponse

class WaiterMenuAdapter(
    private var menuList: List<MenuResponse>,
    private val onAddClick: (MenuResponse) -> Unit
) : RecyclerView.Adapter<WaiterMenuAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMenuImage: ImageView = view.findViewById(R.id.ivMenuImage)
        val tvMenuName: TextView = view.findViewById(R.id.tvMenuName)
        val tvMenuPrice: TextView = view.findViewById(R.id.tvMenuPrice)
        val btnAddOrder: View = view.findViewById(R.id.btnAddOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_waiter_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menuList[position]
        holder.tvMenuName.text = menu.name
        holder.tvMenuPrice.text = "Rp ${menu.price}"

        val fullImageUrl = "${Client.BASE_URL}${menu.imageUrl?.removePrefix("/")}"
        Glide.with(holder.itemView.context)
            .load(fullImageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.ivMenuImage)

        holder.btnAddOrder.setOnClickListener {
            onAddClick(menu)
        }
    }

    override fun getItemCount(): Int = menuList.size

    fun updateData(newList: List<MenuResponse>) {
        menuList = newList
        notifyDataSetChanged()
    }
}