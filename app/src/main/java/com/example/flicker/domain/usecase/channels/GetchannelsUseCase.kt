package com.example.flicker.domain.usecase.channels

import com.example.flicker.domain.model.Channel
import com.example.flicker.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow

class GetChannelsUseCase(
    private val channelRepository: ChannelRepository
) {
    operator fun invoke(): Flow<List<Channel>> {
        return channelRepository.list()
    }
}
