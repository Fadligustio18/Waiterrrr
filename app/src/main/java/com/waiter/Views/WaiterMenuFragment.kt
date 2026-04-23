package com.waiter.Views

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.waiter.Models.MejaModel
import com.waiter.Models.MenuResponse
import com.waiter.R
import com.waiter.Services.Client
import com.waiter.WaiterMenuAdapter
import kotlinx.coroutines.launch

class WaiterMenuFragment : Fragment(R.layout.fragment_waiter_menu) {
    private lateinit var rvMenuList: RecyclerView
    private lateinit var adapter: WaiterMenuAdapter
    private lateinit var tvSelectedTable: TextView
    private lateinit var btnSelectTable: Button
    
    private var fullMenuList: List<MenuResponse> = emptyList()
    private var selectedTable: MejaModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMenuList = view.findViewById(R.id.rvMenuList)
        tvSelectedTable = view.findViewById(R.id.tvSelectedTable)
        btnSelectTable = view.findViewById(R.id.btnSelectTable)
        
        adapter = WaiterMenuAdapter(emptyList()) { menu ->
            if (selectedTable == null) {
                Toast.makeText(requireContext(), "Pilih meja terlebih dahulu!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "${menu.name} ditambahkan ke meja ${selectedTable?.name}", Toast.LENGTH_SHORT).show()
            }
        }
        rvMenuList.adapter = adapter

        btnSelectTable.setOnClickListener {
            showTableSelectionDialog()
        }

        loadMenu()
    }

    private fun showTableSelectionDialog() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Ambil data meja dari API (sama seperti yang dipakai di Admin)
                val response = Client.meja.getMeja()
                if (response.isSuccessful) {
                    val tables = response.body() ?: emptyList()
                    val tableNames = tables.map { "Meja ${it.name}" }.toTypedArray()

                    AlertDialog.Builder(requireContext())
                        .setTitle("Pilih Meja")
                        .setItems(tableNames) { _, which ->
                            selectedTable = tables[which]
                            tvSelectedTable.text = "Meja: ${selectedTable?.name}"
                            Toast.makeText(requireContext(), "Meja ${selectedTable?.name} dipilih", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil data meja", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMenu() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = Client.menu.getMenu()
                if (response.isSuccessful) {
                    fullMenuList = response.body() ?: emptyList()
                    adapter.updateData(fullMenuList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}