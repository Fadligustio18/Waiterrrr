package com.waiter.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.waiter.Models.CartItem
import com.waiter.Models.MejaModel
import com.waiter.Models.MenuResponse
import com.waiter.Models.TableOrder

class CartViewModel : ViewModel() {
    private val _selectedTable = MutableLiveData<MejaModel?>()
    val selectedTable: LiveData<MejaModel?> = _selectedTable

    // Simpan keranjang untuk setiap meja menggunakan ID meja sebagai key
    private val tableCarts = mutableMapOf<Int, MutableList<CartItem>>()
    private val tables = mutableMapOf<Int, MejaModel>()

    private val _allTableOrders = MutableLiveData<List<TableOrder>>(emptyList())
    val allTableOrders: LiveData<List<TableOrder>> = _allTableOrders

    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    fun setSelectedTable(table: MejaModel) {
        _selectedTable.value = table
        tables[table.id] = table
        val itemsForTable = tableCarts.getOrPut(table.id) { mutableListOf() }
        _cartItems.value = itemsForTable
        updateAllTableOrders()
    }

    private fun updateAllTableOrders() {
        val orders = tableCarts.mapNotNull { (tableId, items) ->
            val table = tables[tableId] ?: return@mapNotNull null
            if (items.isEmpty()) return@mapNotNull null
            TableOrder(table, items)
        }
        _allTableOrders.value = orders
    }

    fun addToCart(menu: MenuResponse) {
        val tableId = _selectedTable.value?.id ?: return
        
        val currentItems = tableCarts.getOrPut(tableId) { mutableListOf() }
        val existingItem = currentItems.find { it.menu.id == menu.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(CartItem(menu))
        }
        _cartItems.value = currentItems
        updateAllTableOrders()
    }

    fun removeFromCart(tableId: Int, cartItem: CartItem) {
        val currentItems = tableCarts[tableId] ?: return
        currentItems.remove(cartItem)
        if (currentItems.isEmpty()) {
            tableCarts.remove(tableId)
        }
        updateAllTableOrders()
        
        // Update _cartItems if the removed item was from selected table
        if (_selectedTable.value?.id == tableId) {
            _cartItems.value = tableCarts[tableId] ?: mutableListOf()
        }
    }

    fun updateQuantity(tableId: Int, cartItem: CartItem, delta: Int) {
        val currentItems = tableCarts[tableId] ?: return
        val item = currentItems.find { it.menu.id == cartItem.menu.id }
        item?.let {
            it.quantity += delta
            if (it.quantity <= 0) {
                currentItems.remove(it)
            }
        }
        if (currentItems.isEmpty()) {
            tableCarts.remove(tableId)
        }
        updateAllTableOrders()

        if (_selectedTable.value?.id == tableId) {
            _cartItems.value = tableCarts[tableId] ?: mutableListOf()
        }
    }

    fun clearCartForTable(tableId: Int) {
        tableCarts.remove(tableId)
        updateAllTableOrders()
        if (_selectedTable.value?.id == tableId) {
            _cartItems.value = mutableListOf()
        }
    }

    fun clearAll() {
        tableCarts.clear()
        _cartItems.value = mutableListOf()
        _selectedTable.value = null
    }
}