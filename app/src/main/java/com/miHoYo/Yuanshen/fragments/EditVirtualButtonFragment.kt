package com.miHoYo.Yuanshen.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.miHoYo.Yuanshen.R
import com.miHoYo.Yuanshen.activities.VirtualControllerOverlayMapper.Companion.ACTION_INVALIDATE
import com.miHoYo.Yuanshen.controller.XKeyCodes.getKeyNames
import com.miHoYo.Yuanshen.controller.XKeyCodes.getXKeyScanCodes
import com.miHoYo.Yuanshen.views.OverlayView.Companion.analogList
import com.miHoYo.Yuanshen.views.OverlayView.Companion.buttonList
import com.miHoYo.Yuanshen.views.OverlayViewCreator.Companion.ANALOG
import com.miHoYo.Yuanshen.views.OverlayViewCreator.Companion.BUTTON
import com.miHoYo.Yuanshen.views.OverlayViewCreator.Companion.lastSelectedButton
import com.miHoYo.Yuanshen.views.OverlayViewCreator.Companion.lastSelectedType

class EditVirtualButtonFragment : DialogFragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_edit_virtual_button, null)
        val saveButton = view.findViewById<Button>(R.id.saveButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val radiusSeekbarValue = view.findViewById<TextView>(R.id.radiusSeekbarValue).apply {
            text = "$selectedButtonRadius%"
        }
        val radiusSeekbar = view.findViewById<SeekBar>(R.id.radiusSeekbar).apply {
            max = 400
            min = 100

            setOnSeekBarChangeListener(object :
                OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    radiusSeekbarValue?.text = "$progress%"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        val allKeyNames = getKeyNames(true)

        val buttonSpinner = view.findViewById<Spinner>(R.id.buttonSpinner).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, allKeyNames)
            setSelection(allKeyNames.indexOf(selectedButtonKeyName))
        }
        val analogUpKeySpinner = view.findViewById<Spinner>(R.id.analogUpKeySpinner).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, allKeyNames)
            setSelection(allKeyNames.indexOf(selectedAnalogUpKeyName))
        }
        val analogDownKeySpinner = view.findViewById<Spinner>(R.id.analogDownKeySpinner).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, allKeyNames)
            setSelection(allKeyNames.indexOf(selectedAnalogDownKeyName))
        }
        val analogLeftKeySpinner = view.findViewById<Spinner>(R.id.analogLeftKeySpinner).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, allKeyNames)
            setSelection(allKeyNames.indexOf(selectedAnalogLeftKeyName))
        }
        val analogRightKeySpinner = view.findViewById<Spinner>(R.id.analogRightKeySpinner).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, allKeyNames)
            setSelection(allKeyNames.indexOf(selectedAnalogRightKeyName))
        }

        radiusSeekbar.progress = selectedButtonRadius

        if (lastSelectedType == ANALOG) {
            buttonSpinner.visibility = View.GONE
        } else if (lastSelectedType == BUTTON) {
            view.findViewById<LinearLayout>(R.id.layoutAnalogUp).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layoutAnalogDown).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layoutAnalogLeft).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layoutAnalogRight).visibility = View.GONE
        }

        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).setView(view).create()

        saveButton.setOnClickListener {
            if (lastSelectedType == BUTTON && buttonList.isNotEmpty()) {
                buttonList[lastSelectedButton - 1].keyName = buttonSpinner.selectedItem.toString()
                buttonList[lastSelectedButton - 1].keyCodes = getXKeyScanCodes(buttonSpinner.selectedItem.toString())

                buttonList[lastSelectedButton - 1].radius = radiusSeekbar.progress.toFloat()
            } else if (lastSelectedType == ANALOG && analogList.isNotEmpty()) {
                analogList[lastSelectedButton - 1].upKeyName = analogUpKeySpinner.selectedItem.toString()
                analogList[lastSelectedButton - 1].upKeyCodes = getXKeyScanCodes(analogUpKeySpinner.selectedItem.toString())

                analogList[lastSelectedButton - 1].downKeyName = analogDownKeySpinner.selectedItem.toString()
                analogList[lastSelectedButton - 1].downKeyCodes = getXKeyScanCodes(analogDownKeySpinner.selectedItem.toString())

                analogList[lastSelectedButton - 1].leftKeyName = analogLeftKeySpinner.selectedItem.toString()
                analogList[lastSelectedButton - 1].leftKeyCodes = getXKeyScanCodes(analogLeftKeySpinner.selectedItem.toString())

                analogList[lastSelectedButton - 1].rightKeyName = analogRightKeySpinner.selectedItem.toString()
                analogList[lastSelectedButton - 1].rightKeyCodes = getXKeyScanCodes(analogRightKeySpinner.selectedItem.toString())

                analogList[lastSelectedButton - 1].radius = radiusSeekbar.progress.toFloat()
            }

            context?.sendBroadcast(
                Intent(ACTION_INVALIDATE)
            )

            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        return dialog
    }

    companion object {
        var selectedButtonKeyName = ""
        var selectedAnalogUpKeyName = ""
        var selectedAnalogDownKeyName = ""
        var selectedAnalogLeftKeyName = ""
        var selectedAnalogRightKeyName = ""
        var selectedButtonRadius = 0
    }
}