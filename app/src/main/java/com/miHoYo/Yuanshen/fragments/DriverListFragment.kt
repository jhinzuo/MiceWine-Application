package com.miHoYo.Yuanshen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.miHoYo.Yuanshen.R
import com.miHoYo.Yuanshen.activities.MainActivity.Companion.appRootDir
import com.miHoYo.Yuanshen.adapters.AdapterRatPackage
import com.miHoYo.Yuanshen.adapters.AdapterRatPackage.Companion.DRIVER
import java.io.File

class DriverListFragment : Fragment() {
    private val ratList: MutableList<AdapterRatPackage.Item> = ArrayList()
    private var rootView: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_general_settings, container, false)
        recyclerView = rootView?.findViewById(R.id.recyclerViewGeneralSettings)

        setAdapter()

        return rootView
    }

    private fun setAdapter() {
        recyclerView?.setAdapter(AdapterRatPackage(ratList, requireContext()))

        ratList.clear()

        File("$appRootDir/packages").listFiles()?.forEach { file ->
            if (file.isDirectory && file.name.startsWith("VulkanDriver-")) {
                val lines = File("$file/pkg-header").readLines()

                val name = lines[0].substringAfter("=")
                val version = lines[2].substringAfter("=")

                addToAdapter(name, version, file.name)
            }
        }
    }

    private fun addToAdapter(title: String, description: String, driverFolderId: String) {
        ratList.add(AdapterRatPackage.Item(title, description, driverFolderId, DRIVER))
    }
}
