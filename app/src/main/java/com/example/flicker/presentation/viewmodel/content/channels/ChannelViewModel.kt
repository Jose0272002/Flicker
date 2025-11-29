package com.example.flicker.presentation.viewmodel.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flicker.domain.model.Channel
import com.example.flicker.domain.usecase.channels.GetChannelsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ChannelsViewModel(
    private val getChannelsUseCase: GetChannelsUseCase
) : ViewModel() {

    val channels: StateFlow<List<Channel>> = getChannelsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
