package com.example.echo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.echo.group.GroupActivity
import com.example.echo.group.Message
import com.example.echo.group.Msg
import com.example.echo.group.ReceiveMsg
import com.example.echo.group.detail.TalkAdapter
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


open class WebSocketListener : WebSocketListener() {

    val gson = Gson()
    var receiveMsg = ReceiveMsg("0","0", "0")
    var msg = Msg("0","0","0","0")

    @RequiresApi(Build.VERSION_CODES.O)
    val toDate = LocalDateTime.now()
    lateinit var now: String
    lateinit var mySessionId:String
    lateinit var adapter: TalkAdapter

    // 메시지 보냄
    override fun onOpen(webSocket: WebSocket, response: Response?) {
        webSocket.send("{\"roomNumber\":\"88\", \"type\":\"ticker\", \"symbols\": [\"BTC_KRW\"], \"tickTypes\": [\"30M\"]}")
//        webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
        mySessionId = "0"
        connect = true
    }

    // 메시지 받음
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessage(webSocket: WebSocket?, text: String) {
        Log.d("Socket","Receiving : $text")
//        getMsgList(text)
        receiveMsg = gson.fromJson(text, ReceiveMsg::class.java)
        Log.d("Socket","세션,메시지 확인 : $receiveMsg")

        if(receiveMsg.message!=null) {
            Log.d("세션 아이디 확인1","$mySessionId")
            if(mySessionId=="0"){
                mySessionId = receiveMsg.sessionId
            }
            Log.d("세션 아이디 확인2","$mySessionId")

            msg = gson.fromJson(receiveMsg.message, Msg::class.java)
            Log.d("Socket", "메시지 확인 : $msg")

            if(msg.msg!=null){//메시지를 보낼때만
            now = toDate.format(DateTimeFormatter.ofPattern("HH:mm"))
                if(mySessionId==receiveMsg.sessionId) {
                    talkList.add(
                        Message(
                            msg.msg,
                            msg.sender,
                            now,
                            msg.Lat.toDouble(),
                            msg.Lng.toDouble(),
                            "me"
                        )
                    )
                }
                else if(mySessionId!=receiveMsg.sessionId){
                    talkList.add(
                        Message(
                            msg.msg,
                            msg.sender,
                            now,
                            msg.Lat.toDouble(),
                            msg.Lng.toDouble(),
                            "other"
                        )
                    )
                }

                Log.d("Socket", "talklist확인: $talkList")
            }

        }
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
        var talkList = ArrayList<Message>()
        var connect:Boolean = false
    }
}