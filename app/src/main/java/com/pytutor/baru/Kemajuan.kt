package com.pytutor.baru

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import java.time.LocalDate

/** Menyimpan bintang tiap topik, XP, level, dan streak harian di SharedPreferences. */
class Kemajuan(context: Context) {
    private val sp = context.getSharedPreferences("kemajuan", Context.MODE_PRIVATE)

    val bintang = mutableStateMapOf<String, Int>().apply {
        sp.all.forEach { (k, v) -> if (!k.startsWith("_") && v is Int) put(k, v) }
    }

    var xp by mutableIntStateOf(sp.getInt("_xp", 0))
        private set
    private var streak = sp.getInt("_streak", 0)
    private var terakhir = sp.getString("_terakhir", "") ?: ""

    val level: Int get() = xp / 100 + 1

    /** Streak yang masih berlaku (aktif hari ini atau kemarin). */
    val streakTampil: Int
        get() {
            val hariIni = LocalDate.now().toString()
            val kemarin = LocalDate.now().minusDays(1).toString()
            return if (terakhir == hariIni || terakhir == kemarin) streak else 0
        }

    fun tambahXp(jumlah: Int) {
        xp += jumlah
        sp.edit().putInt("_xp", xp).apply()
        tandaiAktif()
    }

    private fun tandaiAktif() {
        val hariIni = LocalDate.now().toString()
        if (terakhir == hariIni) return
        val kemarin = LocalDate.now().minusDays(1).toString()
        streak = if (terakhir == kemarin) streak + 1 else 1
        terakhir = hariIni
        sp.edit().putInt("_streak", streak).putString("_terakhir", hariIni).apply()
    }

    fun simpan(id: String, jumlah: Int) {
        val terbaik = maxOf(jumlah, bintang[id] ?: 0)
        bintang[id] = terbaik
        sp.edit().putInt(id, terbaik).apply()
    }

    fun terbuka(index: Int): Boolean =
        index == 0 || bintang.containsKey(Konten.topik[index - 1].id)
}
