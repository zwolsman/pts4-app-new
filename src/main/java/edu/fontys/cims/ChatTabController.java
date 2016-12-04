package edu.fontys.cims;

import com.google.protobuf.InvalidProtocolBufferException;
import io.socket.client.Socket;
import java.net.URL;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chatArea.appendText("Connecting..\r\n");
    }

    public void btnSendClick() {
        //TODO api.sendMessage
        String message = messageField.getText().trim();
        chatArea.appendText(message + "\r\n");
        messageField.clear();
    }

    public void setId(String id) {
        Socket chat = Api.createSocket(id);
        chat.on("a message", (Object... os) -> {
            try {
                final InitRequest.Message message = InitRequest.Message.parseFrom((byte[]) os[0]);
                System.out.println(message.getText());
                chatArea.appendText(message.getId() + ": " + message.getText() + "\r\n");
            } catch (InvalidProtocolBufferException ex) {
            }
        });
        chat.on(Socket.EVENT_CONNECT, (Object... os) -> {
            System.out.println("I connected to chat '" + id + "'!");
        });
        chat.connect();
    }

}
