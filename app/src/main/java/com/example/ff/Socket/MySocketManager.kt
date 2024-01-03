package com.example.ff.Socket

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter


class MySocketManager {
    private var socket: Socket? = null

    // Khởi tạo kết nối Socket.IO
    fun initSocket() {
        try {
            val options = IO.Options()
            options.reconnection = true

            // Thay đổi địa chỉ server của bạn tại đây
            socket = IO.socket("http://192.168.1.175:3200/", options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Kết nối Socket.IO
    fun connectSocket() {
        socket?.connect()
    }

    // Ngắt kết nối Socket.IO
    fun disconnectSocket() {
        socket?.disconnect()
    }

    // Gửi sự kiện đến server
    fun emitEvent(eventName: String, data: Any?) {
        socket?.emit(eventName, data)
    }

    // Lắng nghe sự kiện từ server
    fun onEvent(eventName: String, listener: Emitter.Listener) {
        socket?.on(eventName, listener)
    }

    // Đăng ký người dùng khi kết nối
    fun addNewUser(userId: String) {
        emitEvent("addNewUser", userId)
    }

    // Gửi tin nhắn
    fun sendMessage(message: String) {
        emitEvent("sendMessage", message)
    }

    // Đăng ký lắng nghe sự kiện khi nhận tin nhắn
    fun onReceiveMessage(listener: Emitter.Listener) {
        onEvent("receivedMessage", listener)
    }

    // Xử lý sự kiện khi người dùng ngắt kết nối
    fun onDisconnect(listener: Emitter.Listener) {
        onEvent("disconnect", listener)
    }
}
