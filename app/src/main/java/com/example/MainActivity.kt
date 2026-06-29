package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          containerColor = BentoBackground
        ) { innerPadding ->
          ReportScreen(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

data class Section(val title: String, val content: String)

enum class AppTab { CATALOG, SIMULATION, INVOCATION }

@Composable
fun ReportScreen(modifier: Modifier = Modifier) {
    var currentTab by remember { mutableStateOf(AppTab.CATALOG) }

  Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
    // Header
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "RESEARCH REPORT", fontSize = 10.sp, color = HeaderBlue, fontWeight = FontWeight.Bold)
            Text(text = "EPIC · MATTER", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }
        Surface(color = MetricsBlue, shape = RoundedCornerShape(20.dp)) {
            Text(text = "v1.0 · 2026", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp, color = MetricDark, fontWeight = FontWeight.Bold)
        }
    }
    
    // Tabs (simple buttons)
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppTab.values().forEach { tab ->
            Button(
                onClick = { currentTab = tab },
                colors = ButtonDefaults.buttonColors(containerColor = if (currentTab == tab) HeaderBlue else Color.LightGray),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = tab.name, fontSize = 10.sp)
            }
        }
    }

    // Main Content
    Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
        when (currentTab) {
            AppTab.CATALOG -> CatalogContent()
            AppTab.SIMULATION -> SimulationContent()
            AppTab.INVOCATION -> InvocationContent()
        }
    }
  }
}

@Composable
fun CatalogContent() {
    val programs = listOf("AcoustoBioTweeze", "EMFlux-Positioner", "Liquid-Print-Matter")
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(programs) { program ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
                Text(text = program, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun SimulationContent() {
    var frequency by remember { mutableStateOf(2.2f) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Frequency: ${frequency} MHz", color = Color.White)
        Slider(value = frequency, onValueChange = { frequency = it }, valueRange = 1f..5f)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Simulation logic */ }) { Text("Run Simulation") }
    }
}

@Composable
fun InvocationContent() {
    var prompt by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Enter mythic/poetic invocation") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Invocation logic */ }) { Text("Materialize") }
    }
}
