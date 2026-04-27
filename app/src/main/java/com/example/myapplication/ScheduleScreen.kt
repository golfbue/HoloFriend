package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.*
import androidx.compose.ui.draw.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = viewModel(),
    onNotificationClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current
    val uriHandler = LocalUriHandler.current
    val dateTabs = listOf(strings.today, strings.soon, strings.archive)
    var selectedDateTab by remember { mutableStateOf(0) }
    
    val typeFilters = listOf(strings.all, strings.liveStream, strings.clip, strings.shorts)
    var selectedTypeFilter by remember { mutableStateOf(0) }

    // Search logic
    var searchQuery by remember { mutableStateOf("") }

    // Refresh when entering the screen
    LaunchedEffect(Unit) {
        viewModel.refreshSchedule()
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Logo", tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Holo Fan Companion", fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 20.sp)
            }
            Row {
                IconButton(onClick = { viewModel.refreshSchedule() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = PrimaryBlue)
                }
                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = PrimaryBlue)
                }
            }
        }

        // Search Bar (Functional)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search streams or talents...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryBlue) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                focusedBorderColor = PrimaryBlue,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Today/Upcoming/Archive Switch (Pill Style)
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(44.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                dateTabs.forEachIndexed { index, title ->
                    val isSelected = selectedDateTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (isSelected) PrimaryBlue else Color.Transparent)
                            .clickable { selectedDateTab = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            title, 
                            color = if (isSelected) Color.White else TextDark, 
                            fontWeight = FontWeight.ExtraBold, 
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Type Filters (Chips)
        var showTopicDialog by remember { mutableStateOf(false) }
        var selectedTopic by remember { mutableStateOf<String?>(null) }
        val topics = listOf("Gaming", "Singing", "ASMR", "Chatting", "Collaboration", "Review")

        if (showTopicDialog) {
            AlertDialog(
                onDismissRequest = { showTopicDialog = false },
                title = { Text("คัดกรองตามหัวข้อ") },
                text = {
                    Column {
                        listOf("ทั้งหมด" to null).plus(topics.map { it to it }).forEach { (label, topic) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedTopic = topic
                                        showTopicDialog = false
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = selectedTopic == topic, onClick = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(label)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showTopicDialog = false }) { Text("ปิด") }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                typeFilters.forEachIndexed { index, title ->
                    val isSelected = selectedTypeFilter == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTypeFilter = index },
                        label = { Text(title, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryBlue,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(50)
                    )
                }
            }
            
            IconButton(onClick = { showTopicDialog = true }) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Topic Filter",
                    tint = if (selectedTopic != null) LiveRed else PrimaryBlue
                )
            }
        }

        // Schedule List
        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {
                is ScheduleUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ScheduleUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp).align(Alignment.Center)
                    )
                }
                is ScheduleUiState.Success -> {
                    val bangkokZone = java.time.ZoneId.of("Asia/Bangkok")
                    val now = java.time.ZonedDateTime.now(bangkokZone)
                    
                    val todayStreams = mutableListOf<HolodexVideo>()
                    val laterStreams = mutableListOf<HolodexVideo>()
                    val archivedFromUpcoming = mutableListOf<HolodexVideo>()
                    
                    state.upcomingStreams.forEach { stream ->
                        val startTimeStr = stream.start_scheduled ?: stream.available_at
                        if (startTimeStr != null) {
                            try {
                                val startTime = java.time.ZonedDateTime.parse(startTimeStr).withZoneSameInstant(bangkokZone)
                                when {
                                    startTime.toLocalDate().isEqual(now.toLocalDate()) -> {
                                        todayStreams.add(stream)
                                    }
                                    startTime.isAfter(now) -> {
                                        laterStreams.add(stream)
                                    }
                                    else -> {
                                        archivedFromUpcoming.add(stream)
                                    }
                                }
                            } catch (e: Exception) {
                                laterStreams.add(stream)
                            }
                        }
                    }

                    fun filterVideos(videos: List<HolodexVideo>, filter: Int, topic: String?, query: String): List<HolodexVideo> {
                        return videos.filter { video ->
                            val matchesQuery = if (query.isBlank()) true 
                                             else (video.title.contains(query, ignoreCase = true) || 
                                                   video.channel.name.contains(query, ignoreCase = true))
                            if (!matchesQuery) return@filter false

                            val matchesTopic = if (topic == null) true 
                                             else (video.topic_id?.contains(topic, ignoreCase = true) == true || 
                                                   video.title.contains(topic, ignoreCase = true))
                            
                            if (!matchesTopic) return@filter false

                            val titleLower = video.title.lowercase()
                            val topicLower = video.topic_id?.lowercase() ?: ""
                            val isShort = titleLower.contains("#shorts") || 
                                          titleLower.contains("#reel") ||
                                          titleLower.contains("shorts") ||
                                          titleLower.contains("reel") ||
                                          titleLower.contains("tiktok") ||
                                          titleLower.contains("ショート") ||
                                          titleLower.contains("ショーツ") ||
                                          topicLower == "shorts" ||
                                          topicLower == "reel"

                            when (filter) {
                                1 -> video.type == "stream" && !isShort
                                2 -> video.type == "clip" && !isShort
                                3 -> isShort
                                else -> true
                            }
                        }
                    }

                    val filteredLive = filterVideos(state.liveStreams, selectedTypeFilter, selectedTopic, searchQuery)
                    val filteredToday = filterVideos(todayStreams, selectedTypeFilter, selectedTopic, searchQuery)
                    val filteredLater = filterVideos(laterStreams, selectedTypeFilter, selectedTopic, searchQuery)
                    val filteredPast = filterVideos(archivedFromUpcoming + state.pastStreams, selectedTypeFilter, selectedTopic, searchQuery)

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (selectedDateTab == 0) {
                            // Today Tab: Show Live + Today's upcoming
                            if (filteredLive.isNotEmpty()) {
                                item {
                                    SectionHeader(strings.liveNow.uppercase() + " (LIVE NOW)")
                                    if (filteredLive.size == 1) {
                                        val stream = filteredLive[0]
                                        ScheduleItemCard(
                                            time = "LIVE",
                                            channel = stream.channel.name,
                                            title = stream.title,
                                            isLive = true,
                                            type = stream.type,
                                            topic = stream.topic_id,
                                            videoId = stream.id,
                                            channelPhoto = stream.channel.photo,
                                            onClick = { uriHandler.openUri("https://www.youtube.com/watch?v=${stream.id}") }
                                        )
                                    } else {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                        ) {
                                            items(filteredLive.size) { index ->
                                                val stream = filteredLive[index]
                                                ScheduleItemCard(
                                                    time = "LIVE",
                                                    channel = stream.channel.name,
                                                    title = stream.title,
                                                    isLive = true,
                                                    type = stream.type,
                                                    topic = stream.topic_id,
                                                    videoId = stream.id,
                                                    channelPhoto = stream.channel.photo,
                                                    modifier = Modifier.width(360.dp),
                                                    onClick = { uriHandler.openUri("https://www.youtube.com/watch?v=${stream.id}") }
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }

                            if (filteredToday.isNotEmpty()) {
                                item {
                                    SectionHeader(strings.today.uppercase() + " (TODAY)")
                                }
                                items(filteredToday.size) { index ->
                                    val stream = filteredToday[index]
                                    val time = formatStartTime(stream.start_scheduled ?: stream.available_at)
                                    ScheduleItemCard(
                                        time = time,
                                        channel = stream.channel.name,
                                        title = stream.title,
                                        isLive = false,
                                        type = stream.type,
                                        topic = stream.topic_id,
                                        videoId = stream.id,
                                        channelPhoto = stream.channel.photo,
                                        onClick = { uriHandler.openUri("https://www.youtube.com/watch?v=${stream.id}") }
                                    )
                                }
                            }

                            if (filteredLive.isEmpty() && filteredToday.isEmpty()) {
                                item {
                                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(strings.noScheduleToday, color = TextLight)
                                    }
                                }
                            }
                        } else if (selectedDateTab == 1) {
                            // Soon Tab: Show only later streams
                            if (filteredLater.isNotEmpty()) {
                                item {
                                    SectionHeader(strings.soon.uppercase() + " (SOON)")
                                }
                                items(filteredLater.size) { index ->
                                    val stream = filteredLater[index]
                                    val startTimeStr = stream.start_scheduled ?: stream.available_at
                                    val displayTime = if (startTimeStr != null) {
                                        try {
                                            val startTime = java.time.ZonedDateTime.parse(startTimeStr).withZoneSameInstant(bangkokZone)
                                            startTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm"))
                                        } catch (e: Exception) {
                                            "Soon"
                                        }
                                    } else "Soon"
                                    
                                    ScheduleItemCard(
                                        time = displayTime,
                                        channel = stream.channel.name,
                                        title = stream.title,
                                        isLive = false,
                                        type = stream.type,
                                        topic = stream.topic_id,
                                        videoId = stream.id,
                                        channelPhoto = stream.channel.photo,
                                        onClick = { uriHandler.openUri("https://www.youtube.com/watch?v=${stream.id}") }
                                    )
                                }
                            } else {
                                item {
                                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(strings.noScheduleSoon, color = TextLight)
                                    }
                                }
                            }
                        } else {
                            // Archive Tab: Show past streams
                            if (filteredPast.isNotEmpty()) {
                                item {
                                    SectionHeader(strings.archive.uppercase() + " (RECENT ARCHIVES)")
                                }
                                items(filteredPast.size) { index ->
                                    val stream = filteredPast[index]
                                    val startTimeStr = stream.start_scheduled ?: stream.available_at
                                    val displayTime = if (startTimeStr != null) {
                                        try {
                                            val startTime = java.time.ZonedDateTime.parse(startTimeStr).withZoneSameInstant(bangkokZone)
                                            startTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm"))
                                        } catch (e: Exception) {
                                            "Past"
                                        }
                                    } else "Past"

                                    ScheduleItemCard(
                                        time = displayTime,
                                        channel = stream.channel.name,
                                        title = stream.title,
                                        isLive = false,
                                        type = stream.type,
                                        topic = stream.topic_id,
                                        videoId = stream.id,
                                        channelPhoto = stream.channel.photo,
                                        onClick = { uriHandler.openUri("https://www.youtube.com/watch?v=${stream.id}") }
                                    )
                                }
                            } else {
                                item {
                                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(strings.noScheduleArchive, color = TextLight)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        color = PrimaryBlue,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// Helper to format ISO-8601 to HH:mm
fun formatStartTime(isoTime: String?): String {
    if (isoTime == null) return "Soon"
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoTime)
        val localTime = zonedDateTime.withZoneSameInstant(java.time.ZoneId.of("Asia/Bangkok"))
        localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        "Soon"
    }
}

@Composable
fun ScheduleItemCard(
    time: String, 
    channel: String, 
    title: String, 
    isLive: Boolean, 
    type: String?, 
    topic: String?, 
    videoId: String,
    channelPhoto: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column {
            // Thumbnail Section
            Box(modifier = Modifier.height(160.dp).fillMaxWidth()) {
                val thumbUrl = "https://img.youtube.com/vi/$videoId/mqdefault.jpg"
                coil.compose.AsyncImage(
                    model = thumbUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                
                // Overlay for Time and Status
                if (!isLive) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(12.dp).align(Alignment.TopStart)
                    ) {
                        Text(
                            time, 
                            color = Color.White, 
                            fontSize = 12.sp, 
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                if (isLive) {
                    Surface(
                        color = LiveRed,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(12.dp).align(Alignment.TopEnd)
                    ) {
                        Text(
                            "LIVE", 
                            color = Color.White, 
                            fontSize = 10.sp, 
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Info Section
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    coil.compose.AsyncImage(
                        model = channelPhoto,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(channel, fontWeight = FontWeight.SemiBold, color = PrimaryBlue, fontSize = 13.sp)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(title, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 15.sp, maxLines = 2, lineHeight = 20.sp)
                
                Spacer(modifier = Modifier.height(12.dp))

                // Badges
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val titleLower = title.lowercase()
                    val topicLower = topic?.lowercase() ?: ""
                    val isShort = titleLower.contains("#shorts") || 
                                  titleLower.contains("#reel") ||
                                  topicLower == "shorts" ||
                                  topicLower == "reel"

                    val typeLabel = when {
                        isShort -> "REEL"
                        type == "clip" -> "CLIP"
                        else -> "STREAM"
                    }
                    val typeColor = when (typeLabel) {
                        "REEL" -> Color(0xFFE91E63)
                        "CLIP" -> Color(0xFF4CAF50)
                        else -> PrimaryBlue
                    }
                    
                    Badge(text = typeLabel, color = typeColor)
                    
                    if (topic != null && topicLower != "shorts" && topicLower != "reel") {
                        Badge(text = topic.replace("_", " ").uppercase(), color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun Badge(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text,
            color = color,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
