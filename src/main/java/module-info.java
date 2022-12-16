open module cs307_project {
    exports com.littleetx.cs307_project_2;
    exports com.littleetx.cs307_project_2.client;
    exports com.littleetx.cs307_project_2.database;
    exports com.littleetx.cs307_project_2.database.database_type;
    exports com.littleetx.cs307_project_2.database.user;
    exports com.littleetx.cs307_project_2.file_reader;
    exports com.littleetx.cs307_project_2.server;
    exports com.littleetx.cs307_project_2.client.tables;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires cs307.project2.interfaces;
    requires java.sql;
    requires java.rmi;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires javafx.fxml;
}