@file:OptIn(ExperimentalMaterial3Api::class)

package com.pytutor.baru

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Halaman(judul: String, onBack: (() -> Unit)? = null, konten: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(judul) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali") }
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { pad -> Box(Modifier.padding(pad)) { konten() } }
}

@Composable
fun KodeBox(kode: String, onCoba: ((String) -> Unit)? = null) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                kode,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { Kepala(kemajuan) }
            itemsIndexed(Konten.topik) { i, t ->
                val terbuka = kemajuan.terbuka(i)
                val bintang = kemajuan.bintang[t.id] ?: 0
                Card(
                    onClick = { if (terbuka) onBuka(i) },
                    enabled = terbuka,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("${i + 1}. ${t.judul}", style = MaterialTheme.typography.titleMedium)
                            Text(t.deskripsi, style = MaterialTheme.typography.bodyMedium)
                        }
                        if (!terbuka) Icon(Icons.Default.Lock, "Terkunci")
                        else if (bintang > 0) Text("★".repeat(bintang) + "☆".repeat(3 - bintang))
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
    val haptic = LocalHapticFeedback.current
    val bintang = when { salah == 0 -> 3; salah <= 2 -> 2; else -> 1 }

    val onJawab: (Boolean) -> Unit = { benar ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
            Text("Topik selesai! 🎉", style = MaterialTheme.typography.headlineMedium)
            BintangAnimasi(bintang)
            Text("Kesalahan: $salah  •  Bonus +${bintang * 10} XP")
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Kembali ke daftar") }
            OutlinedButton(onClick = onUlang, modifier = Modifier.fillMaxWidth()) { Text("Ulangi") }
        }
        if (bintang >= 2) Konfeti(Modifier.fillMaxSize())
    }
}

@Composable
fun PelajaranView(p: Pelajaran, nomor: Int, total: Int, onCoba: (String) -> Unit, onLanjut: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        LinearProgressIndicator(progress = { nomor / total.toFloat() }, modifier = Modifier.fillMaxWidth())
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(p.judul, style = MaterialTheme.typography.headlineSmall)
            p.blok.forEach { b ->
                when (b) {
                    is Teks -> Text(b.isi)
                    is Kode -> KodeBox(b.kode, onCoba)
                }
            }
        }
        Button(onClick = onLanjut, modifier = Modifier.fillMaxWidth()) { Text("Lanjut") }
    }
}

@Composable
fun SoalView(s: Soal, nomor: Int, total: Int, onJawab: (Boolean) -> Unit, onLanjut: () -> Unit, terakhir: Boolean) {
    var pilih by remember(s) { mutableStateOf<Int?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        LinearProgressIndicator(progress = { nomor / total.toFloat() }, modifier = Modifier.fillMaxWidth())
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(if (s.kode != null) "Isi bagian kosong" else "Kuis", style = MaterialTheme.typography.labelLarge)
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
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(teks, Modifier.padding(16.dp)) }
            }
            if (pilih != null) {
                Text(if (pilih == s.jawaban) "Benar! +10 XP 🎉" else "Belum tepat. Jawaban yang benar sudah ditandai.")
            }
        }
        Button(onClick = onLanjut, enabled = pilih != null, modifier = Modifier.fillMaxWidth()) {
            Text(if (terakhir) "Selesai" else "Lanjut")
        }
    }
}

@Composable
fun SusunView(s: Susun, nomor: Int, total: Int, onJawab: (Boolean) -> Unit, onLanjut: () -> Unit, terakhir: Boolean) {
    val acak = remember(s) { s.urut.shuffled() }
    val dipilih = remember(s) { mutableStateListOf<Int>() }
    var hasil by remember(s) { mutableStateOf<Boolean?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        LinearProgressIndicator(progress = { nomor / total.toFloat() }, modifier = Modifier.fillMaxWidth())
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Susun kode", style = MaterialTheme.typography.labelLarge)
            Text(s.tanya, style = MaterialTheme.typography.titleMedium)
            Text("Jawabanmu (ketuk untuk melepas):", style = MaterialTheme.typography.labelMedium)
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().heightIn(min = 72.dp),
            ) {
                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (dipilih.isEmpty()) Text("Ketuk baris di bawah untuk menyusunnya di sini…")
                    dipilih.toList().forEach { idx ->
                        BarisKode(acak[idx]) {
                            if (hasil != true) { dipilih.removeAll { it == idx }; hasil = null }
                        }
                    }
                }
            }
            Text("Pilihan baris:", style = MaterialTheme.typography.labelMedium)
            acak.forEachIndexed { idx, baris ->
                if (idx !in dipilih) {
                    BarisKode(baris) {
                        if (hasil != true) { dipilih.add(idx); hasil = null }
                    }
                }
            }
            hasil?.let { Text(if (it) "Benar! +10 XP 🎉" else "Urutannya belum tepat. Coba susun ulang!") }
        }
        if (hasil == true) {
            Button(onClick = onLanjut, modifier = Modifier.fillMaxWidth()) {
                Text(if (terakhir) "Selesai" else "Lanjut")
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
            ) { Text("Periksa") }
        }
    }
}

// ---------- Latihan ----------

@Composable
fun LatihanList(onBuka: (Int) -> Unit) {
    Halaman("Latihan") {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(Konten.latihan) { i, l ->
                Card(onClick = { onBuka(i) }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(l.judul, style = MaterialTheme.typography.titleMedium)
                        Text("Tingkat: ${l.tingkat}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun LatihanScreen(index: Int, onBack: () -> Unit, onCoba: (String) -> Unit) {
    val l = Konten.latihan[index]
    var tampil by rememberSaveable { mutableStateOf(false) }
    Halaman(l.judul, onBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Tingkat: ${l.tingkat}", style = MaterialTheme.typography.labelLarge)
            Text(l.soal)
            Button(onClick = { onCoba("# Tulis solusimu di sini\n") }, modifier = Modifier.fillMaxWidth()) {
                Text("Kerjakan di editor")
            }
            OutlinedButton(onClick = { tampil = !tampil }, modifier = Modifier.fillMaxWidth()) {
                Text(if (tampil) "Sembunyikan solusi" else "Lihat solusi")
            }
            if (tampil) KodeBox(l.solusi, onCoba)
        }
    }
}

// ---------- Algoritma ----------

@Composable
fun AlgoritmaList(onBuka: (Int) -> Unit) {
    Halaman("Algoritma") {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(Konten.algoritma) { i, a ->
                Card(onClick = { onBuka(i) }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(a.judul, style = MaterialTheme.typography.titleMedium)
                        Text(a.deskripsi, style = MaterialTheme.typography.bodyMedium)
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
            Text(a.deskripsi)
            KodeBox(a.kode, onCoba)
        }
    }
}
