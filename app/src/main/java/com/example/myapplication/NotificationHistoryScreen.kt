package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationHistoryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val strings = LocalStrings.current
    val sharedPrefs = remember { context.getSharedPreferences("holo_fan_prefs", Context.MODE_PRIVATE) }
    
    var historyList by remember { 
        mutableStateOf(loadHistory(sharedPrefs)) 
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.notificationHistory, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = strings.back)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        sharedPrefs.edit().remove("notification_history").apply()
                        historyList = emptyList()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear All", tint = LiveRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(strings.noScheduleArchive, color = TextLight)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(historyList.size) { index ->
                    val item = historyList[index]
                    NotificationHistoryCard(item)
                }
            }
        }
    }
}

data class NotificationEntry(
    val id: String,
    val channel: String,
    val title: String,
    val timestamp: Long
)

fun loadHistory(sharedPrefs: android.content.SharedPreferences): List<NotificationEntry> {
    val history = sharedPrefs.getStringSet("notification_history", emptySet()) ?: emptySet()
    return history.mapNotNull { entry ->
        val parts = entry.split("|")
        if (parts.size >= 4) {
            NotificationEntry(
                id = parts[0],
                channel = parts[1],
                title = parts[2],
                timestamp = parts[3].toLongOrNull() ?: 0L
            )
        } else null
    }.sortedByDescending { it.timestamp }
}

@Composable
fun NotificationHistoryCard(entry: NotificationEntry) {
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
    val dateString = sdf.format(Date(entry.timestamp))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(entry.channel, fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 14.sp)
                Text(dateString, fontSize = 12.sp, color = TextLight)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(entry.title, fontWeight = FontWeight.SemiBold, color = TextDark, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            val strings = LocalStrings.current
            Text(
                strings.memberLive,
                fontSize = 12.sp,
                color = TextMainBlue.copy(alpha = 0.7f)
            )
        }
    }
}
