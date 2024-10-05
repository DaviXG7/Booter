package com.xg7plugins.booter

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication : Application() {


    val logController: LogController = LogController();
    val mainController: MainController = MainController();

    lateinit var logWindow: Stage;

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("index.fxml"))
        fxmlLoader.setController(mainController);
        val scene = Scene(fxmlLoader.load())
        stage.setOnCloseRequest {
            logController.stop(false)
            Platform.exit()
            System.exit(0)
        }
        stage.title = "Servers"
        stage.scene = scene
        stage.show()
        mainApplication = this;

        stage.setOnCloseRequest {
            logController.stop(false)
        }

        val fxmlLogLoader = FXMLLoader(MainApplication::class.java.getResource("log.fxml"))
        fxmlLogLoader.setController(logController)
        val logStage = Stage()
        logStage.setOnCloseRequest {
            logController.stop(false)
            mainController.parar()
        }
        logStage.title = "Log Window"
        logStage.scene = Scene(fxmlLogLoader.load())
        logWindow = logStage;
    }

    fun showLogWindow() {
        try {
            Platform.runLater {
                logWindow.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun hideLogWindow() {
        try {
            Platform.runLater {
                logWindow.hide()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {
        private var mainApplication: MainApplication? = null

        fun getInstance(): MainApplication {
            if (mainApplication == null) {
                throw IllegalStateException("Application not started yet!")
            }
            return mainApplication!!
        }
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}
