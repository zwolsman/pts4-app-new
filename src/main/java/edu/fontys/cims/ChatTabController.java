/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fontys.cims;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author juleb
 */
public class ChatTabController implements Initializable {

    @FXML
    public TextArea ChatBox_TextArea;
    
    @FXML
    public TextField Message_TextField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void handleSendClick(){
        ChatBox_TextArea.setText(ChatBox_TextArea.getText() + Message_TextField.getText() + "\n");
        InitRequest.Message message = InitRequest.Message.newBuilder().setId(0).setCrisisid(0).setText(ChatBox_TextArea.getText()).build();
        
    }
}
