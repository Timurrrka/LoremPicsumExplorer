package ru.musintimur.lorempicsumexplorer.app

import ru.musintimur.lorempicsumexplorer.model.PhotoRepository
import ru.musintimur.lorempicsumexplorer.room.RoomRepository
import ru.musintimur.lorempicsumexplorer.network.RetrofitClient

object Injection {

    fun provideRoomRepository(): PhotoRepository = RoomRepository()
    fun provideNetworkResource(): PhotoRepository = RetrofitClient()

}