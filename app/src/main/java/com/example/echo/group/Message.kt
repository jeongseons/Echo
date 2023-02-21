package com.example.echo.group

data class Message(var content: String, var sender: String, var proId:String, var date: String,
                   var x: Double, var y: Double, var ui: String) {
}

data class Msg(var msg: String, var sender: String, var Lat: String, var Lng: String, var proId: String)

data class ReceiveMsg(var sessionId: String, var type: String, var message: String) {
}