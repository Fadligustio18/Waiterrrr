package com.waiter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.waiter.Models.MejaModel

import androidx.lifecycle.lifecycleScope
import com.waiter.Services.Client
import kotlinx.coroutines.launch

class MejaFragment : Fragment(R.layout.fragment_meja) {

    private val tableList = mutableListOf<MejaModel>()
    private lateinit var tableAdapter: TabelAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTableNumber = view.findViewById<TextInputEditText>(R.id.etTableNumber)
        val btnAddTable = view.findViewById<Button>(R.id.btnAddTable)
        val rvTableList = view.findViewById<RecyclerView>(R.id.rvTableList)

        // Setup RecyclerView
        tableAdapter = TabelAdapter(
            tableList,
            onUpdateClick = { position ->
                showUpdateDialog(position)
            },
            onDeleteClick = { position ->
                val table = tableList[position]
                deleteMejaApi(table.id, position)
            }
        )
        rvTableList.adapter = tableAdapter

        // Load data meja saat fragment dibuka
        loadMejaData()

        btnAddTable.setOnClickListener {
            val name = etTableNumber.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Masukkan nama/nomor meja", Toast.LENGTH_SHORT).show()
            } else {
                val newMeja = MejaModel(id = 0, name = name)
                createMejaApi(newMeja)
                etTableNumber.text?.clear()
            }
        }
    }

    private fun loadMejaData() {
        lifecycleScope.launch {
            try {
                val response = Client.meja.getMeja()
                if (response.isSuccessful) {
                    tableList.clear()
                    response.body()?.let { tableList.addAll(it) }
                    tableAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal load data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createMejaApi(meja: MejaModel) {
        lifecycleScope.launch {
            try {
                val response = Client.meja.createMeja(meja)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Meja berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    loadMejaData()
                } else {
                    // Ini akan menampilkan pesan error dari server (misal: "Nomor meja sudah ada")
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(requireContext(), "Gagal: $errorMsg", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Ini akan muncul jika tidak bisa konek ke server sama sekali
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun deleteMejaApi(id: Int, position: Int) {
        lifecycleScope.launch {
            try {
                val response = Client.meja.deleteMeja(id)
                if (response.isSuccessful) {
                    tableList.removeAt(position)
                    tableAdapter.notifyItemRemoved(position)
                    Toast.makeText(requireContext(), "Berhasil hapus", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal hapus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUpdateDialog(position: Int) {
        val table = tableList[position]
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Nomor Meja")

        val input = EditText(requireContext())
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER


        val container = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(48, 20, 48, 0)
        input.layoutParams = params
        container.addView(input)

        builder.setView(container)


    }
}