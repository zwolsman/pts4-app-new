/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fontys.cims;

import edu.fontys.cims.InitRequest.Crisis;
import io.socket.client.Socket;

/**
 *
 * @author geene
 */
public final class Globals {

    private Globals() {
    }

    public static Crisis selectedCrisis;
    public static Socket chat;
}
