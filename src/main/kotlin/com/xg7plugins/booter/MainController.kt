package com.xg7plugins.booter

import com.xg7plugins.booter.server.Servidor
import com.xg7plugins.booter.server.Status
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import java.awt.Desktop
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MainController {

    private lateinit var servidor: Servidor;

    @FXML
    private lateinit var status: Label;
    @FXML
    private lateinit var version: ComboBox<String>;
    @FXML
    private lateinit var iniciar: Button;
    @FXML
    private lateinit var reiniciar: Button;
    @FXML
    private lateinit var minecraft: Button;
    @FXML
    private lateinit var parar: Button;
    @FXML
    private lateinit var ir: Button;
    @FXML
    private lateinit var reload: Button;

    fun initialize() {
        reiniciar.isDisable = true
        ir.isDisable = true
        parar.isDisable = true
        reload.isDisable = true
        minecraft.isDisable = true

        version.valueProperty().addListener { _, _, _ ->
            onVersionChange()
        }
    }

    fun iniciarMinecraft() {
        Thread {

            val processBuilder = ProcessBuilder("D:\\Minecraft Versions\\scripts\\${servidor.versao}.bat")
            processBuilder.redirectErrorStream(true)

            val processo = processBuilder.start()

            val leitor = BufferedReader(InputStreamReader(processo.inputStream))
            var linha: String?
            while (leitor.readLine().also { linha = it } != null) {
                println(linha)
            }
            processo.waitFor()

        }.start()
    }


    fun onIniciar() {
        if (version.value == null) {
            status.text = "Selecione uma versão!"
            return
        }
        iniciar.isDisable = true
        reiniciar.isDisable = false
        parar.isDisable = false
        ir.isDisable = false
        reload.isDisable = false
        iniciar()
    }

    fun onReiniciar() {
        MainApplication.getInstance().logController.stop(true)
        servidor.status = Status.Reiniciando
        atualizarStatus()
        reiniciar.isDisable = true
        parar.isDisable = true
        reload.isDisable = true
        Thread {
            while (MainApplication.getInstance().logController.isRunning()) {
                Thread.sleep(100)
            }
            Thread.sleep(5000)
            iniciar()
            reiniciar.isDisable = false
            parar.isDisable = false
            reload.isDisable = false
        }.start()
    }

    fun onVersionChange() {
        ir.isDisable = false
        minecraft.isDisable = false;
        this.servidor = Servidor(version.value, Status.Parado)
        atualizarStatus()
    }

    fun onParar() {
        MainApplication.getInstance().logController.stop(false)
    }
    fun parar() {
        servidor.status = Status.Parado
        atualizarStatus()
        iniciar.isDisable = false
        reiniciar.isDisable = true
        parar.isDisable = true
        reload.isDisable = true
        MainApplication.getInstance().hideLogWindow()
        MainApplication.getInstance().logController.clearLog()
    }

    fun abrirExplorer() {
        try {
            val folder = File(servidor.getPath().removeSuffix("server.jar"))
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(folder)
            } else {
                println("Desktop não suportado.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onCommandReload() {
        MainApplication.getInstance().logController.sendCommand("reload confirm")
    }

    fun atualizarStatus() {
        status.text = "Status: " + servidor.status.toString()
    }

    fun iniciar() {
        servidor.status = Status.Inicializando
        Platform.runLater{atualizarStatus()}
        Thread {

            while (MainApplication.getInstance().logController.isRunning()) {
                Thread.sleep(100)
            }

            val versionServer = servidor.versao.split(".")[1].toInt()
            val javaV = if (versionServer <= 16) "\"D:\\jdks\\8\\bin\\java.exe\"" else if (versionServer <= 20) "\"D:\\jdks\\bin\\java.exe\"" else "\"D:\\jdks\\21\\bin\\java.exe\""
            val processBuilder = ProcessBuilder(javaV, "-jar", servidor.getPath(), "nogui")
            processBuilder.redirectErrorStream(true)
            processBuilder.directory(File(servidor.getPath().removeSuffix("server.jar")))
            val processo = processBuilder.start()

            MainApplication.getInstance().logController.setProcess(processo)

            MainApplication.getInstance().showLogWindow().also {
                val leitor = BufferedReader(InputStreamReader(processo.inputStream))
                var linha: String?
                while (leitor.readLine().also { linha = it } != null) {
                    val logLinha = linha
                    Platform.runLater {
                        if (logLinha != null) {
                            if (logLinha.contains("Done (")) {
                                servidor.status = Status.Rodando
                                atualizarStatus()
                            }
                        }

                        MainApplication.getInstance().logController.log("$logLinha\n")
                    }
                }
            }



            processo.waitFor()
        }.start()
    }
}