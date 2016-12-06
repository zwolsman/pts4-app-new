package edu.fontys.cims;

import com.google.protobuf.GeneratedMessageV3;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import edu.fontys.cims.Api;
import edu.fontys.cims.CreateChatHandler;
import edu.fontys.cims.InitRequest;
import edu.fontys.cims.InitRequest.Alert;
import edu.fontys.cims.InitRequest.Crisis;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public final class CrisisTabController implements Initializable, MapComponentInitializedListener {
    
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
    @FXML
    GoogleMapView mapView;
    GoogleMap map;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapView.addMapInializedListener(this);
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
                    // MainController.setMapPosition(pos); TODO fix dit

                    try {
                        dtAlertDate.setValue(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(selectedCrisis.getAlert().getTimestamp()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    } catch (ParseException ex) {
                        Logger.getLogger(CrisisTabController.class.getName()).log(Level.SEVERE, "Error parsing datetime");
                    }
                }
            }
        });
    }
    
    private CreateChatHandler handler;
    
    public void setCreateChatHandler(CreateChatHandler handler) {
        this.handler = handler;
    }
    
    @FXML
    private void btnChatClick() {
        if (handler == null) {
            return;
        }
        Crisis crisis = (Crisis) lvCrisisen.getSelectionModel().getSelectedItems().get(0);
        if (crisis == null) {
            return;
        }
        handler.createChat(crisis.getId());
    }
    
    @FXML
    private void changeCrisisClick() {
        int index = lvCrisisen.getSelectionModel().getSelectedIndex();
        
        if (index == -1) {
            return;
        }
        
        String status = cbStatus.getSelectionModel().getSelectedItem().toString();
        Crisis selected = crisisen.get(index);
        InitRequest.Crisis crisis = InitRequest.Crisis.newBuilder()
                .setStatus(status)
                .setPriority((int) Math.round(sliderCrisisPriority.getValue()))
                .setTitle(txtTitle.getText())
                .setDescription(txtCrisisDescription.getText())
                .setThumbnail("creck thumbnail")
                .setReach(Integer.valueOf(txtAlertReach.getText()))
                .setId(selected.getId())
                .build();
        
        sendProto(crisis);
        
        if (cbStatus.getSelectionModel().getSelectedIndex() == 1) {
            crisisen.remove(selected);
        }
    }
    
    private void sendProto(GeneratedMessageV3 proto) {
        try {
            URL url = new URL(Api.SOCKET_ENDPOINT + "/api/changecrisis");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/protobuf");
            proto.writeTo(conn.getOutputStream());
            
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();
        
        mapOptions.center(new LatLong(51.436596, 5.478001))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .mapTypeControl(false)
                .zoomControl(false)
                .zoom(12);
        
        map = mapView.createMap(mapOptions);
    }
}
