package com.omni.omniwidgetapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var editTextUrl: EditText
    private lateinit var editTextData: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUrlFunctionality()
        initScriptFunctionality()
    }

    private fun initUrlFunctionality() {
        val launchButton: ImageButton = findViewById<ImageButton>(R.id.buttonLoadUrl)
        editTextUrl = findViewById(R.id.editTextUrl)

        launchButton.setOnClickListener {
            val wb = findViewById<WebView>(R.id.webviewLCW)
            setViewSettings(wb)
            val url = editTextUrl.text.toString()
            if (url.isNotEmpty()) {
                wb.loadUrl(editTextUrl.text.toString())
            }
        }
    }

    private fun initScriptFunctionality() {
        val launchScriptButton: ImageButton = findViewById<ImageButton>(R.id.buttonLoadScript)
        editTextData = findViewById(R.id.editTextScript)

        launchScriptButton.setOnClickListener {
            val wb = findViewById<WebView>(R.id.webviewLCW)
            setViewSettings(wb)

            val script = editTextData.text.toString()
            if (script.isNotEmpty()) {
                wb.loadData(script, "text/html", "UTF-8")
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setViewSettings(wb: WebView) {
        wb.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("WebView", "Console: ${consoleMessage?.message()}")
                return true
            }
        }

        wb.settings.databaseEnabled = true
        wb.settings.javaScriptEnabled = true
        wb.settings.javaScriptCanOpenWindowsAutomatically = true
        wb.settings.domStorageEnabled = true
        wb.settings.allowContentAccess = true
        wb.settings.allowFileAccess = true
    }
}