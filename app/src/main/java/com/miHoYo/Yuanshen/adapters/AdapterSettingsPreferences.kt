package com.miHoYo.Yuanshen.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.miHoYo.Yuanshen.R
import com.miHoYo.Yuanshen.activities.GeneralSettingsActivity.Companion.CHECKBOX
import com.miHoYo.Yuanshen.activities.GeneralSettingsActivity.Companion.DISPLAY_MODE
import com.miHoYo.Yuanshen.activities.GeneralSettingsActivity.Companion.DISPLAY_MODE_DEFAULT_VALUE
import com.miHoYo.Yuanshen.activities.GeneralSettingsActivity.Companion.DISPLAY_RESOLUTION
import com.miHoYo.Yuanshen.activities.GeneralSettingsActivity.Companion.SEEKBAR
import com.miHoYo.Yuanshen.activities.GeneralSettingsActivity.Companion.SPINNER
import com.miHoYo.Yuanshen.activities.GeneralSettingsActivity.Companion.SWITCH
import com.miHoYo.Yuanshen.fragments.DisplaySettingsFragment.Companion.getNativeResolutions
import com.miHoYo.Yuanshen.fragments.DisplaySettingsFragment.Companion.resolutions16_9
import com.miHoYo.Yuanshen.fragments.DisplaySettingsFragment.Companion.resolutions4_3
import com.miHoYo.Yuanshen.fragments.InfoDialogFragment

