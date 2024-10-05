package com.xg7plugins.booter

import javafx.application.Application
import javafx.fxml.FXML
import javafx.scene.control.TextArea

class LogControler(val app: MainApplication) {

    @FXML
    lateinit var logs: TextArea;
    private lateinit var commands: TextArea;


    fun initialize() {
        logs.isEditable = false
    }


    fun log(text: String) {
        logs.appendText(text);
    }

}

