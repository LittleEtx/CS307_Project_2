package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.database.database_type.CityInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import static com.littleetx.cs307_project_2.client.GlobalManager_Client.getStaffInfo;

public class NewItemController {
    @FXML
    private TextField itemName;
    @FXML
    private TextField itemPrice;
    @FXML
    private ChoiceBox<String> itemType;
    @FXML
    private ChoiceBox<String> retrievalCity;
    @FXML
    private ChoiceBox<String> deliveryCity;
    @FXML
    private ChoiceBox<String> exportCity;
    @FXML
    private ChoiceBox<String> importCity;

    @FXML
    private Node submitButton;

    private Map<String, Integer> inlandCities;
    private Map<String, Integer> portCities;

    @FXML
    private void initialize() {
        submitButton.sceneProperty().addListener(observable -> {
            if (submitButton.getScene() != null) {
                submitButton.getScene().getStylesheets().add(
                        "com/littleetx/cs307_project_2/assets/css/validation.css");
            }
        });

        try {
            var server = ClientHelper.getConnection();
            Map<Integer, CityInfo> cityMap = server.getAllCities();
            inlandCities = new HashMap<>();
            portCities = new HashMap<>();
            for (var entry : cityMap.entrySet()) {
                if (entry.getValue().isSeaport()) {
                    portCities.put(entry.getValue().name(), entry.getKey());
                } else {
                    inlandCities.put(entry.getValue().name(), entry.getKey());
                }
            }

            itemType.getItems().addAll(server.getItemTypes());

            StringConverter<String> converter = new StringConverter<>() {
                @Override
                public String toString(String object) {
                    if (object == null) {
                        return null;
                    }
                    return inlandCities.getOrDefault(object,
                            portCities.get(object)) + " - " + object;
                }

                @Override
                public String fromString(String string) {
                    String[] split = string.split(" - ");
                    return split[1];
                }
            };
            retrievalCity.setConverter(converter);
            deliveryCity.setConverter(converter);
            exportCity.setConverter(converter);
            importCity.setConverter(converter);

            retrievalCity.getItems().addAll(inlandCities.keySet());
            deliveryCity.getItems().addAll(inlandCities.keySet());
            deliveryCity.getItems().sort((s1, s2) ->
                    inlandCities.get(s1).compareTo(inlandCities.get(s2)));
            exportCity.getItems().addAll(portCities.keySet());
            exportCity.getItems().sort((s1, s2) ->
                    portCities.get(s1).compareTo(portCities.get(s2)));
            importCity.getItems().addAll(portCities.keySet());
            importCity.getItems().sort((s1, s2) ->
                    portCities.get(s1).compareTo(portCities.get(s2)));

            deliveryCity.getItems().remove(getStaffInfo().city());
            retrievalCity.getSelectionModel().select(getStaffInfo().city());
            setSelection(exportCity, importCity);
            setSelection(importCity, exportCity);

            itemPrice.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    itemPrice.setText(newValue.replaceAll("\\D", ""));
                }
            });

            ValidationSupport validationSupport = new ValidationSupport();
            validationSupport.setValidationDecorator(new StyleClassValidationDecoration());
            validationSupport.registerValidator(itemName, false,
                    Validator.createEmptyValidator("Item name is required"));
            validationSupport.registerValidator(itemPrice, false,
                    Validator.createEmptyValidator("Item price is required"));
            validationSupport.registerValidator(itemType, false,
                    Validator.createEmptyValidator("Item type is required"));
            validationSupport.registerValidator(retrievalCity, false,
                    Validator.createEmptyValidator("Retrieval city is required"));
            validationSupport.registerValidator(deliveryCity, false,
                    Validator.createEmptyValidator("Delivery city is required"));
            validationSupport.registerValidator(exportCity, false,
                    Validator.createEmptyValidator("Export city is required"));
            validationSupport.registerValidator(importCity, false,
                    Validator.createEmptyValidator("Import city is required"));

            submitButton.disableProperty().bind(validationSupport.invalidProperty());
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
    }

    private void setSelection(ChoiceBox<String> selected, ChoiceBox<String> toModify) {
        selected.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                toModify.getItems().add(oldValue);
            }
            if (newValue != null) {
                toModify.getItems().remove(newValue);
            }
            toModify.getItems().sort((s1, s2) ->
                    portCities.get(s1).compareTo(portCities.get(s2)));
        });
    }

    @FXML
    protected void submit() {
        try {
            var server = ClientHelper.getConnection();
            if (!server.newItem(
                    GlobalManager_Client.getStaffID(),
                    itemName.getText(),
                    itemType.getValue(),
                    Integer.parseInt(itemPrice.getText()),
                    inlandCities.get(deliveryCity.getValue()),
                    portCities.get(exportCity.getValue()),
                    portCities.get(importCity.getValue())
            )) {
                GlobalManager_Client.showAlert("Failed to add item");
                return;
            }
            GlobalManager_Client.closeWindow();
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
    }

    @FXML
    protected void cancel() {
        GlobalManager_Client.closeWindow();
    }
}
