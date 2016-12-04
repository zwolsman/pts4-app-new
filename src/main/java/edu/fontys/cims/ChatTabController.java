package edu.fontys.cims;

import com.google.protobuf.InvalidProtocolBufferException;
import io.socket.client.Socket;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Marvin
 */
public class ChatTabController implements Initializable {

    @FXML
    private Button btnSend;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageField;

    private int crisisId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appendText("Connecting..");
    }

    @FXML
    public void btnSendClick() {
        InitRequest.Message message = InitRequest.Message.newBuilder()
                .setCrisisid(crisisId)
                .setId(1)
                .setText(messageField.getText().trim()).build();
        if (Api.sendMessage(message)) {
            appendText("Me: " + message.getText());
            messageField.clear();
        } else {
            appendText("Something went wrong while sending your message. Please try again later..");
        }
    }

    public void setId(int id) {
        Socket chat = Api.createSocket(String.valueOf(id));
        chat.on("a message", (Object... os) -> {
            try {
                final InitRequest.Message message = InitRequest.Message.parseFrom((byte[]) os[0]);
                System.out.println(message.getText());
                appendText(message.getId() + ": " + message.getText());
            } catch (InvalidProtocolBufferException ex) {
            }
        });
        chat.on(Socket.EVENT_CONNECT, (Object... os) -> {
            System.out.println("I connected to chat '" + id + "'!");
            appendText("Connected!");

        });
        chat.connect();
    }

    private void appendText(String message) {

        Date now = new Date();

        chatArea.appendText("[" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "]\t" + message.trim() + "\r\n");
    }
}
