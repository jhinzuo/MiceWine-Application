package com.micewine.emu.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.micewine.emu.R
import com.micewine.emu.adapters.AdapterPreset.Companion.PHYSICAL_CONTROLLER
import com.micewine.emu.adapters.AdapterPreset.Companion.VIRTUAL_CONTROLLER
import com.micewine.emu.adapters.AdapterPreset.Companion.clickedPresetType
import com.micewine.emu.controller.ControllerUtils.getGameControllerNames
import com.micewine.emu.databinding.ActivityPresetManagerBinding
import com.micewine.emu.fragments.Box64PresetManagerFragment
import com.micewine.emu.fragments.Box64SettingsFragment
import com.micewine.emu.fragments.ControllerMapperFragment
import com.micewine.emu.fragments.ControllerPresetManagerFragment
import com.micewine.emu.fragments.CreatePresetFragment
import com.micewine.emu.fragments.CreatePresetFragment.Companion.BOX64_PRESET
import com.micewine.emu.fragments.CreatePresetFragment.Companion.CONTROLLER_PRESET
import com.micewine.emu.fragments.CreatePresetFragment.Companion.VIRTUAL_CONTROLLER_PRESET
import com.micewine.emu.fragments.FloatingFileManagerFragment
import com.micewine.emu.fragments.FloatingFileManagerFragment.Companion.OPERATION_IMPORT_PRESET
import com.micewine.emu.fragments.VirtualControllerPresetManagerFragment
import com.micewine.emu.views.OverlayView

class PresetManagerActivity : AppCompatActivity() {
    private var binding: ActivityPresetManagerBinding? = null
    private var backButton: ImageButton? = null
    private var controllerConnected: TextView? = null
    private var addPresetFAB: FloatingActionButton? = null
    private var importPresetFAB: FloatingActionButton? = null
    private var preferences: SharedPreferences? = null
    private val controllerPresetFragment = ControllerPresetManagerFragment()
    private val controllerMapperFragment = ControllerMapperFragment()
    private val virtualControllerMapperFragment = VirtualControllerPresetManagerFragment()
    private val box64PresetManagerFragment = Box64PresetManagerFragment()
    private val box64SettingsFragment = Box64SettingsFragment()

    // Add a reference to the OverlayView
    private var overlayView: OverlayView? = null

    // Add a preference key for the toggle state
    companion object {
        const val PREF_MOUSE_CONTROL_ENABLED_WHILE_OVERLAY_ENABLED = "mouseControlEnabledWhileOverlayEnabled"
    }

    @SuppressLint("SetTextI18n", "UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)!!

        binding = ActivityPresetManagerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // Initialize the OverlayView (assuming it's part of the layout)
        overlayView = findViewById(R.id.overlayView) // Replace with your actual OverlayView ID

        // Set the initial state of mouse control while overlay is enabled
        val isMouseControlEnabled = preferences?.getBoolean(PREF_MOUSE_CONTROL_ENABLED_WHILE_OVERLAY_ENABLED, true) ?: true
        overlayView?.setMouseControlEnabledWhileOverlayEnabled(isMouseControlEnabled)

        // Rest of your existing onCreate code...
    }

    // Add a method to handle the toggle state change
    fun onMouseControlToggleChanged(isEnabled: Boolean) {
        preferences?.edit()?.putBoolean(PREF_MOUSE_CONTROL_ENABLED_WHILE_OVERLAY_ENABLED, isEnabled)?.apply()
        overlayView?.setMouseControlEnabledWhileOverlayEnabled(isEnabled)
    }

    // Rest of the PresetManagerActivity class remains unchanged...
    // (Include all existing methods and logic here)
}