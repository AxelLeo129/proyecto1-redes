package org.example;

import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;

import javax.security.sasl.SaslException;
import java.io.File;
import java.util.Scanner;

public class Menu {

    public Menu() { }

    public void start() throws Exception {
        boolean validateOption = false;
        Scanner sc = new Scanner(System.in);
        int version = 0;
        int authVersion = 0;
        boolean run = true;
        boolean sessionStarted = false;
        SmackOptions example = new SmackOptions();
        example.initializeAndConnect("alumchat.xyz", "alumchat.xyz");

        System.out.println("Axel Leonardo López 20768");
        System.out.println("Bienvenido al proyecto 1 de Redes");

        while(run) {
            System.out.println("Selecciona una opción:");
            while (!sessionStarted) {
                while (!validateOption) {
                    System.out.println("1. Registro\n2. Inicio de sesión:");
                    try {
                        authVersion = Integer.parseInt(sc.next());
                        if (authVersion == 1 || authVersion == 2)
                            validateOption = true;
                        else
                            System.out.println("Ingrese una opción válida\n");
                    } catch (Exception e) {
                        System.out.println("Ingrese una opción válida\n");
                    }
                }
                validateOption = false;

                switch (authVersion) {
                    case 1:
                        System.out.println("Ingrese el nombre del usuario:");
                        String name = sc.next();
                        System.out.println("Ingrese la contraseña:");
                        String password = sc.next();
                        example.registerAccount(name, password);
                        break;
                    case 2:
                        try {
                            System.out.println("Ingrese el nombre del usuario:");
                            String user = sc.next();
                            System.out.println("Ingrese la contraseña:");
                            String password1 = sc.next();
                            example.login(user, password1);
                            sessionStarted = true;
                        } catch (Exception e) {
                            System.out.println("Ingresa las credenciales correctas.");
                        }
                        break;
                    default:
                        System.out.println("Número inválido. Debe estar entre 1 y 2.");
                        break;
                }
            }

            while (!validateOption) {
                System.out.println("3. Cerrar sesión\n4. Eliminar cuenta\n5. Mostrar usuarios\n6. Agregar usuario a contactos\n7. Mostar detalles de un contacto\n8. Comunicación 1 a 1\n9. Conversación grupal\n10. Definir mensaje de presencia\n11: Enviar/Recibir notificaciones\n12. Enviar y recibir archivos:");
                try {
                    version = Integer.parseInt(sc.next());
                    if (version == 1 || version == 2 || version == 3 || version == 4 || version == 5 || version == 6 || version == 7 || version == 8 || version == 9 || version == 10 || version == 11 || version == 12)
                        validateOption = true;
                    else
                        System.out.println("Ingrese una opción válida\n");
                } catch (Exception e) {
                    System.out.println("Ingrese una opción válida\n");
                }
            }
            validateOption = false;
            switch (version) {
                case 3:
                    example.logout();
                    sessionStarted = false;
                    break;
                case 4:
                    example.deleteAccount();
                    sessionStarted = false;
                    break;
                case 5:
                    example.showAllContacts();
                    break;
                case 6:
                    System.out.println("Ingrese el nombre de usuario:");
                    String userName = sc.next();
                    BareJid bareJid = JidCreate.bareFrom(userName + "@alumchat.xyz");
                    example.addContact(bareJid, userName);
                    break;
                case 7:
                    System.out.println("Ingrese el nombre de usuario:");
                    String userName1 = sc.next();
                    example.showContactDetails(userName1);
                    break;
                case 8:
                    System.out.println("Ingrese el nombre de usuario:");
                    String userName2 = sc.next();
                    System.out.println("Ingrese el mensaje:");
                    String message = sc.next();
                    example.sendMessageToOne(userName2, message);
                    break;
                case 9:
                    System.out.println("Ingrese el su nickname:");
                    String nickname = sc.next();
                    System.out.println("Ingrese el nombre del room:");
                    String room = sc.next();
                    example.joinGroupChat(room, nickname);
                    break;
                case 10:
                    System.out.println("Ingrese el mensaje:");
                    String message1 = sc.next();
                    example.setPresenceMessage(message1);
                    break;
                case 12:
                    System.out.println("Ingrese el userId:");
                    String userId = sc.next();
                    System.out.println("Por favor, ingrese la ruta del archivo:");
                    String filePath = sc.nextLine();

                    File file = new File(filePath);

                    if (file.exists() && file.isFile()) {
                        example.sendFile(userId, file);
                    } else {
                        System.out.println("El archivo no existe o no es un archivo válido.");
                    }
                    break;
                default:
                    System.out.println("Número inválido. Debe estar entre 1 y 12.");
                    break;
            }

        }
    }


}
