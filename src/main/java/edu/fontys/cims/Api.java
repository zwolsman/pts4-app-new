package edu.fontys.cims;

import edu.fontys.cims.InitRequest.InitResponse;
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

    private static final String HOST = "localhost";
    private static final int PORT = 3000;
    private static final String API_ENDPOINT = "http://" + HOST + ":" + PORT + "/api";
    public static final String SOCKET_ENDPOINT = "http://" + HOST + ":" + PORT;

    public static Socket createSocket(String roomId) {
        try {
            Manager manager = new Manager(new URI(SOCKET_ENDPOINT));
            Socket socket = manager.socket("/" + roomId);
            System.out.println("Room: " + roomId + "created");
            return socket;
        } catch (URISyntaxException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

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
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("niet geconnect");
        }
        return null;
    }

    public static void updateAlert() {
        try {
            URL url = new URL("http://localhost:3001/alert");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/protobuf");

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
