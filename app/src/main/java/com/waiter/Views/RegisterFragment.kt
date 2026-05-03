package com.waiter.Views

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.waiter.Controllers.AuthControllers
import com.waiter.Models.UserRequest
import com.waiter.R
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_registration) {
    private val authController = AuthControllers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<TextInputEditText>(R.id.etName)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val rgRole = view.findViewById<RadioGroup>(R.id.radioGroupRole)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val name = etName.text.toString()
            val password = etPassword.text.toString()
            val selectedRoleId = when (rgRole.checkedRadioButtonId) {
                R.id.rbWaiter -> 2
                R.id.rbChef -> 4
                R.id.rbCashier -> 3
                else -> 0
            }

            if (name.isEmpty() || password.isEmpty() || selectedRoleId == 0) {
                Toast.makeText(requireContext(), "Harap isi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userRequest = UserRequest(name, password, selectedRoleId)

            viewLifecycleOwner.lifecycleScope.launch {
                val success = authController.registerController(userRequest)
                if (success) {
                    Toast.makeText(requireContext(), "User berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    etName.text?.clear()
                    etPassword.text?.clear()
                    rgRole.clearCheck()
                } else {
                    Toast.makeText(requireContext(), "Gagal menambahkan user", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
