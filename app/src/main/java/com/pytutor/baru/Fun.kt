package com.pytutor.baru

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin
import kotlin.random.Random
import kotlinx.coroutines.delay

/** Hujan konfeti selama ~2,5 detik. */
@Composable
fun Konfeti(modifier: Modifier = Modifier) {
    val warna = listOf(Color(0xFFFFD43B), Color(0xFF306998), Color(0xFFEF5350), Color(0xFF66BB6A), Color(0xFFAB47BC))
    val partikel = remember { List(40) { Triple(Random.nextFloat(), 0.5f + Random.nextFloat(), warna[it % warna.size]) } }
    val t = remember { Animatable(0f) }
    LaunchedEffect(Unit) { t.animateTo(1f, tween(2500, easing = LinearEasing)) }
    Canvas(modifier) {
        if (t.value < 1f) {
            partikel.forEachIndexed { i, (x, kecepatan, w) ->
                val y = t.value * kecepatan * 1.4f * size.height - 20f
                val geser = sin(t.value * 6f + i) * 30f
                drawCircle(w, radius = 8f + (i % 3) * 3f, center = Offset(x * size.width + geser, y))
            }
        }
    }
}

/** Bintang muncul satu per satu dengan efek memantul. */
@Composable
fun BintangAnimasi(bintang: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(3) { i ->
            var tampil by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { delay(300L * (i + 1)); tampil = true }
            val skala by animateFloatAsState(
                if (tampil) 1f else 0f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "bintang",
            )
            Text(if (i < bintang) "⭐" else "☆", fontSize = 48.sp, modifier = Modifier.scale(skala))
        }
    }
}

/** Kartu maskot Pipi + level, XP, dan streak. */
@Composable
fun Kepala(k: Kemajuan) {
    val xpLevel = k.xp % 100
    val progres by animateFloatAsState(xpLevel / 100f, label = "xp")
    val pesan = when {
        k.xp == 0 -> "Halo! Aku Pipi. Ayo mulai belajar Python!"
        k.streakTampil >= 3 -> "Kamu lagi on fire! Jangan putus ya 🔥"
        else -> "Semangat! Sedikit lagi naik level."
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("🐍", fontSize = 44.sp)
            Spacer(Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(pesan, style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Level ${k.level}", style = MaterialTheme.typography.titleMedium)
                    Text("🔥 ${k.streakTampil} hari", style = MaterialTheme.typography.titleMedium)
                }
                LinearProgressIndicator(progress = { progres }, modifier = Modifier.fillMaxWidth())
                Text("$xpLevel / 100 XP", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun BarisKode(teks: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            teks,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
        )
    }
}
