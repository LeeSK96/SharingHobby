package com.example.sharinghobby

import java.io.Serializable
import java.sql.Timestamp
data class Memo1(var url:String ,var title:String , var idx:String, var timestamp:Long, var star: Boolean = false): Serializable
