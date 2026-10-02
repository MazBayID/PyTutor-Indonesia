package com.pytutor.baru

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject

/** Menjalankan Python (Skulpt) di dalam WebView lokal. Semua berjalan offline. */
class PyRunner(context: Context) {
    var output by mutableStateOf("")
    var berjalan by mutableStateOf(false)
    var siap by mutableStateOf(false)
    var prompt by mutableStateOf<String?>(null)
    private val main = Handler(Looper.getMainLooper())

    inner class Bridge {
        @JavascriptInterface fun out(t: String) { main.post { output += t } }
        @JavascriptInterface fun ask(p: String) { main.post { prompt = p } }
        @JavascriptInterface fun done(err: String) {
            main.post {
                if (err.isNotEmpty()) {
                    if (output.isNotEmpty() && !output.endsWith("\n")) output += "\n"
                    output += err
                }
                berjalan = false
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    val web: WebView = WebView(context).apply {
        settings.javaScriptEnabled = true
        addJavascriptInterface(Bridge(), "Android")
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) { siap = true }
        }
        loadUrl(HALAMAN)
    }

    fun jalankan(kode: String) {
        output = ""
        berjalan = true
        web.evaluateJavascript("runPy(${JSONObject.quote(kode)})", null)
    }

    fun jawab(teks: String) {
        prompt = null
        web.evaluateJavascript("answerInput(${JSONObject.quote(teks)})", null)
    }

    fun berhenti() {
        berjalan = false
        prompt = null
        siap = false
        output += "\n[dihentikan]"
        web.loadUrl(HALAMAN)
    }

    fun tutup() { web.destroy() }

    private companion object { const val HALAMAN = "file:///android_asset/runner.html" }
}

@Composable
fun InterpreterScreen(kode: String, onKode: (String) -> Unit) {
    val ctx = LocalContext.current
    val runner = remember { PyRunner(ctx) }
    DisposableEffect(Unit) { onDispose { runner.tutup() } }
    var tfv by remember { mutableStateOf(TextFieldValue(kode)) }

    Halaman("Coba Kode") {
        Column(
            Modifier.fillMaxSize().imePadding().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // WebView dipasang 1dp agar timer JavaScript tidak di-throttle.
            AndroidView(factory = { runner.web }, modifier = Modifier.size(1.dp))

            OutlinedTextField(
                value = tfv,
                onValueChange = { tfv = it; onKode(it.text) },
                modifier = Modifier.fillMaxWidth().weight(1f),
                textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    capitalization = KeyboardCapitalization.None,
                ),
                label = { Text("Kode Python") },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { runner.jalankan(tfv.text) },
                    enabled = runner.siap && !runner.berjalan,
                ) {
                    Icon(Icons.Default.PlayArrow, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Jalankan")
                }
                if (runner.berjalan) {
                    OutlinedButton(onClick = { runner.berhenti() }) { Text("Berhenti") }
                }
                OutlinedButton(onClick = {
                    val a = tfv.selection.min
                    val b = tfv.selection.max
                    tfv = TextFieldValue(tfv.text.replaceRange(a, b, "    "), TextRange(a + 4))
                    onKode(tfv.text)
                }) { Text("Indent") }
            }
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) {
                Text(
                    if (runner.output.isEmpty()) "Keluaran akan muncul di sini…" else runner.output,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(12.dp),
                )
            }
        }
    }

    runner.prompt?.let { p ->
        var jawaban by remember(p) { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { runner.jawab("") },
            title = { Text("Input") },
            text = {
                Column {
                    Text(p)
                    OutlinedTextField(value = jawaban, onValueChange = { jawaban = it }, singleLine = true)
                }
            },
            confirmButton = { TextButton(onClick = { runner.jawab(jawaban) }) { Text("OK") } },
        )
    }
}
