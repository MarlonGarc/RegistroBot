package umg.progra2.BotTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.progra2.Model.Respuesta;
import umg.progra2.Model.User;
import umg.progra2.Service.RespuestaService;
import umg.progra2.Service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotCuestionario extends TelegramLongPollingBot {
    private final Map<Long, Integer> indicePregunta = new HashMap<>();
    private final Map<Long, String> seccionActiva = new HashMap<>();
    private final Map<String, String[]> preguntas = new HashMap<>();
    private final UserService userService;
    private final RespuestaService respuestaService;

    @Override
    public String getBotUsername() {
        return "@MCasttleBot";
    }

    @Override
    public String getBotToken() {
        return "7402163045:AAHRZLY5A7w5mQsspET7H6gq83plwJ2hScg";
    }

    public BotCuestionario() {
        this.userService = new UserService();
        this.respuestaService = new RespuestaService();
        preguntas.put("SECTION_1", new String[]{"🤦‍♂️1.1- Estas aburrido?", "😂😂 1.2- Te bañaste hoy?", "🤡🤡 Pregunta 1.3"});
        preguntas.put("SECTION_2", new String[]{"Pregunta 2.1- ¿Que edad tienes?👴👦", "Pregunta 2.2- ¿Como estas? 👍👎", "Pregunta 2.3- ¿que te gusta hacer? 👁️👁️", "Pregunta 2.4- ¿Tu sabor de helado favorito? 🍦", "Pregunta 2.5- ¿De que pais eres? 🌎", "Pregunta 2.6- ¿Te gustaron las Preguntas? 🙂"});
        preguntas.put("SECTION_3", new String[]{"Pregunta 3.1", "Pregunta 3.2", "Pregunta 3.3"});
        preguntas.put("SECTION_4", new String[]{"Pregunta 4.1- ¿Como estas? 👍👎", "Pregunta 4.2- ¿Que edad tienes?👴👦", "Pregunta 4.3- ¿que te gusta hacer? 👁️👁️", "Pregunta 4.4- ¿Tu sabor de helado favorito? 🍦", "Pregunta 4.5- ¿De que pais eres? 🌎", "Pregunta 4.6- ¿Te gustaron las Preguntas? 🙂"});
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                long chatId = message.getChatId();
                String text = message.getText();

                // Verifica si el usuario está registrado
                try {
                    User user = userService.getUserByTelegramId(chatId);
                    if (user == null) {
                        if (text.startsWith("/start")) {
                            sendRegistrationRequest(chatId);
                        } else if (text.contains("@")) {
                            // Registro con correo
                            registerUser(chatId, text);
                        }
                    } else {
                        // Usuario registrado, manejar comando /menu o cuestionario
                        String seccion = seccionActiva.get(chatId);
                        if (text.equals("/menu")) {
                            sendMenu(chatId); // Usamos método sendMenu
                        } else if (seccion != null) {
                            manejarCuestionario(chatId, text);
                        } else {
                            sendText(chatId, "Envia /menu para iniciar el cuestionario.");
                        }
                    }
                } catch (SQLException e) {
                    sendText(chatId, "Hubo un problema con el servidor.");
                }
            }
        }
    }

    private void sendMenu(long chatId) {
        // Define las preguntas para cada sección
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selecciona una seccion:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Botones del menú
        rows.add(crearFilaBoton("Sección 1", "SECTION_1"));
        rows.add(crearFilaBoton("Sección 2", "SECTION_2"));
        rows.add(crearFilaBoton("Sección 3", "SECTION_3"));
        rows.add(crearFilaBoton("Sección 4", "SECTION_4"));

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<InlineKeyboardButton> crearFilaBoton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        String section = callbackQuery.getData(); // Obtiene el ID de la sección seleccionada

        // Inicia el cuestionario para la sección seleccionada
        inicioCuestionario(chatId, section);
    }

    private void inicioCuestionario(long chatId, String section) {
        seccionActiva.put(chatId, section);
        indicePregunta.put(chatId, 0);
        enviarPregunta(chatId);
    }

    private void enviarPregunta(long chatId) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        String[] questions = preguntas.get(seccion);

        if (index < questions.length) {
            sendText(chatId, questions[index]);
        } else {
            sendText(chatId, "¡Has completado el cuestionario!🎉🎉🎉");
            seccionActiva.remove(chatId);
            indicePregunta.remove(chatId);
        }
    }

    private void sendRegistrationRequest(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Por favor, proporcione su correo electrónico para registrarse.");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void registerUser(long chatId, String correo) {
        try {
            User user = new User();
            user.setTelegramid(chatId);
            user.setCorreo(correo);
            // Asume que el nombre y otros detalles serán completados en la base de datos o se les pedirá más tarde.
            userService.createUser(user);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Registro exitoso. Envía /menu para iniciar el cuestionario.");
            execute(message);
        } catch (SQLException e) {
            sendText(chatId, "Hubo un problema con el servidor.");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void manejarCuestionario(long chatId, String response) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.getOrDefault(chatId, 0);

        if ("SECTION_4".equals(seccion) && index == 1) { // Segunda pregunta de la cuarta sección
            if (!validarEdad(response)) {
                // Si la respuesta no es válida, solicita la edad nuevamente
                sendText(chatId, "Por favor, ingresa una edad válida (número entre 1 y 100).");
                return;
            }
        }
        // OBJETO RESPUESTA
        Respuesta respuesta = new Respuesta();
        respuesta.setSeccion(seccion);
        respuesta.setTelegramId(chatId);
        respuesta.setPreguntaId(index + 1); // Ajustar si el ID de la pregunta comienza en 1
        respuesta.setRespuestaTexto(response);

        sendText(chatId, "Tu respuesta fue: " + response);

        // Guarda la respuesta en la tabla tb_respuestas
        respuestaService.saveRespuesta(respuesta);

        indicePregunta.put(chatId, index + 1);
        enviarPregunta(chatId);
    }

    private boolean validarEdad(String edad) {
        try {
            int edadInt = Integer.parseInt(edad);
            return edadInt >= 1 && edadInt <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void sendText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
