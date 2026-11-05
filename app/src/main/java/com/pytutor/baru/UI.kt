@file:OptIn(ExperimentalMaterial3Api::class)

package com.pytutor.baru

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NeonBorder = androidx.compose.ui.graphics.Color(0xFF28516B)

@Composable
fun Halaman(judul: String, onBack: (() -> Unit)? = null, konten: @Composable () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(judul, style = MaterialTheme.typography.titleLarge)
                        Text(
                            "PYTUTOR INDONESIA  /  LEARNING SYSTEM",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                        )
                    }
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { pad ->
        Box(
            Modifier.padding(pad).fillMaxSize().background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background,
                    ),
                ),
            ),
        ) { konten() }
    }
}

@Composable
fun KodeBox(kode: String, onCoba: ((String) -> Unit)? = null) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("PYTHON", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.weight(1f))
                Text("MONOSPACE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                highlightPython(kode),
                fontFamily = CodeFont,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            )
            if (onCoba != null) {
                TextButton(onClick = { onCoba(kode) }) {
                    Icon(Icons.Default.PlayArrow, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Coba jalankan")
                }
            }
        }
    }
}

// ---------- Belajar ----------

@Composable
fun TopikList(kemajuan: Kemajuan, onBuka: (Int) -> Unit) {
    Halaman("Belajar Python") {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { Kepala(kemajuan) }
            item { GalaxyPanel(kemajuan) }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("LEARNING PATH", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Text("Pilih sektor untuk memulai perjalananmu.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            itemsIndexed(Konten.topik) { i, t ->
                val terbuka = kemajuan.terbuka(i)
                val bintang = kemajuan.bintang[t.id] ?: 0
                Card(
                    onClick = { if (terbuka) onBuka(i) },
                    enabled = terbuka,
                    modifier = Modifier.fillMaxWidth().border(
                        1.dp,
                        if (terbuka) NeonBorder else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                        RoundedCornerShape(18.dp),
                    ),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (terbuka) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
                        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
                    ),
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(13.dp),
                    ) {
                        Surface(
                            color = if (terbuka) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(13.dp),
                        ) {
                            Text(
                                "%02d".format(i + 1),
                                Modifier.padding(horizontal = 13.dp, vertical = 11.dp),
                                color = if (terbuka) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(t.judul, style = MaterialTheme.typography.titleMedium)
                            Text(t.deskripsi, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (bintang > 0) {
                                Text("★".repeat(bintang) + "☆".repeat(3 - bintang), color = MaterialTheme.colorScheme.tertiary)
                            }
                        }
                        if (!terbuka) Icon(Icons.Default.Lock, "Terkunci", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        else Text("›", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun TopikScreen(index: Int, kemajuan: Kemajuan, onBack: () -> Unit, onCoba: (String) -> Unit) {
    val t = Konten.topik[index]
    val nP = t.pelajaran.size
    val nK = t.kuis.size
    val total = nP + nK + t.susun.size
    var langkah by rememberSaveable { mutableIntStateOf(0) }
    var salah by rememberSaveable { mutableIntStateOf(0) }
    var selesai by rememberSaveable { mutableStateOf(false) }
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val bintang = when { salah == 0 -> 3; salah <= 2 -> 2; else -> 1 }

    val onJawab: (Boolean) -> Unit = { benar ->
        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
        if (benar) kemajuan.tambahXp(10) else salah++
    }
    val lanjut: () -> Unit = {
        if (langkah + 1 >= total) {
            kemajuan.simpan(t.id, bintang)
            kemajuan.tambahXp(bintang * 10)
            selesai = true
        } else {
            langkah++
        }
    }

    Halaman(t.judul, onBack) {
        Crossfade(targetState = if (selesai) -1 else langkah, label = "langkah") { s ->
            when {
                s == -1 -> Selesai(salah, bintang, onBack) { langkah = 0; salah = 0; selesai = false }
                s < nP -> PelajaranView(t.pelajaran[s], s, total, onCoba, lanjut)
                s < nP + nK -> SoalView(t.kuis[s - nP], s, total, onJawab, lanjut, s + 1 >= total)
                else -> SusunView(t.susun[s - nP - nK], s, total, onJawab, lanjut, s + 1 >= total)
            }
        }
    }
}

@Composable
fun Selesai(salah: Int, bintang: Int, onBack: () -> Unit, onUlang: () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("MISI SELESAI", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Topik selesai! 🎉", style = MaterialTheme.typography.headlineMedium)
            BintangAnimasi(bintang)
            Text("Kesalahan: $salah  •  Bonus +${bintang * 10} XP", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Kembali ke daftar") }
            OutlinedButton(onClick = onUlang, modifier = Modifier.fillMaxWidth()) { Text("Ulangi misi") }
        }
        if (bintang >= 2) Konfeti(Modifier.fillMaxSize())
    }
}

@Composable
fun PelajaranView(p: Pelajaran, nomor: Int, total: Int, onCoba: (String) -> Unit, onLanjut: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("PROGRESS  /  ${nomor + 1} DARI $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        LinearProgressIndicator(
            progress = { (nomor + 1) / total.toFloat() },
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(p.judul, style = MaterialTheme.typography.headlineSmall)
            p.blok.forEach { b ->
                when (b) {
                    is Teks -> Text(b.isi, style = MaterialTheme.typography.bodyLarge, lineHeight = 25.sp)
                    is Kode -> KodeBox(b.kode, onCoba)
                }
            }
        }
        Button(onClick = onLanjut, modifier = Modifier.fillMaxWidth()) { Text("Lanjutkan") }
    }
}

@Composable
fun SoalView(s: Soal, nomor: Int, total: Int, onJawab: (Boolean) -> Unit, onLanjut: () -> Unit, terakhir: Boolean) {
    var pilih by remember(s) { mutableStateOf<Int?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("KNOWLEDGE CHECK  /  ${nomor + 1} DARI $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        LinearProgressIndicator(
            progress = { (nomor + 1) / total.toFloat() },
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(if (s.kode != null) "ISI BAGIAN KOSONG" else "KUIS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
            Text(s.tanya, style = MaterialTheme.typography.titleMedium)
            s.kode?.let { KodeBox(it) }
            s.pilihan.forEachIndexed { i, teks ->
                val target = when {
                    pilih == null -> MaterialTheme.colorScheme.surfaceVariant
                    i == s.jawaban -> MaterialTheme.colorScheme.primaryContainer
                    i == pilih -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                val warna by animateColorAsState(target, label = "pilihan")
                Surface(
                    onClick = {
                        if (pilih == null) {
                            pilih = i
                            onJawab(i == s.jawaban)
                        }
                    },
                    color = warna,
                    shape = RoundedCornerShape(13.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)),
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(teks, Modifier.padding(16.dp)) }
            }
            if (pilih != null) {
                Text(
                    if (pilih == s.jawaban) "Benar! +10 XP 🎉" else "Belum tepat. Jawaban yang benar sudah ditandai.",
                    color = if (pilih == s.jawaban) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                )
            }
        }
        Button(onClick = onLanjut, enabled = pilih != null, modifier = Modifier.fillMaxWidth()) {
            Text(if (terakhir) "Selesaikan topik" else "Lanjut")
        }
    }
}

@Composable
fun SusunView(s: Susun, nomor: Int, total: Int, onJawab: (Boolean) -> Unit, onLanjut: () -> Unit, terakhir: Boolean) {
    val acak = remember(s) { s.urut.shuffled() }
    val dipilih = remember(s) { mutableStateListOf<Int>() }
    var hasil by remember(s) { mutableStateOf<Boolean?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("CODE PUZZLE  /  ${nomor + 1} DARI $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        LinearProgressIndicator(
            progress = { (nomor + 1) / total.toFloat() },
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Susun kode", style = MaterialTheme.typography.titleLarge)
            Text(s.tanya, style = MaterialTheme.typography.bodyMedium)
            Text("JAWABANMU (KETUK UNTUK MELEPAS)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth().heightIn(min = 72.dp),
            ) {
                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (dipilih.isEmpty()) Text("Ketuk baris di bawah untuk menyusunnya di sini…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    dipilih.toList().forEach { idx ->
                        BarisKode(acak[idx]) {
                            if (hasil != true) { dipilih.removeAll { it == idx }; hasil = null }
                        }
                    }
                }
            }
            Text("PILIHAN BARIS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            acak.forEachIndexed { idx, baris ->
                if (idx !in dipilih) {
                    BarisKode(baris) {
                        if (hasil != true) { dipilih.add(idx); hasil = null }
                    }
                }
            }
            hasil?.let {
                Text(
                    if (it) "Benar! +10 XP 🎉" else "Urutannya belum tepat. Coba susun ulang!",
                    color = if (it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                )
            }
        }
        if (hasil == true) {
            Button(onClick = onLanjut, modifier = Modifier.fillMaxWidth()) {
                Text(if (terakhir) "Selesaikan topik" else "Lanjut")
            }
        } else {
            Button(
                onClick = {
                    val benar = dipilih.map { acak[it] } == s.urut
                    hasil = benar
                    onJawab(benar)
                },
                enabled = dipilih.size == acak.size,
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Periksa jawaban") }
        }
    }
}

// ---------- Latihan ----------

@Composable
fun LatihanList(onBuka: (Int) -> Unit) {
    Halaman("Latihan") {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("PRACTICE BAY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text("Uji kemampuanmu lewat tantangan coding.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            itemsIndexed(Konten.latihan) { i, l ->
                Card(
                    onClick = { onBuka(i) },
                    modifier = Modifier.fillMaxWidth().border(1.dp, NeonBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape) {
                            Text("${i + 1}", Modifier.padding(12.dp), color = MaterialTheme.colorScheme.secondary)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(l.judul, style = MaterialTheme.typography.titleMedium)
                            Text("TINGKAT: ${l.tingkat.uppercase()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("›", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun LatihanScreen(index: Int, onBack: () -> Unit, onCoba: (String) -> Unit) {
    val l = Konten.latihan[index]
    val context = LocalContext.current
    val runner = remember(l.judul) { PyRunner(context) }
    DisposableEffect(runner) { onDispose { runner.tutup() } }
    var kode by remember(l.judul) { mutableStateOf("# Tulis solusi Python-mu di sini\n") }
    var tampilSolusi by rememberSaveable { mutableStateOf(false) }
    var sudahDijalankan by remember(l.judul) { mutableStateOf(false) }
    var inputIndex by remember(l.judul) { mutableIntStateOf(0) }
    var lulus by remember(l.judul) { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(runner.prompt) {
        if (runner.prompt != null && inputIndex < l.inputUji.size) {
            val jawabanUji = l.inputUji[inputIndex]
            inputIndex++
            runner.jawab(jawabanUji)
        }
    }
    LaunchedEffect(runner.berjalan, runner.output) {
        if (sudahDijalankan && !runner.berjalan && runner.siap && runner.output.isNotEmpty()) {
            lulus = runner.output.trim() == l.hasilDiharapkan.trim()
        }
    }

    Halaman(l.judul, onBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AndroidView(factory = { runner.web }, modifier = Modifier.size(1.dp))
            Text("PRACTICE MISSION  /  \${l.tingkat.uppercase()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            Text(l.soal, style = MaterialTheme.typography.bodyLarge, lineHeight = 25.sp)
            Text("CODE EDITOR", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = kode,
                onValueChange = { kode = it; lulus = null },
                modifier = Modifier.fillMaxWidth().heightIn(min = 180.dp),
                textStyle = TextStyle(fontFamily = CodeFont, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface),
                visualTransformation = PythonSyntaxTransformation,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    autoCorrectEnabled = false,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.None,
                ),
                label = { Text("Tulis kode Python") },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        inputIndex = 0
                        lulus = null
                        sudahDijalankan = true
                        runner.jalankan(kode)
                    },
                    enabled = runner.siap && !runner.berjalan,
                ) {
                    Text(if (runner.berjalan) "Menjalankan…" else "Uji jawaban")
                }
                OutlinedButton(onClick = { onCoba(kode) }) { Text("Code Lab") }
            }
            Text(
                if (!runner.siap) "MENYIAPKAN PYTHON…" else "TES OTOMATIS DENGAN INPUT CONTOH",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            lulus?.let { benar ->
                Surface(
                    color = if (benar) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        if (benar) "LULUS! Output sesuai hasil yang diharapkan. +20 XP" else "Belum cocok. Periksa kode dan output, lalu coba lagi.",
                        modifier = Modifier.padding(12.dp),
                        color = if (benar) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
            Text("OUTPUT CONSOLE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    runner.output.ifEmpty { "Output program akan muncul di sini…" },
                    fontFamily = CodeFont,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                )
            }
            OutlinedButton(onClick = { tampilSolusi = !tampilSolusi }, modifier = Modifier.fillMaxWidth()) {
                Text(if (tampilSolusi) "Sembunyikan solusi" else "Lihat solusi")
            }
            if (tampilSolusi) KodeBox(l.solusi)
        }
    }

    runner.prompt?.let { prompt ->
        if (inputIndex >= l.inputUji.size) {
            var jawaban by remember(prompt) { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { runner.jawab("") },
                title = { Text("Input Python") },
                text = {
                    Column {
                        Text(prompt)
                        OutlinedTextField(value = jawaban, onValueChange = { jawaban = it }, singleLine = true)
                    }
                },
                confirmButton = { TextButton(onClick = { runner.jawab(jawaban) }) { Text("Kirim") } },
            )
        }
    }
}

// ---------- Algoritma ----------

@Composable
fun AlgoritmaList(onBuka: (Int) -> Unit) {
    Halaman("Algoritma") {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("ALGORITHM ARCHIVE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text("Pelajari cara program memecahkan masalah.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            itemsIndexed(Konten.algoritma) { i, a ->
                Card(
                    onClick = { onBuka(i) },
                    modifier = Modifier.fillMaxWidth().border(1.dp, NeonBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("MODULE ${(i + 1).toString().padStart(2, '0')}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        Text(a.judul, style = MaterialTheme.typography.titleMedium)
                        Text(a.deskripsi, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun AlgoritmaScreen(index: Int, onBack: () -> Unit, onCoba: (String) -> Unit) {
    val a = Konten.algoritma[index]
    Halaman(a.judul, onBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("ALGORITHM MODULE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Text(a.deskripsi, style = MaterialTheme.typography.bodyLarge, lineHeight = 25.sp)
            if (a.judul == "Bubble Sort") BubbleSortVisualizer()
            KodeBox(a.kode, onCoba)
        }
    }
}
