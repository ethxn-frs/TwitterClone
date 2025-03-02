package com.etang.twitterclone.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.databinding.FragmentEditProfileBinding
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import kotlinx.coroutines.launch
import java.io.File

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository()
    private lateinit var sessionManager: SessionManager
    private var userId: Int = 0
    private lateinit var originalValues: MutableMap<String, String>

    private val REQUEST_CODE_PP = 100
    private val REQUEST_CODE_COVER = 101
    private var imageType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        userId = sessionManager.getUserId()
        originalValues = mutableMapOf()

        setupTopBar()
        fetchUserProfile()
        setupImageButtons()
    }

    private fun setupTopBar() {
        val topBar = requireActivity().findViewById<View>(R.id.topBar)
        val tvTitleTopBar = requireActivity().findViewById<TextView>(R.id.tvTitleTopBar)
        val btnBackTopBar = requireActivity().findViewById<ImageButton>(R.id.btnBackTopBar)
        val ivProfileTopBar = requireActivity().findViewById<ImageView>(R.id.ivProfileTopBar)
        val btnActionTopBar = requireActivity().findViewById<ImageButton>(R.id.btnActionTopBar)

        topBar.visibility = View.VISIBLE
        tvTitleTopBar.text = "Modifier le profil"

        btnBackTopBar.visibility = View.VISIBLE
        ivProfileTopBar.visibility = View.GONE
        btnActionTopBar.visibility = View.GONE

        btnBackTopBar.setOnClickListener {
            topBar.visibility = View.GONE
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun fetchUserProfile() {
        lifecycleScope.launch {
            val user = userRepository.getUserById(userId)
            user?.let {
                binding.etFirstName.setText(it.firstName)
                binding.etLastName.setText(it.lastName)
                binding.etBio.setText(it.bio ?: "")
                binding.etWebsite.setText(it.website ?: "")
                binding.etBirthDate.setText(it.birthDate ?: "")

                originalValues["firstName"] = it.firstName
                originalValues["lastName"] = it.lastName
                originalValues["bio"] = it.bio ?: ""
                originalValues["website"] = it.website ?: ""
                originalValues["birthDate"] = it.birthDate ?: ""

                Glide.with(this@EditProfileFragment)
                    .load(it.profilePictureUrl ?: R.drawable.ic_profile).circleCrop()
                    .into(binding.ivProfilePicture)

                Glide.with(this@EditProfileFragment)
                    .load(it.coverPictureUrl ?: R.drawable.ic_profile).into(binding.ivCoverPicture)

                setupEditFields()
            }
        }
    }

    private fun setupEditFields() {
        setupFieldWatcher(
            binding.etFirstName, binding.btnSaveFirstName, binding.btnCancelFirstName, "firstName"
        )
        setupFieldWatcher(
            binding.etLastName, binding.btnSaveLastName, binding.btnCancelLastName, "lastName"
        )
        setupFieldWatcher(
            binding.etBio, binding.btnSaveBio, binding.btnCancelBio, "bio"
        )
        setupFieldWatcher(
            binding.etWebsite, binding.btnSaveWebsite, binding.btnCancelWebsite, "website"
        )
        setupFieldWatcher(
            binding.etBirthDate, binding.btnSaveBirthDate, binding.btnCancelBirthDate, "birthDate"
        )
    }

    private fun setupFieldWatcher(
        editText: EditText, saveButton: ImageButton, cancelButton: ImageButton, field: String
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newValue = editText.text.toString()
                val oldValue = originalValues[field] ?: ""

                if (newValue != oldValue) {
                    saveButton.visibility = View.VISIBLE
                    cancelButton.visibility = View.VISIBLE
                } else {
                    saveButton.visibility = View.GONE
                    cancelButton.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        saveButton.setOnClickListener {
            val newValue = editText.text.toString()
            updateUserProfileField(field, newValue)
            saveButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
        }

        cancelButton.setOnClickListener {
            editText.setText(originalValues[field])
            saveButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
        }
    }

    private fun updateUserProfileField(field: String, value: String) {
        lifecycleScope.launch {
            val success = userRepository.updateUserField(userId, field, value)
            if (success) {
                originalValues[field] = value
                Toast.makeText(requireContext(), "$field mis à jour", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(), "Erreur lors de la mise à jour", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupImageButtons() {
        binding.btnChangeProfilePicture.setOnClickListener {
            imageType = "pp"
            if (checkAndRequestPermissions()) {
                openGallery(REQUEST_CODE_PP)
            }
        }

        binding.btnDeleteProfilePicture.setOnClickListener {
            Toast.makeText(requireContext(), "Supprimer la photo de profil", Toast.LENGTH_SHORT)
                .show()
            deleteProfilePicture()
        }

        binding.btnChangeCover.setOnClickListener {
            imageType = "cover"
            if (checkAndRequestPermissions()) {
                openGallery(REQUEST_CODE_PP)
            }
        }

        binding.btnDeleteCover.setOnClickListener {
            Toast.makeText(requireContext(), "Supprimer la photo de couverture", Toast.LENGTH_SHORT)
                .show()
            deleteCoverPicture()
        }
    }


    private fun openGallery(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                val filePath = getRealPathFromUri(it)
                if (filePath != null) {
                    uploadImage(filePath, imageType)
                }
            }
        }
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val fileName = getFileName(uri) ?: return null
        val file = File(requireContext().cacheDir, fileName)

        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    private fun getFileName(uri: Uri): String? {
        var name: String? = null
        val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    name = it.getString(index)
                }
            }
        }
        return name
    }


    private fun uploadImage(filePath: String, type: String) {
        lifecycleScope.launch {
            val file = File(filePath)
            val success = userRepository.updateProfileImage(userId, file, type)

            if (success) {
                Toast.makeText(requireContext(), "Image mise à jour", Toast.LENGTH_SHORT).show()
                fetchUserProfile()
            } else {
                Toast.makeText(requireContext(), "Erreur lors de l'upload", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun deleteProfilePicture() {
        lifecycleScope.launch {
            userRepository.deleteProfilePicture(sessionManager.getUserId())
            Toast.makeText(
                requireContext(), "Photo de couverture supprimée", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissions = mutableListOf<String>()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ : Demander les nouvelles permissions
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            // Android 12 et inférieur : Utiliser l'ancienne permission
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        return if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                101
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission accordée !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission refusée !", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun deleteCoverPicture() {
        lifecycleScope.launch {
            userRepository.deleteCoverPicture(sessionManager.getUserId())
            Toast.makeText(
                requireContext(), "Photo de couverture supprimée", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
