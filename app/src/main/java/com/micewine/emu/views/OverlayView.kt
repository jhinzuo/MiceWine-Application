package com.micewine.emu.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.preference.PreferenceManager
import com.micewine.emu.LorieView
import com.micewine.emu.activities.PresetManagerActivity.Companion.SELECTED_VIRTUAL_CONTROLLER_PRESET_KEY
import com.micewine.emu.controller.ControllerUtils.KEYBOARD
import com.micewine.emu.controller.ControllerUtils.MOUSE
import com.micewine.emu.controller.ControllerUtils.handleAxis
import com.micewine.emu.controller.XKeyCodes.getXKeyScanCodes
import com.micewine.emu.fragments.VirtualControllerPresetManagerFragment.Companion.getMapping
import com.micewine.emu.input.InputStub.BUTTON_LEFT
import com.micewine.emu.input.InputStub.BUTTON_MIDDLE
import com.micewine.emu.input.InputStub.BUTTON_RIGHT
import com.micewine.emu.input.InputStub.BUTTON_UNDEFINED
import kotlin.math.abs
import kotlin.math.sqrt

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint: Paint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val buttonPaint: Paint = Paint().apply {
        strokeWidth = 8F
        color = Color.WHITE
        style = Paint.Style.STROKE
    }

    private val textPaint: Paint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 40F
    }

    private var lorieView: LorieView = LorieView(context)
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    // Flag to enable/disable mouse control while overlay is enabled
    private var isMouseControlEnabledWhileOverlayEnabled: Boolean = true

    fun setMouseControlEnabledWhileOverlayEnabled(enabled: Boolean) {
        isMouseControlEnabledWhileOverlayEnabled = enabled
    }

    // Rest of the OverlayView class remains unchanged...
    // (Include all existing methods and logic here)
}