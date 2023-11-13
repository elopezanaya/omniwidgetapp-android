package com.example.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var editTextUrl: EditText
    private lateinit var editTextData: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var launchButton = findViewById<Button>(R.id.buttonLoadUrl)
        editTextUrl = findViewById(R.id.editTextUrl)
        editTextData = findViewById(R.id.editTextData)

        launchButton.setOnClickListener {
            var wb = findViewById<WebView>(R.id.webviewLCW)

            wb.webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    Log.d("WebView", "Console: ${consoleMessage?.message()}")
                    return true
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                wb.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true)
            }

            wb.settings.databaseEnabled= true
            wb.settings.javaScriptEnabled = true
            wb.settings.javaScriptCanOpenWindowsAutomatically= true
            wb.settings.domStorageEnabled= true
            wb.settings.allowContentAccess= true
            wb.settings.allowFileAccess= true


           // wb.loadUrl("https://elastorageredmond.blob.core.windows.net/elopez-dev/mobile/pizza.html");
            var url = editTextUrl.text.toString()
            var script = editTextData.text.toString()

            if ( url.isNotEmpty()){
                wb.loadUrl(editTextUrl.text.toString())

            }else{

                if (script.isNotEmpty()){
                    wb.loadData(script,  "text/html", "UTF-8")
                }

            }

        }


       // wb.loadUrl("https://elopezanayatesting.z19.web.core.windows.net/")
    }
}