class AdapterSettingsPreferences(
    private val settingsList: List<SettingsListSpinner>,
    private val activity: FragmentActivity,
    private val recyclerView: RecyclerView
) :
    RecyclerView.Adapter<AdapterSettingsPreferences.ViewHolder>() {

    val preferences = PreferenceManager.getDefaultSharedPreferences(activity)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_settings_preferences_item, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sList = settingsList[position]
        holder.settingsName.setText(sList.titleSettings)
        holder.settingsDescription.setText(sList.descriptionSettings)

        if (activity.getString(sList.descriptionSettings) == " ") {
            holder.settingsDescription.visibility = View.GONE
        }

        if (sList.key == DISPLAY_RESOLUTION) {
            val aspectRatio = preferences.getString(DISPLAY_MODE, DISPLAY_MODE_DEFAULT_VALUE)

            when (aspectRatio) {
                "4:3" -> {
                    sList.spinnerOptions = resolutions4_3
                }
                "16:9" -> {
                    sList.spinnerOptions = resolutions16_9
                }
                "Native" -> {
                    sList.spinnerOptions = getNativeResolutions(activity).toTypedArray()
                }
            }
        }

        when (sList.type) {
            SWITCH -> {
                holder.spinnerOptions.visibility = View.GONE
                holder.settingsSwitch.visibility = View.VISIBLE
                holder.seekBar.visibility = View.GONE
                holder.seekBarValue.visibility = View.GONE

                holder.settingsSwitch.isChecked = preferences.getBoolean(sList.key, sList.defaultValue.toBoolean())
                holder.settingsSwitch.setOnClickListener {
                    preferences.edit().apply {
                        putBoolean(sList.key, !preferences.getBoolean(sList.key, sList.defaultValue.toBoolean()))
                        apply()
                    }
                }
            }
            SPINNER -> {
                holder.settingsSwitch.visibility = View.GONE
                holder.spinnerOptions.visibility = View.VISIBLE
                holder.seekBar.visibility = View.GONE
                holder.seekBarValue.visibility = View.GONE

                holder.spinnerOptions.adapter = ArrayAdapter(
                    activity,
                    android.R.layout.simple_spinner_dropdown_item,
                    sList.spinnerOptions!!
                )

                holder.spinnerOptions.setSelection(
                    sList.spinnerOptions!!.indexOf(
                        preferences.getString(
                            sList.key,
                            sList.defaultValue
                        )
                    )
                )

                holder.spinnerOptions.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedItem = parent?.getItemAtPosition(position).toString()

                            preferences.edit().apply {
                                putString(sList.key, selectedItem)
                                apply()
                            }

                            if (sList.key == DISPLAY_MODE) {
                                activity.runOnUiThread {
                                    recyclerView.adapter?.notifyItemRangeChanged(holder.adapterPosition + 1, recyclerView.adapter?.itemCount!!)
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
            }
            CHECKBOX -> {
                holder.settingsSwitch.visibility = View.GONE
                holder.spinnerOptions.visibility = View.VISIBLE
                holder.seekBar.visibility = View.GONE
                holder.seekBarValue.visibility = View.GONE

                holder.spinnerOptions.adapter = CheckableAdapter(
                    activity,
                    sList.spinnerOptions!!,
                    sList,
                    preferences,
                    holder.spinnerOptions
                )
            }
            SEEKBAR -> {
                holder.settingsSwitch.visibility = View.GONE
                holder.spinnerOptions.visibility = View.GONE
                holder.seekBar.visibility = View.VISIBLE
                holder.seekBarValue.visibility = View.VISIBLE

                holder.seekBar.min = sList.seekBarMaxMinValues!![0]
                holder.seekBar.max = sList.seekBarMaxMinValues!![1]

                holder.seekBar.progress = preferences.getInt(sList.key, sList.defaultValue.toInt())

                if (holder.seekBar.progress == 0) {
                    holder.seekBarValue.text = activity.getString(R.string.unlimited)
                } else {
                    holder.seekBarValue.text = "${holder.seekBar.progress} FPS"
                }

                holder.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (progress == 0) {
                            holder.seekBarValue.text = activity.getString(R.string.unlimited)
                        } else {
                            holder.seekBarValue.text = "$progress FPS"
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        preferences.edit().apply {
                            putInt(sList.key, seekBar?.progress!!)
                            apply()
                        }
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return settingsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val settingsName: TextView = itemView.findViewById(R.id.title_preferences_model)
        val settingsDescription: TextView = itemView.findViewById(R.id.description_preferences_model)
        val spinnerOptions: Spinner = itemView.findViewById(R.id.keyBindSpinner)
        val settingsSwitch: SwitchCompat = itemView.findViewById(R.id.optionSwitch)
        val seekBar: SeekBar = itemView.findViewById(R.id.seekBar)
        val seekBarValue: TextView = itemView.findViewById(R.id.seekBarValue)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val settingsModel = settingsList[getAdapterPosition()]

            InfoDialogFragment(
                settingsName.text.toString(),
                activity.getString(settingsModel.descriptionSettings)
            ).show(activity.supportFragmentManager, "")
        }
    }

    class SettingsListSpinner(var titleSettings: Int, var descriptionSettings: Int, var spinnerOptions: Array<String>?, var seekBarMaxMinValues: Array<Int>?, var type: Int, var defaultValue: String, var key: String)

    class CheckableAdapter(
        val activity: Activity,
        private val arrayElements: Array<String>,
        private val sList: SettingsListSpinner,
        val preferences: SharedPreferences,
        private val spinner: Spinner
    ) : SpinnerAdapter {
        val checked = BooleanArray(count)

        override fun registerDataSetObserver(p0: DataSetObserver?) {
        }

        override fun unregisterDataSetObserver(p0: DataSetObserver?) {
        }

        override fun getCount(): Int {
            return arrayElements.count()
        }

        override fun getItem(p0: Int): Any {
            return arrayElements[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
            val inflater = activity.layoutInflater
            val view = p1 ?: inflater.inflate(android.R.layout.simple_spinner_item, p2, false)

            view.findViewById<TextView>(android.R.id.text1).apply {
                text = preferences.getString(sList.key, sList.defaultValue)
            }

            return view
        }

        override fun getItemViewType(p0: Int): Int {
            return 0
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun isEmpty(): Boolean {
            return arrayElements.isEmpty()
        }

        override fun getDropDownView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val inflater = activity.layoutInflater
            val view = inflater.inflate(R.layout.item_checkbox, p2, false)
            val checkBox = view.findViewById<CheckBox>(R.id.checkbox)
            val preferencesValue = preferences.getString(sList.key, sList.defaultValue)

            checked[p0] = preferencesValue?.contains(arrayElements[p0]) == true

            checkBox.isChecked = checked[p0]
            checkBox.text = arrayElements[p0]

            checkBox.setOnClickListener {
                checked[p0] = !checked[p0]

                val builder: StringBuilder = StringBuilder()

                val editor = preferences.edit()

                for (i in checked.indices) {
                    if (checked[i]) {
                        builder.append(",")
                        builder.append(arrayElements[i])
                    }
                }

                if (builder.isNotEmpty()) {
                    builder.deleteCharAt(0)
                }

                editor.putString(sList.key, builder.toString())

                editor.apply()

                spinner.adapter = this
            }

            return view
        }
    }
}