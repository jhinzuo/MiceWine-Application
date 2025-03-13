package com.micewine.emu.fragments

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.micewine.emu.R
import com.micewine.emu.activities.PresetManagerActivity
import com.micewine.emu.activities.PresetManagerActivity.Companion.SELECTED_VIRTUAL_CONTROLLER_PRESET_KEY
import com.micewine.emu.adapters.AdapterPreset
import com.micewine.emu.adapters.AdapterPreset.Companion.VIRTUAL_CONTROLLER
import com.micewine.emu.adapters.AdapterPreset.Companion.selectedPresetId
import com.micewine.emu.views.OverlayView
import java.io.File

class VirtualControllerPresetManagerFragment : Fragment() {
    private var rootView: View? = null
    private lateinit var mouseControlToggle: Switch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_general_settings, container, false)
        recyclerView = rootView?.findViewById(R.id.recyclerViewGeneralSettings)

        // Find the toggle in the layout
        mouseControlToggle = rootView!!.findViewById(R.id.mouseControlToggle)

        // Set the initial state from preferences
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isMouseControlEnabled = preferences.getBoolean(PresetManagerActivity.PREF_MOUSE_CONTROL_ENABLED_WHILE_OVERLAY_ENABLED, true)
        mouseControlToggle.isChecked = isMouseControlEnabled

        // Handle toggle state changes
        mouseControlToggle.setOnCheckedChangeListener { _, isChecked ->
            (activity as? PresetManagerActivity)?.onMouseControlToggleChanged(isChecked)
        }

        initialize(requireContext())
        setAdapter()

        return rootView
    }

    // Rest of the VirtualControllerPresetManagerFragment class remains unchanged...
    // (Include all existing methods and logic here)
}