package org.example;

import org.jxmpp.stringprep.XmppStringprepException;

/**
 * The Main class serves as the entry point for the chat application.
 */
public class Main {

    /**
     * The main method initializes the chat application and starts the main menu.
     *
     * @param args Command line arguments (not used in this application).
     * @throws XmppStringprepException if there's an error related to XMPP string preparation.
     */
    public static void main(String[] args) throws XmppStringprepException {

        try {
            // Create a new Menu instance
            Menu menu = new Menu();
            // Start the main menu loop
            menu.start();
        } catch (Exception e) {
            // Print any exceptions that occur
            e.printStackTrace();
        }

    }
}