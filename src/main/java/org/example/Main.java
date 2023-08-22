package org.example;

import org.jxmpp.stringprep.XmppStringprepException;

public class Main {
    public static void main(String[] args) throws XmppStringprepException {

        try {
            Menu menu = new Menu();
            menu.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}