package com.xg7plugins.booter

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import java.io.BufferedWriter
import java.io.OutputStreamWriter

class LogController {

    @FXML
    private lateinit var logs: TextFlow;
    @FXML
    private lateinit var comandos: TextArea;

    private lateinit var processo: Process;


    fun initialize() {

        comandos.setOnKeyPressed { event ->
            if (event.code == KeyCode.ENTER) {
                val command = comandos.text.trim()
                if (command.isNotEmpty()) {
                    if (command == "stop") {
                        stop(false)
                    } else {
                        sendCommand(command)
                    }
                    comandos.clear()
                }
                event.consume()
            }
        }
    }

    fun isRunning(): Boolean {
        if (!this::processo.isInitialized) {
            return false
        }
        return processo.isAlive
    }

    fun setProcess(process: Process) {
        this.processo = process;
    }


    fun log(text: String) {
        logs.children.add(Text(text));
    }

    fun stop(isReloading: Boolean) {
        sendCommand("stop")

        if (!isReloading) {
            MainApplication.getInstance().mainController.parar()
            return
        }
    }

    fun clearLog() {
        logs.children.clear()
    }

    fun sendCommand(command: String) {
        processo.let {
            val writer = BufferedWriter(OutputStreamWriter(it.outputStream))
            writer.write(command)
            writer.newLine()
            writer.flush()
            log("> $command\n")
        }
    }

}

