package com.waiter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.waiter.Controllers.MenuControllers
import com.waiter.Models.Menu
import kotlinx.coroutines.launch

class PesananFragment : Fragment(R.layout.fragment_pesanan) {

    private var selectedImageUri: Uri? = null
    private lateinit var ivMenuImage: ImageView
    private lateinit var layoutPlaceholder: LinearLayout
    private val menuList = mutableListOf<Menu>()
    private lateinit var menuAdapter: MenuAdapter
    private val menuController = MenuControllers()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            ivMenuImage.setImageURI(it)
            layoutPlaceholder.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivMenuImage = view.findViewById(R.id.ivMenuImage)
        layoutPlaceholder = view.findViewById(R.id.layoutPlaceholder)
        val etMenuName = view.findViewById<TextInputEditText>(R.id.etMenuName)
        val etMenuPrice = view.findViewById<TextInputEditText>(R.id.etMenuPrice)
        val rgCategory = view.findViewById<android.widget.RadioGroup>(R.id.rgCategory)
        val btnSaveMenu = view.findViewById<Button>(R.id.btnSaveMenu)
        val rvMenuList = view.findViewById<RecyclerView>(R.id.rvMenuList)

        // Setup RecyclerView
        menuAdapter = MenuAdapter(menuList, { position ->
            val menuToDelete = menuList[position]
            
            // Konversi ID String ke Int untuk API
            val menuId = menuToDelete.id.toIntOrNull()
            
            if (menuId != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val success = menuController.deleteMenu(menuId)
                    if (success == true) {
                        menuList.removeAt(position)
                        menuAdapter.notifyItemRemoved(position)
                        Toast.makeText(requireContext(), "${menuToDelete.name} berhasil dihapus dari server", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Gagal menghapus ${menuToDelete.name} dari server", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Jika ID bukan angka (misal data dummy), hapus lokal saja
                menuList.removeAt(position)
                menuAdapter.notifyItemRemoved(position)
                Toast.makeText(requireContext(), "${menuToDelete.name} dihapus lokal", Toast.LENGTH_SHORT).show()
            }
        }, { menu ->
            showEditDialog(menu)
        })
        rvMenuList.adapter = menuAdapter

        // Tambahkan fungsi load data dari API
        loadMenuFromServer()

        view.findViewById<View>(R.id.cardImage).setOnClickListener {
            getContent.launch("image/*")
        }

        btnSaveMenu.setOnClickListener {
            val name = etMenuName.text.toString()
            val price = etMenuPrice.text.toString()
            val (typeId, typeName) = when (rgCategory.checkedRadioButtonId) {
                R.id.rbFood -> "1" to "Makanan"
                R.id.rbDrink -> "2" to "Minuman"
                else -> "" to ""
            }

            if (name.isEmpty() || price.isEmpty() || selectedImageUri == null || typeName.isEmpty()) {
                Toast.makeText(requireContext(), "Harap lengkapi semua data dan foto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jalankan di Coroutine
            viewLifecycleOwner.lifecycleScope.launch {
                val createdMenu = menuController.createMenu(
                    requireContext(),
                    name,
                    price,
                    typeId,
                    typeName,
                    selectedImageUri!!
                )

                if (createdMenu != null) {
                    // Tambah ke list lokal menggunakan ID ASLI dari server
                    val newMenu = Menu(
                        id = createdMenu.id.toString(),
                        name = createdMenu.name,
                        price = createdMenu.price.toString(),
                        category = createdMenu.typeName ?: typeName,
                        imageUrl = createdMenu.imageUrl,
                        imageUri = selectedImageUri
                    )
                    menuList.add(0, newMenu)
                    menuAdapter.notifyItemInserted(0)
                    rvMenuList.scrollToPosition(0)

                    Toast.makeText(requireContext(), "Menu $name berhasil disimpan ke server", Toast.LENGTH_SHORT).show()

                    // Reset form
                    etMenuName.text?.clear()
                    etMenuPrice.text?.clear()
                    rgCategory.check(R.id.rbFood)
                    ivMenuImage.setImageDrawable(null)
                    layoutPlaceholder.visibility = View.VISIBLE
                    selectedImageUri = null
                } else {
                    Toast.makeText(requireContext(), "Gagal menyimpan menu ke server (Cek format data)", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showEditDialog(menu: Menu) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_menu, null)
        val etName = dialogView.findViewById<TextInputEditText>(R.id.etEditName)
        val etPrice = dialogView.findViewById<TextInputEditText>(R.id.etEditPrice)

        etName.setText(menu.name)
        etPrice.setText(menu.price)

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Edit Menu")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = etName.text.toString()
                val newPrice = etPrice.text.toString()

                if (newName.isNotEmpty() && newPrice.isNotEmpty()) {
                    updateMenu(menu, newName, newPrice)
                } else {
                    Toast.makeText(requireContext(), "Nama dan Harga tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun updateMenu(oldMenu: Menu, newName: String, newPrice: String) {
        val menuId = oldMenu.id.toIntOrNull() ?: return
        
        viewLifecycleOwner.lifecycleScope.launch {
            // Update Nama
            val nameUpdateSuccess = menuController.updateMenuName(menuId, oldMenu.copy(name = newName))
            // Update Harga
            val priceUpdateSuccess = menuController.updateMenuPrice(menuId, oldMenu.copy(price = newPrice))

            if (nameUpdateSuccess || priceUpdateSuccess) {
                loadMenuFromServer() // Refresh list
                Toast.makeText(requireContext(), "Menu berhasil diperbarui", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Gagal memperbarui menu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMenuFromServer() {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = menuController.getMenu()
            if (response != null) {
                // Konversi MenuResponse dari API ke Model Menu yang digunakan Adapter
                val mappedMenu = response.map { res ->
                    Menu(
                        id = res.id.toString(),
                        name = res.name,
                        price = res.price.toString(),
                        category = res.typeName ?: "Tanpa Kategori",
                        imageUrl = res.imageUrl // Menggunakan field imageUrl untuk Glide
                    )
                }
                
                menuList.clear()
                menuList.addAll(mappedMenu)
                menuAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data dari server", Toast.LENGTH_SHORT).show()
            }
        }
    }
}