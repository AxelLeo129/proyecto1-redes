package org.example;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;

public class SmackOptions {

    private AbstractXMPPConnection connection;

    // 1) Registrar una nueva cuenta en el servidor
    public void registerAccount(String username, String password) throws Exception {
        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.createAccount(Localpart.fromOrThrowUnchecked(username), password);
    }

    // 2) Iniciar sesión con una cuenta
    public void login(String username, String password) throws Exception {
        connection.login(username, password);
    }

    // 3) Cerrar sesión con una cuenta
    public void logout() {
        connection.disconnect();
    }

    // 4) Eliminar la cuenta del servidor
    public void deleteAccount() throws Exception {
        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.deleteAccount();
    }

    // 1) Mostrar todos los usuarios/contactos y su estado
    public void showAllContacts() {
        Roster roster = Roster.getInstanceFor(connection);
        for (RosterEntry entry : roster.getEntries()) {
            Presence presence = roster.getPresence(entry.getJid());
            System.out.println(entry.getJid() + " - " + presence.getStatus());
        }
    }

    // 2) Agregar un usuario a los contactos
    public void addContact(BareJid userJid, String name) throws Exception {
        Roster roster = Roster.getInstanceFor(connection);
        roster.createEntry(userJid, name, null);
    }

    // 3) Mostrar detalles de contacto de un usuario
    public void showContactDetails(String userJid) throws XmppStringprepException {
        Roster roster = Roster.getInstanceFor(connection);
        RosterEntry entry = roster.getEntry(JidCreate.bareFrom(userJid));
        System.out.println(entry.getJid() + " - " + entry.getName());
    }

    // 4) Comunicación 1 a 1 con cualquier usuario/contacto
    public void sendMessageToOne(String userJid, String message) throws Exception {
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        EntityBareJid jid = JidCreate.entityBareFrom(userJid);
        Chat chat = chatManager.chatWith(jid);
        chat.send(message);
    }

    // 5) Participar en conversaciones grupales
    public void joinGroupChat(String roomJid, String nickname) throws Exception {
        MultiUserChatManager mucManager = MultiUserChatManager.getInstanceFor(connection);
        MultiUserChat muc = mucManager.getMultiUserChat(JidCreate.entityBareFrom(roomJid));
        muc.join(Resourcepart.fromOrNull(nickname));
    }

    // 6) Definir mensaje de presencia
    public void setPresenceMessage(String message) throws Exception {
        Presence presence = new Presence(Presence.Type.available, message, 1, Presence.Mode.available);
        connection.sendStanza(presence);
    }

    // 7) Enviar/recibir notificaciones (esto es similar a enviar/recibir mensajes)

    // 8) Enviar/recibir archivos
    public void sendFile(String userJid, File file) throws Exception {
        FileTransferManager ftm = FileTransferManager.getInstanceFor(connection);
        OutgoingFileTransfer transfer = ftm.createOutgoingFileTransfer(JidCreate.entityFullFrom(userJid));
        transfer.sendFile(file, "Sending a file");
    }

    // Inicializar y conectar
    public void initializeAndConnect(String host, String domain) throws XmppStringprepException {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost(host)
                .setXmppDomain(domain)
                .build();
        connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
