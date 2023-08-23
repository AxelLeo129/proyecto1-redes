package org.example;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
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

/**
 * The SmackOptions class provides methods to interact with an XMPP server using the Smack library.
 */
public class SmackOptions {

    private AbstractXMPPConnection connection;

    /**
     * Registers a new account on the XMPP server.
     *
     * @param username Desired username for the new account.
     * @param password Password for the new account.
     * @throws Exception if there's an error during registration.
     */
    public void registerAccount(String username, String password) throws Exception {
        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.sensitiveOperationOverInsecureConnection(true);
        accountManager.createAccount(Localpart.fromOrThrowUnchecked(username), password);
        System.out.println("Registrado");
    }

    /**
     * Logs in to the XMPP server using the provided credentials.
     *
     * @param username Username of the account.
     * @param password Password of the account.
     * @throws Exception if there's an error during login.
     */
    public void login(String username, String password) throws Exception {
        connection.login(username, password);
        System.out.println("Logged in");
    }

    /**
     * Logs out from the XMPP server.
     */
    public void logout() {
        connection.disconnect();
    }

    /**
     * Deletes the account from the XMPP server.
     *
     * @throws Exception if there's an error during account deletion.
     */
    public void deleteAccount() throws Exception {
        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.deleteAccount();
    }

    /**
     * Displays all contacts and their status.
     */
    public void showAllContacts() {
        Roster roster = Roster.getInstanceFor(connection);
        for (RosterEntry entry : roster.getEntries()) {
            Presence presence = roster.getPresence(entry.getJid());
            System.out.println("- " + entry.getJid() + " - " + presence.getStatus());
        }
    }

    /**
     * Adds a user to the contact list.
     *
     * @param userJid User's JID.
     * @param name    Name to be displayed in the contact list.
     * @throws Exception if there's an error during contact addition.
     */
    public void addContact(BareJid userJid, String name) throws Exception {
        Roster roster = Roster.getInstanceFor(connection);
        roster.createEntry(userJid, name, null);
        Presence subscribe = new Presence(Presence.Type.subscribe);
        subscribe.setTo(userJid);
        connection.sendStanza(subscribe);
        roster.reloadAndWait();
        if (roster.getEntry(userJid) != null) {
            System.out.println("Contacto agregado satisfactoriamente");
        } else {
            System.out.println("Fallo al agregar el contacto");
        }
    }

    /**
     * Displays details of a specific contact.
     *
     * @param userJid User's JID.
     * @throws XmppStringprepException if there's an error related to XMPP string preparation.
     */
    public void showContactDetails(String userJid) throws XmppStringprepException {
        Roster roster = Roster.getInstanceFor(connection);
        RosterEntry entry = roster.getEntry(JidCreate.bareFrom(userJid));
        if (entry != null) {
            System.out.println(entry.getJid() + " - " + entry.getName());
        } else {
            System.out.println("No se encontró el contacto con JID: " + userJid);
        }
    }

    /**
     * Sends a one-to-one message to a user.
     *
     * @param userJid User's JID.
     * @param message Message content.
     * @throws Exception if there's an error during message sending.
     */
    public void sendMessageToOne(String userJid, String message) throws Exception {
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        EntityBareJid jid = JidCreate.entityBareFrom(userJid);
        Chat chat = chatManager.chatWith(jid);
        chat.send(message);
    }

    /**
     * Sends a one-to-one message to a user.
     *
     * @param roomJid Room JID.
     * @param nickname Nickname.
     * @throws Exception if there's an error during message sending.
     */
    public void joinGroupChat(String roomJid, String nickname) throws Exception {
        MultiUserChatManager mucManager = MultiUserChatManager.getInstanceFor(connection);
        MultiUserChat muc = mucManager.getMultiUserChat(JidCreate.entityBareFrom(roomJid));
        muc.join(Resourcepart.fromOrNull(nickname));
    }

    /**
     * Send presence message
     *
     * @param message Message content.
     * @throws Exception if there's an error during message sending.
     */
    public void setPresenceMessage(String message) throws Exception {
        Presence presence = new Presence(Presence.Type.available, message, 1, Presence.Mode.available);
        connection.sendStanza(presence);
    }

    /**
     * Sends a one-to-one file to a user.
     *
     * @param userJid User's JID.
     * @param file File.
     * @throws Exception if there's an error during message sending.
     */
    public void sendFile(String userJid, File file) throws Exception {
        FileTransferManager ftm = FileTransferManager.getInstanceFor(connection);
        OutgoingFileTransfer transfer = ftm.createOutgoingFileTransfer(JidCreate.entityFullFrom(userJid));
        transfer.sendFile(file, "Sending a file");
    }

    /**
     * Create server connection.
     *
     * @param host String.
     * @param domain String.
     * @throws Exception if there's an error during message sending.
     */
    public void initializeAndConnect(String host, String domain) throws XmppStringprepException {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost(host)
                .setXmppDomain(domain)
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();
        connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener((from, message, chat) -> {
                System.out.println("Nuevo mensaje de " + from + ": " + message.getBody());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
