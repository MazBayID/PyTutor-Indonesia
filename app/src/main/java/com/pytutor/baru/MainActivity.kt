package com.pytutor.baru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

private val NeoBackground = Color(0xFF07111F)
private val NeoCyan = Color(0xFF55E6FF)
private val NeoViolet = Color(0xFFB69CFF)

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
    val scheme = darkColorScheme(
        primary = NeoCyan,
        onPrimary = Color(0xFF00212A),
        primaryContainer = Color(0xFF103B4A),
        onPrimaryContainer = Color(0xFFC7F7FF),
        secondary = NeoViolet,
        onSecondary = Color(0xFF241644),
        secondaryContainer = Color(0xFF30264F),
        onSecondaryContainer = Color(0xFFE9DEFF),
        tertiary = Color(0xFFFFC66D),
        background = NeoBackground,
        onBackground = Color(0xFFE8F4FF),
        surface = Color(0xFF0D1B2B),
        onSurface = Color(0xFFE8F4FF),
        surfaceVariant = Color(0xFF14283D),
        onSurfaceVariant = Color(0xFFB8CADB),
        outline = Color(0xFF45647D),
        error = Color(0xFFFF7D8D),
        errorContainer = Color(0xFF51232D),
        onErrorContainer = Color(0xFFFFD9DE),
    )
    MaterialTheme(colorScheme = scheme, content = content)
}

private data class Tab(val rute: String, val label: String, val ikon: Int)

@Composable
fun App(kemajuan: Kemajuan) {
    val nav = rememberNavController()
    var kodeEditor by rememberSaveable { mutableStateOf("print(\"Halo, Python!\")") }
    val rute = nav.currentBackStackEntryAsState().value?.destination?.route
    val tabs = listOf(
        Tab("belajar", "Belajar", R.drawable.ic_home),
        Tab("latihan", "Latihan", R.drawable.ic_idea),
        Tab("algoritma", "Algoritma", R.drawable.ic_trophy),
        Tab("coba", "Code Lab", R.drawable.ic_code),
    )
    val tampilBar = tabs.any { it.rute == rute }
    val intArg = { nama: String -> listOf(navArgument(nama) { type = NavType.IntType }) }
    val coba = { k: String -> kodeEditor = k; nav.navigate("coba") { launchSingleTop = true } }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (tampilBar) {
                NavigationBar(
                    containerColor = Color(0xFF0A1726),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 0.dp,
                ) {
                    tabs.forEach { t ->
                        NavigationBarItem(
                            selected = rute == t.rute,
                            onClick = { nav.navigate(t.rute) { popUpTo("belajar"); launchSingleTop = true } },
                            icon = { Icon(painterResource(t.ikon), contentDescription = null) },
                            label = { Text(t.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeoCyan,
                                selectedTextColor = NeoCyan,
                                indicatorColor = Color(0xFF123746),
                                unselectedIconColor = Color(0xFF8CA6BC),
                                unselectedTextColor = Color(0xFF8CA6BC),
                            ),
                        )
                    }
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
                LatihanScreen(it.arguments!!.getInt("i"), kemajuan, { nav.popBackStack() }, coba)
            }
            composable("algoritma") { AlgoritmaList { nav.navigate("algoritma/$it") } }
            composable("algoritma/{i}", intArg("i")) {
                AlgoritmaScreen(it.arguments!!.getInt("i"), { nav.popBackStack() }, coba)
            }
            composable("coba") { InterpreterScreen(kodeEditor) { kodeEditor = it } }
        }
    }
}
