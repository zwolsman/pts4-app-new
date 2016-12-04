/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fontys.cims;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import edu.fontys.cims.InitRequest.Alert;
import edu.fontys.cims.InitRequest.Crisis;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author juleb
 */
public final class CrisisTabController implements Initializable {

    @FXML
    private ListView lvCrisisen;
    @FXML
    private TextArea alertDescription;
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
    private ComboBox cbStatus;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextArea chatBoxArea;

    @FXML
    private TextArea txtCrisisDescription;

    private Crisis selectedCrisis;

    private final ObservableList<InitRequest.Crisis> crisisen = FXCollections.observableArrayList();

    private ObservableList<String> cbOptions
            = FXCollections.observableArrayList(
                    "In gang",
                    "Afgehandeld"
            );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitRequest.InitResponse resp = null;
        resp = Api.init();

        if (resp != null) {
            crisisen.addAll(resp.getCrisisResultsList());
        }

        lvCrisisen.setItems(crisisen);
        cbStatus.setItems(cbOptions);
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
                    selectedCrisis = newValue;
                    Alert alert = selectedCrisis.getAlert();
                    txtTitle.setText(selectedCrisis.getTitle());
                    alertDescription.setText(alert.getDescription());
                    txtAlertReach.setText(String.valueOf(selectedCrisis.getReach()));
                    txtCrisisDescription.setText(selectedCrisis.getDescription());
                    sliderCrisisPriority.setValue(selectedCrisis.getPriority());
                    txtCrisisDescription.setWrapText(true);
                    alertDescription.setWrapText(true);
                    cbStatus.getSelectionModel().selectFirst();
                    
                    if (alert.getLocation().getStreetName().isEmpty()) {
                        txtAlertLocation.setText(alert.getLocation().getCity());
                    } else {
                        txtAlertLocation.setText(alert.getLocation().getStreetName() + " " + alert.getLocation().getStreetNumber());
                    }

                    LatLong pos = new LatLong(alert.getLocation().getLatitude(), alert.getLocation().getLongitude());
                    MainController.setMapPosition(pos);
                
                    try {
                        dtAlertDate.setValue(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(selectedCrisis.getAlert().getTimestamp()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    } catch (ParseException ex) {
                        Logger.getLogger(CrisisTabController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Socket chat = Api.createSocket(String.valueOf(newValue.getId()));
                    chat.on("a message", (Object... os) -> {
                        try {
                            final InitRequest.Message message = InitRequest.Message.parseFrom((byte[]) os[0]);
                            System.out.println(message.getText());
                            chatTextArea.appendText(message.getId() + ": " + message.getText() + "\r\n");
                        } catch (InvalidProtocolBufferException ex) {
                        }
                    });
                    chat.on(Socket.EVENT_CONNECT, (Object... os) -> {
                        System.out.println("I connected!");
                    });
                    chat.connect();
                }
            }
        });
    }

    @FXML
    private void sendMessageButton() {
        try {
            URL url = new URL("http://localhost:3000/chat");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/protobuf");
            InitRequest.Message message = InitRequest.Message.newBuilder()
                    .setCrisisid(selectedCrisis.getId())
                    .setId(1)
                    .setText(chatBoxArea.getText()).build();
            System.out.println(message);
            message.writeTo(conn.getOutputStream());

            chatTextArea.appendText("Me: " + chatBoxArea.getText() + "\r\n");
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void changeCrisisClick() {

    }
}