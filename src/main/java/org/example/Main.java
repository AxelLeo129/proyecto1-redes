package org.example;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws XmppStringprepException {

        SmackConfiguration.DEBUG = true;

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost("alumchat.xyz")
                .setXmppDomain("alumchat.xyz")
                .setPort(5222) // Puerto estándar para XMPP. Cambia si es necesario.
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // Deshabilita la seguridad si no usas SSL/TLS
                .build();

        // Crear la conexión
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);

        try {
            // Conectar y autenticar (cambia 'username' y 'password' por tus credenciales)
            connection.connect();

            connection.login("lop20768", "Axel.129");

            System.out.println("Conectado a " + connection.getHost());
            // Asegúrate de que el servidor admite el registro de cuentas
            /*AccountManager accountManager = AccountManager.getInstance(connection);
            accountManager.sensitiveOperationOverInsecureConnection(true);

            if (accountManager.supportsAccountCreation()) {
                accountManager.createAccount(Localpart.fromOrThrowUnchecked("lop20768"), "Axel.129");
                System.out.println("Cuenta creada con éxito!");
            } else {
                System.out.println("El servidor no admite la creación de cuentas.");
            }*/

            //connection.disconnect();

            // Desconectar
            connection.disconnect();
        } catch (SmackException | XMPPException | InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}