package umg.progra2;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import umg.progra2.BotTelegram.Bot;
import umg.progra2.BotTelegram.BotCuestionario;
import umg.progra2.Model.User;
import umg.progra2.Service.UserService;

import java.sql.SQLException;

public class Main {

        //OPERACIONES CRUD
        private static void PruebaInsertaUsuario() {
            //explicación:
            // 1. Servicio
            //Servicio (UserService.java):
            //La clase UserService actúa como intermediario entre el controlador y la capa de acceso a datos (DAO).
            // Se encarga de la lógica de negocio, validaciones y de coordinar las transacciones.
            // 2. DAO
            //Capa de Acceso a Datos (UserDao.java):
            //Esta clase contiene los métodos para interactuar con la base de datos, usando la
            // conexión proporcionada por DatabaseConnection. Aquí es donde se construyen y ejecutan
            // las consultas SQL.
            // 3. Conexión a la Base de Datos
            //Gestión de la Conexión (DatabaseConnection.java):
            //Esta clase es responsable de proporcionar la conexión a la base de datos. Puede leer la configuración
            // desde un archivo de propiedades (application.properties) para obtener los detalles de conexión.
            // 4. Transacciones
            //Gestión de Transacciones (TransactionManager.java):
            //Esta clase se encarga de iniciar, confirmar o revertir transacciones en la base de datos.
            // Se utiliza para agrupar varias operaciones en una sola transacción y garantizar la integridad de los datos.
            // 5. Modelo (User.java):
            //La clase User representa la estructura de los datos que se insertan en la base de datos.
            // Es una clase POJO (Plain Old Java Object) con atributos, getters y setters.

            //invoca el servicio que manejará la lógica de negocio.
            UserService userService=new UserService();
            User user = new User();

            // Crear un nuevo usuarioUseruser=newUser();
            user.setCarne("0905-23-11865");
            user.setNombre("Marlon Castillo");
            user.setCorreo("marloncastillo424@gmail.com");
            user.setSeccion("A");
            user.setTelegramid(1234567890L);
            user.setActivo("Y");

            try {
                userService.createUser(user);
                System.out.println("User created successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Hay clavos");
            }
        }

        private static void PruebaActualizacionUsuario() {
            UserService servicioUsuaio = new UserService();

            User usurioObtenido;
            //obtener información del usuario por correo electrónico
            try {
                usurioObtenido = servicioUsuaio.getUserByEmail("mcastillog31@gmail.com");
                System.out.println("Retrieved User: " + usurioObtenido.getNombre());
                System.out.println("Retrieved User: " + usurioObtenido.getCorreo());
                System.out.println("Retrieved User: " + usurioObtenido.getId());

                //actualizar información del usuario
                usurioObtenido.setCarne("0905-23-11865");
                usurioObtenido.setNombre("Marlon Castillo");
                usurioObtenido.setCorreo("mcastillog31@miumg.edu.gt");
                usurioObtenido.setSeccion("A");
                usurioObtenido.setTelegramid(1234567890L);
                usurioObtenido.setActivo("Y");

                servicioUsuaio.updateUser(usurioObtenido);
                System.out.println("User updated successfully!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        private static void PruebaEliminarUsuario() {
            UserService servicioUsuaio = new UserService();
            try {
                servicioUsuaio.deleteUserByEmail("mcastillog31@miumg.edu.gt");
                System.out.println("User deleted successfully!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        }
        private static void PruebaObtenerUsuarioPorCarne() {
            UserService servicioUsuaio = new UserService();
            try {
                User usuarioObtenido = servicioUsuaio.getUserByCarne("0905-23-11865");
                if (usuarioObtenido != null) {
                    System.out.println("Usuario obtenido: " + usuarioObtenido.getNombre());
                }
                else {
                    System.out.println("Usuario no encontrado");
                }
            }
            catch (SQLException e) {
                System.out.println("Hay clavos");
            }
        }
        public static void main(String[] args) {
            try {
// Inicializa la API de Telegram
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

// Registra ambos bots
                botsApi.registerBot(new BotCuestionario());


                System.out.println("Bot registrado exitosamente.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }