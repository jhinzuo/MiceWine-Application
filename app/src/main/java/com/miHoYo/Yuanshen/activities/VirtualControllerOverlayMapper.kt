package com.miHoYo.Yuanshen.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.miHoYo.Yuanshen.R
import com.miHoYo.Yuanshen.controller.XKeyCodes.getXKeyScanCodes
import com.miHoYo.Yuanshen.databinding.ActivityVirtualControllerMapperBinding
import com.miHoYo.Yuanshen.fragments.EditVirtualButtonFragment
import com.miHoYo.Yuanshen.views.OverlayView
import com.miHoYo.Yuanshen.views.OverlayView.Companion.analogList
import com.miHoYo.Yuanshen.views.OverlayView.Companion.buttonList
import com.miHoYo.Yuanshen.views.OverlayViewCreator

class VirtualControllerOverlayMapper : AppCompatActivity() {
    private var binding: ActivityVirtualControllerMapperBinding? = null
    private var overlayView: OverlayViewCreator? = null
    private var virtualControllerMapperDrawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ACTION_EDIT_VIRTUAL_BUTTON) {
                EditVirtualButtonFragment().show(supportFragmentManager, "")
            } else if (intent.action == ACTION_INVALIDATE) {
                overlayView?.invalidate()
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVirtualControllerMapperBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        overlayView = findViewById(R.id.overlayView)

        virtualControllerMapperDrawerLayout = findViewById(R.id.virtualControllerMapperDrawerLayout)
        virtualControllerMapperDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        navigationView = findViewById(R.id.navigationView)
        navigationView?.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.addButton -> {
                    overlayView?.addButton(
                        OverlayView.VirtualButton(
                            buttonList.count() + 1,
                            overlayView?.width!! / 2F,
                            overlayView?.height!! / 2F,
                            180F,
                            "Null",
                            null,
                            -1,
                            false
                        )
                    )

                    virtualControllerMapperDrawerLayout?.closeDrawers()
                }

                R.id.addVAxis -> {
                    overlayView?.addAnalog(
                        OverlayView.VirtualAnalog(
                            analogList.count() + 1,
                            overlayView?.width!! / 2F,
                            overlayView?.height!! / 2F,
                            0F,
                            0F,
                            275F,
                            "Null",
                            getXKeyScanCodes(""),
                            "Null",
                            getXKeyScanCodes(""),
                            "Null",
                            getXKeyScanCodes(""),
                            "Null",
                            getXKeyScanCodes(""),
                            false,
                            -1,
                            0.5F
                        )
                    )

                    virtualControllerMapperDrawerLayout?.closeDrawers()
                }

                R.id.exitButton -> {
                    overlayView?.saveOnPreferences()

                    finish()
                }
            }

            true
        }

        registerReceiver(receiver, object : IntentFilter(ACTION_EDIT_VIRTUAL_BUTTON) {
            init {
                addAction(ACTION_INVALIDATE)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (virtualControllerMapperDrawerLayout?.isOpen!!) {
                virtualControllerMapperDrawerLayout?.closeDrawers()
            } else {
                virtualControllerMapperDrawerLayout?.openDrawer(GravityCompat.START)
            }

            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    companion object {
        const val ACTION_EDIT_VIRTUAL_BUTTON = "com.miHoYo.Yuanshen.ACTION_EDIT_VIRTUAL_BUTTON"
        const val ACTION_INVALIDATE = "com.miHoYo.Yuanshen.ACTION_INVALIDATE"
    }
}