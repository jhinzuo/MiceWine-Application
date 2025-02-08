package com.miHoYo.Yuanshen.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.miHoYo.Yuanshen.R
import com.miHoYo.Yuanshen.activities.MainActivity.Companion.selectedFile
import com.miHoYo.Yuanshen.activities.MainActivity.Companion.selectedFragment
import com.miHoYo.Yuanshen.activities.MainActivity.Companion.selectedGameArray
import com.miHoYo.Yuanshen.fragments.FileManagerFragment.Companion.deleteFile
import com.miHoYo.Yuanshen.fragments.ShortcutsFragment.Companion.removeGameFromList

class DeleteGameItemFragment : DialogFragment() {
    private var preferences: SharedPreferences? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_delete_game_item, null)

        val buttonContinue = view.findViewById<Button>(R.id.buttonContinue)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)

        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).setView(view).create()

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        buttonContinue.setOnClickListener {
            if (selectedFragment == "ShortcutsFragment") {
                removeGameFromList(preferences!!, selectedGameArray)
            } else if (selectedFragment == "FileManagerFragment") {
                deleteFile(selectedFile)
            }

            dismiss()
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }

        return dialog
    }
}