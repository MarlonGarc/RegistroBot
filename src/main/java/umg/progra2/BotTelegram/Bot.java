package umg.progra2.BotTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Bot extends TelegramLongPollingBot {
    private static final double EXCHANGE_RATE = 8.53;// Taza de Cambio Fija E a Q
    private static final List<Long> CHAT_IDS = Arrays.asList(
            6657627021L,
            1689396617L);


    @Override
    public String getBotUsername() {
        return "@MCasttleBot";
    }

    @Override
    public String getBotToken() {
        return "7402163045:AAHRZLY5A7w5mQsspET7H6gq83plwJ2hScg";
    }

    //El método onUpdateReceived(Update update) de la clase Bot se usa para manejar todas las actualizaciones que el
    // bot recibe.
    // Dependiendo del tipo de actualización, se toman diferentes acciones.

    @Override
    public void onUpdateReceived(Update update) {

        //Obtener información de la persona que manda los mensajes
        String nombre = update.getMessage().getFrom().getFirstName();
        String apellido = update.getMessage().getFrom().getLastName();
        String nickName = update.getMessage().getFrom().getUserName();

        //Se verifica si la actualización contiene un mensaje y si ese mensaje tiene texto.
        //Luego se procesa el contenido del mensaje y se responde según el caso.
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Hola" + nickName + " Tu Nombre es:" + nombre + " y tu Apellido es:" + apellido);
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.startsWith("/info")) {
                String response = String.format("Información personal:\nNombre: %s %s\nNúmero de carnet: 0905-23-19399\nSemestre: 4",
                        nombre, apellido);
                sendText(chat_id, response);
            }
            else if (message_text.toLowerCase().equals("/progra")) {
                sendText(chat_id, "La clase de progra es muy entretenida y sirve para resolver muchos problemas");
            }
            else if (message_text.startsWith("/hola")) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", new Locale("es", "ES"));
                String fecha = sdf.format(new Date(System.currentTimeMillis()));
                String saludo = String.format("Hola, %s, hoy es %s", nombre, fecha);
                sendText(chat_id, saludo);
            }
            else if (message_text.startsWith("/cambio")) {
                //Manejar cambio E a Q
                try {
                    String[] parts = message_text.split(" ");
                    if (parts.length == 2) {
                        double euros = Double.parseDouble(parts[1]);
                        double quetzales = euros * EXCHANGE_RATE;

                        String response = String.format("Son %.2f quetzales", quetzales);
                        sendText(chat_id, response);
                    }else {
                        sendText(chat_id, "Por favor envia el monto de euros despues del comando /cambio. Ejemplo /cambio 100");

                    }
                } catch (NumberFormatException e){
                    sendText(chat_id,"Por favor asegurate de que el monto enviado sea un numero valido");
                }

            }
            else if (message_text.startsWith("/grupal")) {
                String[] parts = message_text.split(" ", 2);
                if (parts.length == 2) {
                    String mensaje = parts[1];
                    for (Long id : CHAT_IDS) {
                        sendText(id, mensaje);
                    }
                    sendText(chat_id, "Mensaje enviado a los compañeros");
                }else {
                    sendText(chat_id, "Por favor envia el mensaje despues del comando /grupal. Ejemplo /grupal mensaje");
                }

            }
            else if (message_text.toLowerCase().equals("hola")) {
                sendText(chat_id, "Hola precioso " + nombre + " gusto de Saludarte 😁");
            }

            System.out.println("User id: " + chat_id + " Message: " + message_text);

        }
    }
    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}
