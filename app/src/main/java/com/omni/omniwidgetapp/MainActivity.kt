package com.omni.omniwidgetapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.webkit.DownloadListener
import android.webkit.ValueCallback
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher


class MainActivity : AppCompatActivity() {
    private lateinit var editTextUrl: EditText
    private lateinit var editTextScript: EditText
    private lateinit var fileChooserLauncher: ActivityResultLauncher<String>
    var uploadedFileTempPath: ValueCallback<Array<Uri?>?>? = null


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("ELOPEZANAYA", "Console: ELOPEZANAYA :: 3")
        // Request permissions

        preloadUrl();
        initUrlFunctionality()
        initScriptFunctionality()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun preloadUrl(){
        val myWebView: WebView = findViewById(R.id.webviewLCW)
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl("https://www.msn.com/en-us/weather/forecast")
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

            // Request permissions
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PackageManager.PERMISSION_GRANTED
            );
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

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PackageManager.PERMISSION_GRANTED
            );

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
        // Expose Android methods to Javascript layer
        val javascriptInterface = JavascriptInterface(this, wb)
        wb.addJavascriptInterface(javascriptInterface, "Android")

        // Subscribe to notification when a file from Web content needs to be downloaded in Android layer
        wb.setDownloadListener(DownloadListener { url, _, _, _, _ ->

            Log.d("ELOPEZANAYA", "downloadingn URL ###  =>>$url");
            if (url.startsWith("blob:")) {
                wb.evaluateJavascript(javascriptInterface.getBase64StringFromBlobUrl(url), null)
            }

            if (url.contains("data:image")){
                Log.i("ELOPEZANAYA", "transforming image");
                wb.evaluateJavascript(javascriptInterface.processBase64Data(url).toString(), null)
            }

            Log.i("ELOPEZANAYA", "Done with download");

        })

        wb.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("WebView", "Console: ${consoleMessage?.message()}")
                return true
            }

            // Handles HTML forms with 'file' input type on android API 21+
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>?>,
                fileChooserParams: FileChooserParams
            ): Boolean {

                this@MainActivity.uploadedFileTempPath = filePathCallback
                val intent = fileChooserParams.createIntent()
                try {
                    startActivityForResult(intent, 100) // Send request code for select file
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Unable to open file chooser",
                        Toast.LENGTH_LONG
                    ).show()
                    return false
                }

                return true

            }




            override fun onPermissionRequest(request: PermissionRequest?) {
                runOnUiThread {
                    val permissions = arrayOf(
                        PermissionRequest.RESOURCE_AUDIO_CAPTURE,
                        PermissionRequest.RESOURCE_VIDEO_CAPTURE,
                        PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID

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
        wb.settings.allowUniversalAccessFromFileURLs = true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) {
            uploadedFileTempPath?.onReceiveValue(null) // Need to send null value to ensure future attempts workable
            return
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                uploadedFileTempPath?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
                uploadedFileTempPath = null
            }
        }
    }
}