package com.etang.twitterclone.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.databinding.BottomSheetEditProfileBinding
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import kotlinx.coroutines.launch

class EditProfileBottomSheet(private val onProfileUpdated: () -> Unit) : DialogFragment() {

    private var _binding: BottomSheetEditProfileBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository()
    private lateinit var sessionManager: SessionManager
    private var userId: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = BottomSheetEditProfileBinding.inflate(LayoutInflater.from(context))
        sessionManager = SessionManager(requireContext())
        userId = sessionManager.getUserId()

        fetchUserProfile()

        binding.btnClose.setOnClickListener { dismiss() }

        setupEditFields()

        return Dialog(requireContext(), R.style.BottomSheetDialogTheme).apply {
            setContentView(binding.root)
        }
    }

    private fun fetchUserProfile() {
        lifecycleScope.launch {
            val user = userRepository.getUserById(userId)
            user?.let {
                binding.etFirstName.setText(it.firstName)
                binding.etLastName.setText(it.lastName)
                binding.etBio.setText(it.bio ?: "")
                binding.etLocation.setText(it.location ?: "")
                binding.etWebsite.setText(it.website ?: "")
                binding.etBirthDate.setText(it.birthDate ?: "")

                Glide.with(this@EditProfileBottomSheet)
                    .load(it.profilePictureUrl ?: R.drawable.ic_profile)
                    .circleCrop()
                    .into(binding.ivProfilePicture)

                Glide.with(this@EditProfileBottomSheet)
                    .load(it.coverPictureUrl ?: R.drawable.ic_profile)
                    .into(binding.ivCoverPicture)
            }
        }
    }

    private fun setupEditFields() {
        binding.etFirstName.setOnClickListener { showEditDialog("Prénom", binding.etFirstName) }
        binding.etLastName.setOnClickListener { showEditDialog("Nom", binding.etLastName) }
        binding.etBio.setOnClickListener { showEditDialog("Bio", binding.etBio) }
        binding.etLocation.setOnClickListener { showEditDialog("Localisation", binding.etLocation) }
        binding.etWebsite.setOnClickListener { showEditDialog("Site web", binding.etWebsite) }
        binding.etBirthDate.setOnClickListener {
            showEditDialog(
                "Date de naissance",
                binding.etBirthDate
            )
        }
    }

    private fun showEditDialog(title: String, editText: EditText) {
        val input = EditText(requireContext()).apply { setText(editText.text.toString()) }

        AlertDialog.Builder(requireContext())
            .setTitle("Modifier $title")
            .setView(input)
            .setPositiveButton("Valider") { _, _ ->
                val newValue = input.text.toString()
                editText.setText(newValue)
                updateUserProfileField(title, newValue)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun updateUserProfileField(field: String, value: String) {
        lifecycleScope.launch {
            val success = userRepository.updateUserField(userId, field, value)
            if (success) {
                Toast.makeText(requireContext(), "$field mis à jour", Toast.LENGTH_SHORT).show()
                onProfileUpdated()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Erreur lors de la mise à jour",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
