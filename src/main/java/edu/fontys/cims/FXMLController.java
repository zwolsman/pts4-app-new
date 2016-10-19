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

public class FXMLController implements Initializable {

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
    }
}
