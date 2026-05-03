package com.waiter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waiter.Models.Menu

class MenuAdapter(
    private val menuList: MutableList<Menu>,
    private val onDeleteClick: (Int) -> Unit,
    private val onEditClick: (Menu) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItemImage: ImageView = view.findViewById(R.id.ivItemImage)
        val tvItemName: TextView = view.findViewById(R.id.tvItemName)
        val tvItemCategory: TextView = view.findViewById(R.id.tvItemCategory)
        val tvItemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val btnDeleteItem: ImageView = view.findViewById(R.id.btnDeleteItem)
        val btnEdit: ImageView = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]
        holder.tvItemName.text = menu.name
        holder.tvItemCategory.text = menu.category
        holder.tvItemPrice.text = "Rp ${menu.price}"
        
        if (menu.imageUri != null) {
            holder.ivItemImage.setImageURI(menu.imageUri)
        } else if (!menu.imageUrl.isNullOrEmpty()) {
            val fullImageUrl = "http://192.168.1.7:3000${menu.imageUrl}"
            Glide.with(holder.itemView.context)
                .load(fullImageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivItemImage)
        } else {
            holder.ivItemImage.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.btnDeleteItem.setOnClickListener {
            onDeleteClick(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            onEditClick(menu)
        }
    }

    override fun getItemCount(): Int = menuList.size
}