package com.example.sloppyweather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column // Only Column, no Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier // Modifier imported but not used properly
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sloppyweather.data.InfoPacket

class MainActivity : ComponentActivity() {

    private val processor: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // Using default theme
                Surface {
                    WeatherScreen(processor)
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(processor: WeatherViewModel) {
    // Collect state from ViewModel
    val info by processor.currentInfo.collectAsState()

    // Trigger data processing on composition
    LaunchedEffect(Unit) {
        processor.fetchAndProcessWeatherData()
    }

    // Improved UI Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Weather Info", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (info != null) {
            // Display values with improved layout and null safety
            Text(
                text = info?.temperature?.let { "Temperature: %.1f°C".format(it) } ?: "Temperature: N/A",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Condition: ${info?.weatherCondition ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = info?.humidity?.let { "Humidity: $it%" } ?: "Humidity: N/A",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            // Show loading indicator
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Basic Preview
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        // Dummy data for preview
        val dummyProcessor = WeatherViewModel()
        // Preview might show loading state or require manual state setting for info
        WeatherScreen(dummyProcessor)
    }
} 