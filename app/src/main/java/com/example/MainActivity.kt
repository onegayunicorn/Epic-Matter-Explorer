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
    Text("EPIC MATTER · Catalog", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
    val programs = listOf(
        "AcoustoBioTweeze: Contactless cell translation for synthetic tissue.",
        "ParticleConjure-Env: AI-driven assembly of environmental particles.",
        "MythicInvoke-Assembler: Combined human/AI/mythic materialization.",
        "BubbleBurstSynthetica: Amino acid synthesis from microbubble tech.",
        "Industrial Edge Sentinel: Real-time digital twin mapping.",
        "Bio-Entanglement Engine: Coupled state vector synchronization."
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(programs) { program ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
                Text(text = program, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun SimulationContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Sovereign Engine: Digital Twin", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Text("Status: Syncing Bio-Entanglement Engine...", color = Color.LightGray)
        
        // Status Indicators
        Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(8.dp)) {
                Text("Coherence: 0.99", modifier = Modifier.padding(8.dp), color = Color.Green, fontSize = 12.sp)
            }
            Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(8.dp)) {
                Text("Entanglement: Active", modifier = Modifier.padding(8.dp), color = Color.Cyan, fontSize = 12.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        var frequency by remember { mutableStateOf(2.2f) }
        Text("Frequency: ${"%.2f".format(frequency)} MHz", color = Color.White)
        Slider(value = frequency, onValueChange = { frequency = it }, valueRange = 1f..5f)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Simulation logic */ }, modifier = Modifier.fillMaxWidth()) { Text("Run Digital Twin Simulation") }
    }
}

@Composable
fun InvocationContent() {
    var prompt by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Mythic Invoke-Assembler", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Sovereign Engine: READY", modifier = Modifier.padding(8.dp), color = Color.Green, fontSize = 12.sp)
        }
        Text("Enter poetic descriptions for matter transition:", color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Invoke...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Invocation logic */ }, modifier = Modifier.fillMaxWidth()) { Text("Materialize") }
    }
}
