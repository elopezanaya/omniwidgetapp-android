package com.omni.omniwidgetapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var editTextUrl: EditText
    private lateinit var editTextData: EditText

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launchButton: Button = findViewById<Button>(R.id.buttonLoadUrl)
        editTextUrl = findViewById(R.id.editTextUrl)
        editTextData = findViewById(R.id.editTextData)

        launchButton.setOnClickListener {
            val wb = findViewById<WebView>(R.id.webviewLCW)
            wb.webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    Log.d("WebView", "Console: ${consoleMessage?.message()}")
                    return true
                }
            }

            wb.settings.databaseEnabled= true
            wb.settings.javaScriptEnabled = true
            wb.settings.javaScriptCanOpenWindowsAutomatically= true
            wb.settings.domStorageEnabled= true
            wb.settings.allowContentAccess= true
            wb.settings.allowFileAccess= true

            val url = editTextUrl.text.toString()
           // val script = editTextData.text.toString()

            print("ELOPEZ : URL :$url");

            if ( url.isNotEmpty()){
                wb.loadUrl(editTextUrl.text.toString())
            }

            /*else{

                if (script.isNotEmpty()){
                    wb.loadData(script,  "text/html", "UTF-8")
                }

            }*/

        }
    }
}