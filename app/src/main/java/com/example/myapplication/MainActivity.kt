package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.content.Intent
import android.net.Uri
import android.content.Context
import androidx.compose.ui.platform.LocalContext

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, schedule work
            scheduleLiveCheck()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize Notification Channel
        LiveNotificationWorker.createNotificationChannel(this)
        
        // Request Permission (Android 13+)
        checkNotificationPermission()

        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    scheduleLiveCheck()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            scheduleLiveCheck()
        }
    }

    private fun scheduleLiveCheck() {
        val workRequest = PeriodicWorkRequestBuilder<LiveNotificationWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "LiveNotificationWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    var showNotificationHistory by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("holo_fan_prefs", Context.MODE_PRIVATE) }
    
    var isDarkMode by remember { 
        mutableStateOf(sharedPrefs.getBoolean("dark_mode_enabled", false)) 
    }

    var appLanguage by remember {
        mutableStateOf(sharedPrefs.getString("app_language", "ภาษาไทย") ?: "ภาษาไทย")
    }

    val strings = if (appLanguage == "English") EnglishStrings else ThaiStrings
    
    CompositionLocalProvider(LocalStrings provides strings) {
        MyApplicationTheme(darkTheme = isDarkMode) {
            if (showNotificationHistory) {
                NotificationHistoryScreen(onBack = { showNotificationHistory = false })
            } else {
                Scaffold(
                    bottomBar = {
                        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Home, contentDescription = strings.home) },
                                label = { Text(strings.home) },
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.DateRange, contentDescription = strings.schedule) },
                                label = { Text(strings.schedule) },
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Person, contentDescription = strings.talents) },
                                label = { Text(strings.talents) },
                                selected = false,
                                onClick = { 
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hololive.hololivepro.com/en/talents"))
                                    context.startActivity(intent)
                                },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = strings.merch) },
                                label = { Text(strings.merch) },
                                selected = false,
                                onClick = { 
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://shop.hololivepro.com/"))
                                    context.startActivity(intent)
                                },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Settings, contentDescription = strings.settings) },
                                label = { Text(strings.settings) },
                                selected = selectedTab == 4,
                                onClick = { selectedTab = 4 },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (selectedTab) {
                            0 -> HomeScreen()
                            1 -> ScheduleScreen(onNotificationClick = { showNotificationHistory = true })
                            4 -> SettingsScreen(
                                onDarkModeToggle = { isDarkMode = it },
                                onLanguageToggle = { appLanguage = it },
                                onNotificationToggle = { enabled ->
                                    if (enabled) {
                                        val workRequest = PeriodicWorkRequestBuilder<LiveNotificationWorker>(15, TimeUnit.MINUTES).build()
                                        WorkManager.getInstance(context).enqueueUniquePeriodicWork("LiveNotificationWork", ExistingPeriodicWorkPolicy.KEEP, workRequest)
                                    } else {
                                        WorkManager.getInstance(context).cancelUniqueWork("LiveNotificationWork")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

