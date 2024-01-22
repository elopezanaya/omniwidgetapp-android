package com.omni.omniwidgetapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var editTextUrl: EditText
    private lateinit var editTextScript: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        preloadUrl();
        initUrlFunctionality()
        initScriptFunctionality()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun preloadUrl(){
        val myWebView: WebView = findViewById(R.id.webviewLCW)
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl("https://www.clockfaceonline.co.uk/clocks/digital/")

    }

    private fun initUrlFunctionality() {
        val launchButton: ImageButton = findViewById(R.id.buttonLoadUrl)
        val deleteUrlContent : ImageButton = findViewById(R.id.buttonDeleteUrl)

        editTextUrl = findViewById(R.id.editTextUrl)

        launchButton.setOnClickListener {
            val wb = findViewById<WebView>(R.id.webviewLCW)
            setViewSettings(wb)
            val url = editTextUrl.text.toString()
            if (url.isNotEmpty()) {
                wb.loadUrl(editTextUrl.text.toString())
            }
        }

        deleteUrlContent.setOnClickListener {
            editTextUrl.setText("")
        }
    }

    private fun initScriptFunctionality() {
        val launchScriptButton: ImageButton = findViewById(R.id.buttonLoadScript)
        val deleteScriptContent : ImageButton = findViewById(R.id.buttonDeleteScript)

        editTextScript = findViewById(R.id.editTextScript)

        launchScriptButton.setOnClickListener {
            val wb = findViewById<WebView>(R.id.webviewLCW)
            setViewSettings(wb)

            val script = editTextScript.text.toString()
            if (script.isNotEmpty()) {
                wb.loadData(script, "text/html", "UTF-8")
            }
        }

        deleteScriptContent.setOnClickListener {
            editTextScript.setText("")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setViewSettings(wb: WebView) {
        wb.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("WebView", "Console: ${consoleMessage?.message()}")
                return true
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                runOnUiThread {
                    val permissions = arrayOf(
                        PermissionRequest.RESOURCE_AUDIO_CAPTURE,
                        PermissionRequest.RESOURCE_VIDEO_CAPTURE,

                    )
                    request!!.grant(permissions)
                }
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