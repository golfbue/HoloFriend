package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MerchStoreScreen() {
    val strings = LocalStrings.current
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
                Text(strings.merch, fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 20.sp)
            }
            Icon(Icons.Default.Notifications, contentDescription = "Bell", tint = PrimaryBlue)
        }

        Text(strings.merch, fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))

        // List
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item { MerchItemCard("Aqua Birthday Merch", "1 มี.ค. 2022", "15 มิ.ย. 2022") }
            item { MerchItemCard("Hololive Summer Goods", "25 มิ.ย. 2022", "สินค้าพร้อมส่ง") }
        }
    }
}

@Composable
fun MerchItemCard(title: String, openDate: String, endDate: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.height(120.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color.LightGray))
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text("เปิดรับ: $openDate", fontSize = 14.sp, color = TextLight)
            Text("ถึง: $endDate", fontSize = 14.sp, color = TextLight)
        }
    }
}
