package com.example.echo

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

open class WebSocketListener : WebSocketListener() {


    // 메시지 보냄
    override fun onOpen(webSocket: WebSocket, response: Response?) {
        webSocket.send("{\"roomNumber\":\"88\", \"type\":\"ticker\", \"symbols\": [\"BTC_KRW\"], \"tickTypes\": [\"30M\"]}")
//        webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
        connect = true
    }

    // 메시지 받음
    override fun onMessage(webSocket: WebSocket?, text: String) {
        Log.d("Socket","Receiving : $text")

    }

    // 메시지 받음
    override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
        Log.d("Socket", "Receiving bytes : $bytes")
    }

    // 소켓 닫음
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Socket","Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }
    // 소켓 닫음
     fun onClose(webSocket: WebSocket) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }

    // 실패
    override fun onFailure(webSocket: WebSocket?, t: Throwable, response: Response?) {
        Log.d("Socket","Error : " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
        var connect:Boolean = false
    }
}