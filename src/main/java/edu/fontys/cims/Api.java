package edu.fontys.cims;

import com.google.protobuf.GeneratedMessageV3;
import edu.fontys.cims.InitRequest.InitResponse;
import edu.fontys.cims.InitRequest.Message;
import io.socket.client.Manager;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marvin
 */
public class Api {

    private static final String HOST = "piminusone.tk";
    private static final int PORT = 8443;
    private static final String API_ENDPOINT = "https://" + HOST + ":" + PORT + "/api";
    public static final String SOCKET_ENDPOINT = "https://" + HOST + ":" + PORT;
    public static final String API_CRISIS = "/api/crisis";
    public static final String API_CHANGECRISIS = "/api/changecrisis";

    /**
     * Creates socket via room id
     *
     * @param roomId
     * @return
     */
    public static Socket createSocket(String roomId) {
        try {
            Manager manager = new Manager(new URI(SOCKET_ENDPOINT));
            Socket socket = manager.socket("/" + roomId);
            System.out.println("Room: " + roomId + " created");
            return socket;
        } catch (URISyntaxException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Initializes the application
     *
     * @return
     */
    public static InitResponse init() {
        try {
            URL url = new URL(API_ENDPOINT + "/init");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            return InitResponse.parseFrom(url.openStream());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("niet geconnect");
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, "No internet connection");
            //System.out.println("niet geconnect");
        }
        return null;
    }

    /**
     * Send message to server via proto message
     *
     * @param message
     * @return
     */
    public static boolean sendMessage(Message message) {
        try {
            URL url = new URL(API_ENDPOINT + "/chat");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/protobuf");
            message.writeTo(conn.getOutputStream());

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            return responseCode == 200;
        } catch (MalformedURLException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    /**
     * Send proto to server.
     * @param proto proto object
     */
    public static void sendProto(GeneratedMessageV3 proto, String link) {
        try {
            URL url = new URL(Api.SOCKET_ENDPOINT + link);
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
}
