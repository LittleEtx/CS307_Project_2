open module cs307_project {
    exports com.littleetx.cs307_project_2;
    exports com.littleetx.cs307_project_2.client;
    exports com.littleetx.cs307_project_2.database;
    exports com.littleetx.cs307_project_2.database.database_type;
    exports com.littleetx.cs307_project_2.database.user;
    exports com.littleetx.cs307_project_2.file_reader;
    exports com.littleetx.cs307_project_2.server;
    exports com.littleetx.cs307_project_2.client.tables;
    exports main.interfaces;
    exports main;
    exports com.littleetx.cs307_project_2.client.controllers;
    exports com.littleetx.cs307_project_2.client.dialogs;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;
    requires java.rmi;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires javafx.fxml;
    requires org.controlsfx.controls;
}