/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fontys.cims;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author juleb
 */
public final class CrisisFXMLController implements Initializable {

    @FXML
    private ListView lvCrisisen;

    @FXML
    private TextArea txtAlertUserDescription;
    @FXML
    private DatePicker dtAlertDate;
    @FXML
    private TextField txtAlertLocation;
    @FXML
    private TextField txtAlertReach;
    @FXML
    private Slider sliderCrisisPriority;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextArea txtAlertDescription;
    @FXML
    private ComboBox cbStatus;

    private final ObservableList<InitRequest.Crisis> crisisen = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitRequest.InitResponse resp = null;
        resp = Api.init();

        if (resp != null) {
            crisisen.addAll(resp.getCrisisResultsList());
        }

        lvCrisisen.setItems(crisisen);

        lvCrisisen.setCellFactory((Object x) -> new ListCell<InitRequest.Crisis>() {
            @Override
            public void updateItem(InitRequest.Crisis item, boolean empty) {
                if (empty) {
                    return;
                }
                setText(item.getId() + ": " + item.getTitle());
                super.updateItem(item, empty);
            }
        });

        lvCrisisen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<InitRequest.Crisis>() {
            @Override
            public void changed(ObservableValue<? extends InitRequest.Crisis> observable, InitRequest.Crisis oldValue, InitRequest.Crisis newValue) {
                if (newValue != null && oldValue != newValue) {
                    Globals.selectedCrisis = newValue;
                    if (Globals.selectedCrisis != null) {
                        if (Globals.chat != null) {
                            Globals.chat.disconnect();
                        }
                        Globals.chat = Api.createSocket(String.valueOf(Globals.selectedCrisis.getId()));
                    }
                }
            }
        });
    }

    @FXML
    private void changeCrisisClick() {

    }
}
