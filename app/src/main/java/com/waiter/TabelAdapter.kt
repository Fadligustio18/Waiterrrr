package com.waiter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waiter.Models.MejaModel

class TabelAdapter(
    private val tableList: MutableList<MejaModel>,
    private val onUpdateClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TabelAdapter.TableViewHolder>() {

    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTableNumber: TextView = view.findViewById(R.id.tvTableNumber)

        val btnDeleteTable: ImageView = view.findViewById(R.id.btnDeleteTable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meja, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val table = tableList[position]
        holder.tvTableNumber.text = "Meja ${table.name}"



        holder.btnDeleteTable.setOnClickListener {
            onDeleteClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = tableList.size
}