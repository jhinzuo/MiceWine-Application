package com.miHoYo.Yuanshen.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.miHoYo.Yuanshen.R
import com.miHoYo.Yuanshen.activities.MainActivity.Companion.ACTION_SELECT_ICON
import com.miHoYo.Yuanshen.activities.MainActivity.Companion.selectedGameArray
import com.miHoYo.Yuanshen.fragments.ShortcutsFragment.Companion.editGameFromList

class EditGamePreferencesFragment : DialogFragment() {
    private var preferences: SharedPreferences? = null
    private var imageView: ImageView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_edit_game_preferences, null)

        val editTextNewName = view.findViewById<EditText>(R.id.editTextNewName)
        val editTextArguments = view.findViewById<EditText>(R.id.appArgumentsEditText)
        val buttonContinue = view.findViewById<Button>(R.id.buttonContinue)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)

        imageView = view.findViewById(R.id.imageView)

        val imageBitmap = BitmapFactory.decodeFile(selectedGameArray[2])

        if (imageBitmap != null) {
            imageView?.setImageBitmap(
                resizeBitmap(
                    imageBitmap, imageView?.layoutParams?.width!!, imageView?.layoutParams?.height!!
                )
            )
        }

        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).setView(view).create()

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        editTextNewName.setText(selectedGameArray[0])
        editTextArguments.setText(selectedGameArray[3])

        imageView?.setOnClickListener {
            requireActivity().sendBroadcast(
                Intent(ACTION_SELECT_ICON)
            )
        }

        buttonContinue.setOnClickListener {
            val newName = editTextNewName.text.toString()
            val newArguments = editTextArguments.text.toString()

            if (newName == "") {
                return@setOnClickListener
            }

            editGameFromList(preferences!!, selectedGameArray, newName, newArguments)

            dismiss()
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }

        return dialog
    }

    override fun onResume() {
        val imageBitmap = BitmapFactory.decodeFile(selectedGameArray[2])

        if (imageBitmap != null) {
            imageView?.setImageBitmap(
                resizeBitmap(
                    imageBitmap, imageView?.layoutParams?.width!!, imageView?.layoutParams?.height!!
                )
            )
        }

        super.onResume()
    }

    private fun resizeBitmap(originalBitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
    }
}