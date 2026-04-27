package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.content.Context
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingsScreen(
    onNotificationToggle: (Boolean) -> Unit = {},
    onDarkModeToggle: (Boolean) -> Unit = {},
    onLanguageToggle: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val strings = LocalStrings.current
    val sharedPrefs = remember { context.getSharedPreferences("holo_fan_prefs", Context.MODE_PRIVATE) }
    
    var notificationEnabled by remember { 
        mutableStateOf(sharedPrefs.getBoolean("notification_enabled", true)) 
    }
    var darkModeEnabled by remember { 
        mutableStateOf(sharedPrefs.getBoolean("dark_mode_enabled", false)) 
    }

    val updateNotification = { enabled: Boolean ->
        notificationEnabled = enabled
        sharedPrefs.edit().putBoolean("notification_enabled", enabled).apply()
        onNotificationToggle(enabled)
    }

    val updateDarkMode = { enabled: Boolean ->
        darkModeEnabled = enabled
        sharedPrefs.edit().putBoolean("dark_mode_enabled", enabled).apply()
        onDarkModeToggle(enabled)
    }

    var showLanguageDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { 
        mutableStateOf(sharedPrefs.getString("app_language", "ภาษาไทย")) 
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(strings.selectLanguage) },
            text = {
                Column {
                    listOf("ภาษาไทย", "English").forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLanguage = lang
                                    sharedPrefs.edit().putString("app_language", lang).apply()
                                    onLanguageToggle(lang)
                                    showLanguageDialog = false
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selectedLanguage == lang, onClick = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(lang)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) { Text(strings.cancel) }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header / Profile Section
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = PrimaryBlue.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("💙", fontSize = 40.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Holo-Fan Companion", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = TextDark)
            Text("v1.2.0 Premium", color = TextLight, fontSize = 12.sp)
        }

        Text(
            "Preferences", 
            fontWeight = FontWeight.Bold, 
            color = TextLight, 
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                SettingItem(
                    icon = Icons.Default.Notifications,
                    title = strings.liveNotification,
                    trailing = {
                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = { updateNotification(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
                        )
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray.copy(alpha = 0.1f))
                SettingItem(
                    icon = Icons.Default.Settings,
                    title = strings.darkMode,
                    trailing = {
                        Switch(
                            checked = darkModeEnabled,
                            onCheckedChange = { updateDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
                        )
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray.copy(alpha = 0.1f))
                SettingItem(
                    icon = Icons.Default.Edit,
                    title = strings.language,
                    subtitle = selectedLanguage,
                    onClick = { showLanguageDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Support", 
            fontWeight = FontWeight.Bold, 
            color = TextLight, 
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                SettingItem(
                    icon = Icons.Default.Info,
                    title = strings.aboutApp,
                    onClick = { /* TODO */ }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray.copy(alpha = 0.1f))
                SettingItem(
                    icon = Icons.Default.Share,
                    title = "Share with Friends",
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(12.dp),
            color = PrimaryBlue.copy(alpha = 0.05f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = TextDark, fontSize = 15.sp)
            if (subtitle != null) {
                Text(subtitle, color = TextLight, fontSize = 12.sp)
            }
        }
        if (trailing != null) {
            trailing()
        } else if (onClick != null) {
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextLight.copy(alpha = 0.5f))
        }
    }
}
