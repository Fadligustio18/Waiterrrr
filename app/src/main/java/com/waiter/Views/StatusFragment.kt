package com.waiter.Views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waiter.R

class StatusFragment : Fragment(R.layout.fragment_status) {

    private lateinit var rvStatus: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvStatus = view.findViewById(R.id.rvStatus)
        rvStatus.layoutManager = LinearLayoutManager(requireContext())

        // Di sini nantinya bisa ditambahkan adapter untuk menampilkan status pesanan
        // Untuk sementara, kita biarkan kosong atau tambahkan dummy data jika diperlukan
    }
}