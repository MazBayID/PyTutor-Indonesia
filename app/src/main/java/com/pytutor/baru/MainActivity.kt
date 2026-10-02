package com.pytutor.baru

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val kemajuan = Kemajuan(applicationContext)
        setContent { PyTutorTheme { App(kemajuan) } }
    }
}

@Composable
fun PyTutorTheme(content: @Composable () -> Unit) {
    val gelap = isSystemInDarkTheme()
    val scheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (gelap) dynamicDarkColorScheme(LocalContext.current) else dynamicLightColorScheme(LocalContext.current)
        gelap -> darkColorScheme(primary = Color(0xFF8FB8DE))
        else -> lightColorScheme(primary = Color(0xFF306998))
    }
    MaterialTheme(colorScheme = scheme, content = content)
}

private data class Tab(val rute: String, val label: String, val ikon: ImageVector)

@Composable
fun App(kemajuan: Kemajuan) {
    val nav = rememberNavController()
    var kodeEditor by rememberSaveable { mutableStateOf("print(\"Halo, Python!\")") }
    val rute = nav.currentBackStackEntryAsState().value?.destination?.route
    val tabs = listOf(
        Tab("belajar", "Belajar", Icons.Default.Home),
        Tab("latihan", "Latihan", Icons.Default.Edit),
        Tab("algoritma", "Algoritma", Icons.Default.List),
        Tab("coba", "Coba Kode", Icons.Default.PlayArrow),
    )
    val tampilBar = tabs.any { it.rute == rute }
    val intArg = { nama: String -> listOf(navArgument(nama) { type = NavType.IntType }) }
    val coba = { k: String -> kodeEditor = k; nav.navigate("coba") { launchSingleTop = true } }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (tampilBar) NavigationBar {
                tabs.forEach { t ->
                    NavigationBarItem(
                        selected = rute == t.rute,
                        onClick = { nav.navigate(t.rute) { popUpTo("belajar"); launchSingleTop = true } },
                        icon = { Icon(t.ikon, null) },
                        label = { Text(t.label) },
                    )
                }
            }
        },
    ) { pad ->
        val mod = Modifier.padding(pad).let { if (tampilBar) it else it.navigationBarsPadding() }
        NavHost(nav, startDestination = "belajar", modifier = mod) {
            composable("belajar") { TopikList(kemajuan) { nav.navigate("topik/$it") } }
            composable("topik/{i}", intArg("i")) {
                TopikScreen(it.arguments!!.getInt("i"), kemajuan, { nav.popBackStack() }, coba)
            }
            composable("latihan") { LatihanList { nav.navigate("latihan/$it") } }
            composable("latihan/{i}", intArg("i")) {
                LatihanScreen(it.arguments!!.getInt("i"), { nav.popBackStack() }, coba)
            }
            composable("algoritma") { AlgoritmaList { nav.navigate("algoritma/$it") } }
            composable("algoritma/{i}", intArg("i")) {
                AlgoritmaScreen(it.arguments!!.getInt("i"), { nav.popBackStack() }, coba)
            }
            composable("coba") { InterpreterScreen(kodeEditor) { kodeEditor = it } }
        }
    }
}
