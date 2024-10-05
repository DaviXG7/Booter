package com.xg7plugins.booter

import com.xg7plugins.booter.server.Servidor
import com.xg7plugins.booter.server.Status
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import java.io.BufferedReader
import java.io.InputStreamReader

class MainControler(val app: MainApplication) {

    private lateinit var servidor: Servidor;

    @FXML
    private lateinit var aviso: Label;
    @FXML
    private lateinit var status: Label;
    @FXML
    private lateinit var version: ComboBox<String>;
    @FXML
    private lateinit var iniciar: Button;
    @FXML
    private lateinit var reiniciar: Button;
    @FXML
    private lateinit var ir: Button;



    fun onIniciar() {


        if (version.value == null) {
            aviso.text = "Selecione uma versão!"
            return
        }
        iniciar(version.value)
    }

    fun iniciar(version: String) {
        this.servidor = Servidor(version, Status.Inicializando)
        aviso.text = "Iniciando servidor..."
        Thread {
            val processBuilder = ProcessBuilder("java", "-jar", servidor.getPath())
            processBuilder.redirectErrorStream(true)
            processBuilder.directory(java.io.File(servidor.getPath().removeSuffix("server.jar")))
            val processo = processBuilder.start()



            val leitor = BufferedReader(InputStreamReader(processo.inputStream))
            var linha: String?
            while (leitor.readLine().also { linha = it } != null) {
                val logLinha = linha
                Platform.runLater {
                    app.logControler.log("$logLinha\n")
                }
            }

            processo.waitFor()
        }.start()
    }
}