package com.xg7plugins.booter.server

import javafx.application.Platform
import java.io.BufferedReader
import java.io.InputStreamReader

data class Servidor(val versao: String, var status: Status) {

    fun getPath(): String {
        return "D:\\Spigot\\Spigot $versao\\server.jar"
    }


}
