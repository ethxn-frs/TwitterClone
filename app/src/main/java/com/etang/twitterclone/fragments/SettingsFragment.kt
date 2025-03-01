package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.SettingsAdapter
import com.etang.twitterclone.data.model.SettingItem
import com.etang.twitterclone.databinding.FragmentSettingsBinding
import com.etang.twitterclone.pages.MainActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.updateTopBar("Paramètres", 0, isSettings = true)

        val settingsList = listOf(
            SettingItem(R.drawable.ic_profile, "Votre compte"),
            SettingItem(R.drawable.ic_notifications_24px, "Notifications"),
            SettingItem(R.drawable.baseline_heart_broken_24, "Accessibilité, Affichage"),
            SettingItem(R.drawable.ic_send, "Confidentialité et sécurité"),
            SettingItem(R.drawable.ic_favorite_filled24px, "Centre d'aide")
        )

        val adapter = SettingsAdapter(settingsList)
        binding.recyclerViewSettings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSettings.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
