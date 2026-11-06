package com.pytutor.baru

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
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
    val clipboard = LocalClipboardManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val runner = remember { PyRunner(ctx) }
    DisposableEffect(Unit) { onDispose { runner.tutup() } }

    var tfv by remember { mutableStateOf(TextFieldValue(kode)) }
    var ctrlAktif by remember { mutableStateOf(false) }
    var altAktif by remember { mutableStateOf(false) }
    val undo = remember { mutableStateListOf<TextFieldValue>() }
    val redo = remember { mutableStateListOf<TextFieldValue>() }

    fun ubah(n: TextFieldValue, simpanUndo: Boolean = true) {
        if (n.text != tfv.text && simpanUndo) {
            undo.add(tfv)
            if (undo.size > 100) undo.removeAt(0)
            redo.clear()
        }
        tfv = n
        onKode(n.text)
    }

    fun sisip(teks: String, posisiDalam: Int = teks.length) {
        val a = tfv.selection.min
        val b = tfv.selection.max
        val baru = tfv.text.replaceRange(a, b, teks)
        ubah(TextFieldValue(baru, TextRange(a + posisiDalam)))
    }

    fun geserKursor(arah: String) {
        val pos = tfv.selection.end.coerceIn(0, tfv.text.length)
        val target = when {
            altAktif && arah == "←" -> tfv.text.lastIndexOfAny(charArrayOf(' ', '\n', '\t'), (pos - 1).coerceAtLeast(0)).let { if (it < 0) 0 else it + 1 }
            altAktif && arah == "→" -> tfv.text.indexOfAny(charArrayOf(' ', '\n', '\t'), pos).let { if (it < 0) tfv.text.length else it + 1 }
            arah == "←" -> (pos - 1).coerceAtLeast(0)
            arah == "→" -> (pos + 1).coerceAtMost(tfv.text.length)
            arah == "↑" || arah == "↓" -> {
                val awal = tfv.text.lastIndexOf('\n', (pos - 1).coerceAtLeast(0)) + 1
                val kolom = pos - awal
                val startLine = if (arah == "↑") {
                    if (awal == 0) 0 else tfv.text.lastIndexOf('\n', awal - 2) + 1
                } else {
                    tfv.text.indexOf('\n', pos).let { if (it < 0) tfv.text.length else it + 1 }
                }
                val endLine = tfv.text.indexOf('\n', startLine).let { if (it < 0) tfv.text.length else it }
                (startLine + kolom).coerceAtMost(endLine)
            }
            else -> pos
        }
        ubah(tfv.copy(selection = TextRange(target)), false)
        ctrlAktif = false
        altAktif = false
    }

    fun tombol(label: String) {
        if (ctrlAktif) {
            when (label) {
                "A" -> tfv = tfv.copy(selection = TextRange(0, tfv.text.length))
                "Z" -> if (undo.isNotEmpty()) {
                    redo.add(tfv); tfv = undo.removeAt(undo.lastIndex); onKode(tfv.text)
                }
                "Y" -> if (redo.isNotEmpty()) {
                    undo.add(tfv); tfv = redo.removeAt(redo.lastIndex); onKode(tfv.text)
                }
                "C" -> {
                    val teks = if (tfv.selection.collapsed) tfv.text else tfv.text.substring(tfv.selection.min, tfv.selection.max)
                    clipboard.setText(AnnotatedString(teks))
                }
                "X" -> if (!tfv.selection.collapsed) {
                    clipboard.setText(AnnotatedString(tfv.text.substring(tfv.selection.min, tfv.selection.max)))
                    sisip("")
                }
                "V" -> sisip(clipboard.getText()?.text.orEmpty())
                else -> sisip(label)
            }
            ctrlAktif = false
            return
        }
        if (altAktif && label in listOf("←", "→")) {
            geserKursor(label)
            return
        }
        when (label) {
            "TAB" -> sisip("    ")
            "ESC" -> { ctrlAktif = false; altAktif = false; keyboard?.hide() }
            "CTRL" -> { ctrlAktif = true; altAktif = false }
            "ALT" -> { altAktif = true; ctrlAktif = false }
            "←", "→", "↑", "↓" -> geserKursor(label)
            "HOME" -> {
                val p = tfv.selection.end
                val target = tfv.text.lastIndexOf('\n', (p - 1).coerceAtLeast(0)) + 1
                ubah(tfv.copy(selection = TextRange(target)), false)
            }
            "END" -> {
                val p = tfv.selection.end
                val target = tfv.text.indexOf('\n', p).let { if (it < 0) tfv.text.length else it }
                ubah(tfv.copy(selection = TextRange(target)), false)
            }
            "()" -> sisip("()", 1)
            "[]" -> sisip("[]", 1)
            "{}" -> sisip("{}", 1)
            "\"\"" -> sisip("\"\"", 1)
            "''" -> sisip("''", 1)
            "==", "!=", "<=", ">=", ":", "#", "_", "->", " and ", " or " -> sisip(label)
            "UNDO" -> tombol("Z")
            "REDO" -> tombol("Y")
            else -> sisip(label)
        }
    }

    @Composable
    fun Key(label: String, width: androidx.compose.ui.unit.Dp = 48.dp) {
        val aktif = (label == "CTRL" && ctrlAktif) || (label == "ALT" && altAktif)
        FilledTonalButton(
            onClick = { tombol(label) },
            modifier = Modifier.width(width).height(40.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
            colors = if (aktif) ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else ButtonDefaults.filledTonalButtonColors(),
        ) { Text(label, fontFamily = FontFamily.Monospace, fontSize = 12.sp, maxLines = 1) }
    }

    Halaman("Code Lab") {
        Column(
            Modifier.fillMaxSize().imePadding().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            AndroidView(factory = { runner.web }, modifier = Modifier.size(1.dp))
            OutlinedTextField(
                value = tfv,
                onValueChange = { ubah(it); ctrlAktif = false; altAktif = false },
                modifier = Modifier.fillMaxWidth().weight(1f),
                textStyle = TextStyle(fontFamily = CodeFont, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface),
                visualTransformation = PythonSyntaxTransformation,
                keyboardOptions = KeyboardOptions(autoCorrectEnabled = false, capitalization = KeyboardCapitalization.None),
                label = { Text("Kode Python") },
            )
            Text("NEO CODING KEYS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                listOf("TAB", "ESC", "CTRL", "ALT", "←", "↑", "↓", "→", "HOME", "END").forEach { Key(it, if (it.length > 3) 58.dp else 48.dp) }
            }
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                listOf("()", "[]", "{}", "\"\"", "''", ":", "#", "==", "!=", "<=", ">=", "->", "_").forEach { Key(it, if (it.length > 2) 52.dp else 44.dp) }
            }
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                listOf("A", "Z", "Y", "C", "X", "V", "UNDO", "REDO").forEach { Key(it, if (it.length > 2) 58.dp else 44.dp) }
            }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { runner.jalankan(tfv.text); ctrlAktif = false; altAktif = false }, enabled = runner.siap && !runner.berjalan) {
                    Icon(Icons.Default.PlayArrow, null); Spacer(Modifier.width(4.dp)); Text("Jalankan")
                }
                if (runner.berjalan) OutlinedButton(onClick = { runner.berhenti() }) { Text("Berhenti") }
                OutlinedButton(onClick = { ubah(TextFieldValue("def main():\n    print(\"Halo, Python!\")\n\nmain()")) }) { Text("Contoh") }
                OutlinedButton(onClick = { ubah(TextFieldValue("")); ctrlAktif = false; altAktif = false }) { Text("Bersihkan") }
            }
            Text(if (!runner.siap) "MENYIAPKAN PYTHON…" else if (runner.berjalan) "PROGRAM BERJALAN" else "OUTPUT CONSOLE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(if (runner.output.isEmpty()) "Keluaran akan muncul di sini…" else runner.output, fontFamily = CodeFont, fontSize = 14.sp, modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(12.dp))
            }
        }
    }
    runner.prompt?.let { p ->
        var jawaban by remember(p) { mutableStateOf("") }
        AlertDialog(onDismissRequest = { runner.jawab("") }, title = { Text("Input") }, text = {
            Column { Text(p); OutlinedTextField(value = jawaban, onValueChange = { jawaban = it }, singleLine = true) }
        }, confirmButton = { TextButton(onClick = { runner.jawab(jawaban) }) { Text("OK") } })
    }
}
