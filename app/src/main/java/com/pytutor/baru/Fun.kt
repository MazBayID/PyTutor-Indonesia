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
import androidx.compose.ui.graphics.Brush
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
    val warna = listOf(Color(0xFFFFD43B), Color(0xFF55E6FF), Color(0xFFFF7D8D), Color(0xFF66D9A8), Color(0xFFB69CFF))
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
            Text(
                if (i < bintang) "★" else "☆",
                color = if (i < bintang) Color(0xFFFFC66D) else MaterialTheme.colorScheme.outline,
                fontSize = 48.sp,
                modifier = Modifier.scale(skala),
            )
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
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text("🐍", Modifier.padding(12.dp), fontSize = 38.sp)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("WELCOME BACK, EXPLORER", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Text(pesan, style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("LEVEL ${k.level}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        Text("🔥 ${k.streakTampil} hari", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.tertiary)
                    }
                    LinearProgressIndicator(
                        progress = { progres },
                        modifier = Modifier.fillMaxWidth().height(7.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.background,
                    )
                    Text("$xpLevel / 100 XP", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Box(
                Modifier.align(Alignment.TopEnd).size(64.dp),
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color(0x3355E6FF), Color.Transparent),
                            center = Offset(size.width, 0f),
                            radius = size.minDimension,
                        ),
                        radius = size.minDimension,
                        center = Offset(size.width, 0f),
                    )
                }
            }
        }
    }
}

@Composable
fun BarisKode(teks: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            teks,
            fontFamily = CodeFont,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
        )
    }
}
