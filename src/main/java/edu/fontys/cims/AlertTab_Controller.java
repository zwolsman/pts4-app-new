package edu.fontys.cims;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.protobuf.GeneratedMessageV3;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Jip
 */
public class AlertTab_Controller implements Initializable {
    @FXML
    private ListView lvAlerts;

    @FXML
    private DatePicker dtAlertDate;

    @FXML
    private TextArea txtAlertUserDescription;

    @FXML
    private TextField txtAlertLocation;

    @FXML
    private TextField txtCrisisTitle;

    @FXML
    private TextArea txtCrisisDescription;

    @FXML
    private Slider sliderCrisisPriority;
    
    @FXML
    private TextField txtCrisisReach;
    

    private InitRequest.Alert selectedAlert;
    private final ObservableList<InitRequest.Alert> alerts = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //mapView.addMapInializedListener(this);

        InitRequest.InitResponse resp = null;
        resp = Api.init();
        
        if(resp != null){
            alerts.addAll(resp.getAlertResultsList());
        }
        initializeListView();
    } 
    
    private void initializeListView() {
        lvAlerts.setItems(alerts);

        lvAlerts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<InitRequest.Alert>() {
            @Override
            public void changed(ObservableValue<? extends InitRequest.Alert> observable, InitRequest.Alert oldValue, InitRequest.Alert newValue) {
                if(newValue != oldValue){
                    selectedAlert = newValue;
                }
                
                try {
                    dtAlertDate.setValue(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newValue.getTimestamp()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (ParseException ex) {
                    Logger.getLogger(SceneFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }

                MarkerOptions markerOptions = new MarkerOptions();
                System.out.println(newValue.getLocation().getLatitude());
                System.out.println(newValue.getLocation().getLongitude());

                LatLong pos = new LatLong(newValue.getLocation().getLatitude(), newValue.getLocation().getLongitude());
                markerOptions.position(pos);

                if (SceneFXMLController.marker != null) {
                    SceneFXMLController.map.removeMarker(SceneFXMLController.marker);

                }
                SceneFXMLController.marker = new Marker(markerOptions);
                SceneFXMLController.map.addMarker(SceneFXMLController.marker);
                SceneFXMLController.map.panTo(pos);
                txtAlertUserDescription.setText(newValue.getDescription());

            }
        });

        lvAlerts.setCellFactory((Object x) -> new ListCell<InitRequest.Alert>() {
            @Override
            public void updateItem(InitRequest.Alert item, boolean empty) {
                if (empty) {
                    return;
                }
                setText(item.getTimestamp());
                super.updateItem(item, empty);

            }
        });

    }

    @FXML
    public void handleProcessClick() {
        if (lvAlerts.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }

        if (txtCrisisTitle.getText().equalsIgnoreCase("")) {
            prompt("Please fill in the title.");
            return;
        }
        if (txtCrisisDescription.getText().equalsIgnoreCase("")) {
            prompt("Please fill in the description.");
            return;
        }
        if (txtCrisisReach.getText().equalsIgnoreCase("")) {
            prompt("Please fill in the reach.");
            return;
        }
        boolean isNumeric = true;
        for (char c : txtCrisisReach.getText().toCharArray()) {
            if (!Character.isDigit(c)) {
                prompt("Please fill in a valid reach.");
                isNumeric = false;
                break;
            }
        }
        if (!isNumeric) {
            return;
        }

        InitRequest.Crisis crisis = InitRequest.Crisis.newBuilder()
                .setAlert(selectedAlert)
                .setLocation(selectedAlert.getLocation())
                .setStatus("henk")
                .setPriority((int) Math.round(sliderCrisisPriority.getValue()))
                .setTitle(txtCrisisTitle.getText())
                .setDescription(txtCrisisDescription.getText())
                .setThumbnail("henkiest thumbnail")
                .setReach(Integer.parseInt(txtCrisisReach.getText()))
                .setTimestamp(dtAlertDate.getValue().format(DateTimeFormatter.ISO_DATE))
                .build();
        sendProto(crisis);
        System.out.println("Handle process");
    }

    private void prompt(String desc) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Oops, something isn't right");
        alert.setContentText(desc);
        alert.showAndWait();
    }

    private void sendProto(GeneratedMessageV3 proto) {
        try {
            URL url = new URL("http://localhost:3001/" + "crisis");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/protobuf");
            proto.writeTo(conn.getOutputStream());

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SceneFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SceneFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleDenyClick() {
        if (lvAlerts.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Ignore alert");
        alert.setContentText("Are you sure you want to ignore this alert?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            alerts.remove(lvAlerts.getSelectionModel().getSelectedIndex());
        }
    }

    
}
