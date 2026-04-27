package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ScheduleUiState {
    object Loading : ScheduleUiState()
    data class Success(
        val liveStreams: List<HolodexVideo>,
        val upcomingStreams: List<HolodexVideo>,
        val pastStreams: List<HolodexVideo>
    ) : ScheduleUiState()
    data class Error(val message: String) : ScheduleUiState()
}

class ScheduleViewModel : ViewModel() {
    private val api = HolodexApi.create(BuildConfig.HOLODEX_API_KEY)
    
    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val uiState: StateFlow<ScheduleUiState> = _uiState

    init {
        refreshSchedule()
        // Auto-refresh every 5 minutes
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(5 * 60 * 1000)
                refreshSchedule()
            }
        }
    }

    fun refreshSchedule() {
        viewModelScope.launch {
            // Only show loading if we don't have data yet
            if (_uiState.value !is ScheduleUiState.Success) {
                _uiState.value = ScheduleUiState.Loading
            }
            try {
                // Fetch more items and ensure Hololive org
                val live = api.getLiveStreams(org = "Hololive")
                val upcoming = api.getVideos(org = "Hololive", status = "upcoming", limit = 50)
                val past = api.getVideos(org = "Hololive", status = "past", limit = 50)
                
                // Final client-side safeguard to ensure only Hololive content (if API returns mixed)
                // Note: We'll assume the API's 'org' filter is mostly correct, 
                // but we can add more logic here if needed.
                _uiState.value = ScheduleUiState.Success(live, upcoming, past)
            } catch (e: Exception) {
                // If we already have data, don't overwrite with error, just log it
                if (_uiState.value !is ScheduleUiState.Success) {
                    _uiState.value = ScheduleUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
}
