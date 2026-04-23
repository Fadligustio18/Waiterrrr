package com.waiter


import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.waiter.Controllers.WorkerControllers
import com.waiter.Models.Worker
import kotlinx.coroutines.launch


class TabelFragment: Fragment(R.layout.fragment_tabel) {
    private lateinit var tableData: TableLayout
    private val ctrlr = WorkerControllers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            activity?.findViewById<View>(R.id.mainLayout)?.visibility = View.VISIBLE
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        tableData = view.findViewById(R.id.tableData)
        loadData()
    }

    private fun loadData() {
        tableData.removeAllViews()
        lifecycleScope.launch {
            val workers = ctrlr.GetWorker()
            if (workers != null) {
                for (worker in workers) {
                    addTableRow(worker)
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTableRow(worker: Worker) {
        val row = TableRow(requireContext())
        fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
        fun createCellParams(widthDp: Int) = TableRow.LayoutParams(dpToPx(widthDp), TableRow.LayoutParams.WRAP_CONTENT)

        val tvId = createTextView(worker.id.toString(), 60)
        val tvName = createTextView(worker.name, 150)
        val tvPass = createTextView(worker.password, 150)
        val tvRole = createTextView(worker.roleName, 120)

        // Tombol Edit
        val btnUpdate = createTextView("Edit", 100, android.R.color.holo_blue_dark)
        btnUpdate.setOnClickListener { showUpdateDialog(worker) }

        // Tombol Hapus
        val btnDelete = createTextView("Hapus", 100, android.R.color.holo_red_dark)
        btnDelete.setOnClickListener { showDeleteConfirmation(worker) }

        row.addView(tvId)
        row.addView(tvName)
        row.addView(tvPass)
        row.addView(tvRole)
        row.addView(btnUpdate)
        row.addView(btnDelete)

        tableData.addView(row)
    }

    private fun createTextView(text: String, widthDp: Int, colorRes: Int? = null): TextView {
        val tv = TextView(requireContext())
        tv.text = text
        tv.setPadding(12, 12, 12, 12)
        tv.setBackgroundResource(R.drawable.shape_habib)
        tv.gravity = android.view.Gravity.CENTER
        fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
        tv.layoutParams = TableRow.LayoutParams(dpToPx(widthDp), TableRow.LayoutParams.WRAP_CONTENT)
        colorRes?.let { tv.setTextColor(requireContext().getColor(it)) }
        return tv
    }

    private fun showDeleteConfirmation(worker: Worker) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Worker")
            .setMessage("Apakah Anda yakin ingin menghapus ${worker.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    val success = ctrlr.deleteWorker(worker.id)
                    if (success) {
                        Toast.makeText(requireContext(), "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                        loadData()
                    } else {
                        Toast.makeText(requireContext(), "Gagal menghapus", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showUpdateDialog(worker: Worker) {
        val context = requireContext()
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 20, 40, 20)

        val etName = EditText(context).apply { 
            hint = "Nama"
            setText(worker.name) 
        }
        val etPass = EditText(context).apply { 
            hint = "Password"
            setText(worker.password) 
        }
        val etRoleId = EditText(context).apply { 
            hint = "Role ID (1: Admin, 2: Waiter, etc)"
            setText(worker.roleId.toString())
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        // roleName biasanya ditentukan oleh roleId di backend, tapi kita kirim saja sesuai model
        val etRoleName = EditText(context).apply { 
            hint = "Role Name"
            setText(worker.roleName)
        }

        layout.addView(etName)
        layout.addView(etPass)
        layout.addView(etRoleId)
        layout.addView(etRoleName)

        AlertDialog.Builder(context)
            .setTitle("Update Worker")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                val updatedWorker = Worker(
                    id = worker.id,
                    name = etName.text.toString(),
                    password = etPass.text.toString(),
                    roleId = etRoleId.text.toString().toIntOrNull() ?: worker.roleId,
                    roleName = etRoleName.text.toString()
                )
                lifecycleScope.launch {
                    val success = ctrlr.updateWorker(worker.id, updatedWorker)
                    if (success) {
                        Toast.makeText(context, "Berhasil diupdate", Toast.LENGTH_SHORT).show()
                        loadData()
                    } else {
                        Toast.makeText(context, "Gagal update", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}