package umg.progra2.Service;


import umg.progra2.DAO.UserDao;
import umg.progra2.Model.User;
import umg.progra2.db.DatabaseConnection;
import umg.progra2.db.TransactionManager;

import java.sql.Connection;
import java.sql.SQLException;


public class UserService {
    private UserDao userDao = new UserDao();

    public void deleteUserByEmail(String email) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            TransactionManager tm = new TransactionManager(connection);
            tm.beginTransaction();
            try {
                userDao.deleteUserByEmail(email);
                tm.commit();
            } catch (SQLException e) {
                tm.rollback();
                throw e;
            }
        }
    }


    public void createUser(User user) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            TransactionManager tm = new TransactionManager(connection);
            tm.beginTransaction();
            try {
                userDao.insertUser(user);
                tm.commit();
            } catch (SQLException e) {
                tm.rollback();
                throw e;
            }
        }
    }

    public void updateUser(User user) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            TransactionManager tm = new TransactionManager(connection);
            tm.beginTransaction();
            try {
                userDao.updateUser(user);
                tm.commit();
            } catch (SQLException e) {
                tm.rollback();
                throw e;
            }
        }
    }


    public User getUserByTelegramId(long telegramid) throws SQLException {
        return userDao.getUserByTelegramId(telegramid);
    }

    public User getUserByEmail(String Email) throws SQLException {
        return userDao.getUserByEmail(Email);
    }

    public User getUserByCarne(String carne) throws SQLException {
        return userDao.getUserBycarne(carne);
    }
}
