package edu.fontys.cims;

import com.google.protobuf.GeneratedMessageV3;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import edu.fontys.cims.InitRequest.Alert;
import edu.fontys.cims.InitRequest.Crisis;
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
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class FXMLController implements Initializable, MapComponentInitializedListener {

    @FXML
    private ListView lvAlerts;

    @FXML
    private DatePicker dtAlertDate;

    @FXML
    private TextArea txtAlertUserDescription;

    @FXML
    private TextField txtAlertLocation;

    @FXML
    private TextField txtAlertTitle;

    @FXML
    private TextArea txtAlertDescription;

    @FXML
    private TextField txtAlertReach;

    @FXML
    private GoogleMapView mapView;
    private GoogleMap map;
    private Marker marker;

    private final ObservableList<Alert> alerts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);

        InitRequest.InitResponse resp = null;
        
        resp = Api.init();
        
        if(resp != null){
        alerts.addAll(resp.getAlertResultsList());
        }
        initializeListView();

        /*try {
            final Socket socket = IO.socket("http://localhost:3001");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("Connected to web socket!");
                }

            }).on("melding", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("Disconnected!");
                }

            });
            socket.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    private void initializeListView() {
        lvAlerts.setItems(alerts);

        lvAlerts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Alert>() {
            @Override
            public void changed(ObservableValue<? extends Alert> observable, Alert oldValue, Alert newValue) {
                try {
                    dtAlertDate.setValue(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newValue.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (ParseException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }

                MarkerOptions markerOptions = new MarkerOptions();
                System.out.println(newValue.getLocation().getLat());
                System.out.println(newValue.getLocation().getLong());

                LatLong pos = new LatLong(newValue.getLocation().getLat(), newValue.getLocation().getLong());
                markerOptions.position(pos);

                if (marker != null) {
                    map.removeMarker(marker);

                }
                marker = new Marker(markerOptions);
                map.addMarker(marker);
                map.panTo(pos);
                txtAlertUserDescription.setText(newValue.getUserDescription());
            }
        });

        lvAlerts.setCellFactory((Object x) -> new ListCell<Alert>() {
            @Override
            public void updateItem(Alert item, boolean empty) {
                if (empty) {
                    return;
                }
                setText(item.getDate());
                super.updateItem(item, empty);

            }
        });

    }

    @FXML
    public void handleProcessClick() {
        if (lvAlerts.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }

        if (txtAlertTitle.getText().equalsIgnoreCase("")) {
            prompt("Please fill in the title.");
            return;
        }
        if (txtAlertDescription.getText().equalsIgnoreCase("")) {
            prompt("Please fill in the description.");
            return;
        }
        if (txtAlertReach.getText().equalsIgnoreCase("")) {
            prompt("Please fill in the reach.");
            return;
        }
        boolean isNumeric = true;
        for (char c : txtAlertReach.getText().toCharArray()) {
            if (!Character.isDigit(c)) {
                prompt("Please fill in a valid reach.");
                isNumeric = false;
                break;
            }
        }
        if (!isNumeric) {
            return;
        }

        Crisis crisis = Crisis.newBuilder()
                .setTitle(txtAlertTitle.getText())
                .setDescription(txtAlertDescription.getText())
                .setReach(Integer.parseInt(txtAlertReach.getText()))
                .setDate(dtAlertDate.getValue().format(DateTimeFormatter.ISO_DATE))
                .build();
        sendProto(crisis);
        System.out.println("Handle process");
    }

    private void prompt(String desc) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(AlertType.ERROR);
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
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleDenyClick() {
        if (lvAlerts.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Ignore alert");
        alert.setContentText("Are you sure you want to ignore this alert?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            alerts.remove(lvAlerts.getSelectionModel().getSelectedIndex());
        }
    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
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
        System.out.println("Woah, initialized map!");
    }
}
