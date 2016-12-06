package edu.fontys.controllers;

import edu.fontys.controllers.AlertTabController;
import edu.fontys.controllers.ChatTabController;
import edu.fontys.controllers.CrisisTabController;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.Marker;
import edu.fontys.cims.CreateChatHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController implements Initializable, MapComponentInitializedListener, CreateChatHandler {

    @FXML
    public GoogleMapView mapView;
    public static GoogleMap map;
    public static Marker marker;

    @FXML
    public TabPane TabView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadAlertTab();
    }

    private void loadAlertTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AlertTab.fxml"));
            Tab tab = new Tab();
            tab.setText("Melding");
            tab.setContent(loader.load());

            AlertTabController controller = loader.getController();
            controller.mapView.addMapInializedListener(this);
            TabView.getTabs().add(0, tab);
            TabView.getSelectionModel().select(tab);

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCrisisTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CrisisTab.fxml"));
            Tab tab = new Tab();
            tab.setText("Crisis");
            tab.setContent(loader.load());

            CrisisTabController controller = loader.getController();
            controller.setCreateChatHandler(this);
            TabView.getTabs().add(1, tab);

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mapInitialized() {
        loadCrisisTab(); //Load if after the first map is initialized
    }

    @FXML
    TabPane chatTabPane;

    @Override
    public void createChat(int id) {
        try {
            System.out.println("Create chat tab with id " + id);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatTab.fxml"));
            Tab tab = new Tab();
            tab.setContent(loader.load());

            ChatTabController controller = loader.getController();
            controller.setId(id);

            tab.setText("Chat #" + id);
            chatTabPane.getTabs().add(tab);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
