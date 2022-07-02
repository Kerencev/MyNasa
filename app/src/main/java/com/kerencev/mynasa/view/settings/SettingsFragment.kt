package com.kerencev.mynasa.view.settings

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.FragmentSettingsBinding
import com.kerencev.mynasa.model.helpers.SPreference

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCurrentTabPosition()
        setTabLayoutClicks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTabLayoutClicks() = with(binding) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        SPreference.saveTheme(requireContext(), R.style.Theme_MyNasa)
                    }
                    1 -> {
                        SPreference.saveTheme(requireContext(), R.style.ThemeSpace)
                    }
                }
                requireActivity().recreate()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun setCurrentTabPosition() = with(binding) {
        when (SPreference.getCurrentTheme(requireContext())) {
            R.style.Theme_MyNasa -> {
                tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            }
            R.style.ThemeSpace -> {
                tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            }
        }
    }
}