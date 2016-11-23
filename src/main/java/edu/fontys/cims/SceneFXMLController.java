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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SceneFXMLController implements Initializable, MapComponentInitializedListener {

    @FXML
    public GoogleMapView mapView;
    public static GoogleMap map;
    public static Marker marker;
    public static TabPane TabView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        mapView.addMapInializedListener(this);
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

        TabView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                String id = t1.idProperty().getValue();
                if (id.equals("MeldingTab") || id.equals("CrisisTab")) {
                    mapView.visibleProperty().set(true);
                } else if (id.equals("ChatTab")) {
                    mapView.visibleProperty().set(false);
                    Globals.chat = Api.createSocket(String.valueOf(Globals.selectedCrisis.getId()));
                }
            }
        }
        );
    }

}
