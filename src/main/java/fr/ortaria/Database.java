package fr.ortaria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    public static Connection openConnection() {
        try {
            // Vérifier si la connexion est déjà ouverte
            if (connection != null && !connection.isClosed()) {
                return connection; // Retourner la connexion existante si elle est toujours ouverte
            }

            synchronized (Database.class) {
                // Double vérification pour éviter d'ouvrir plusieurs connexions dans un contexte multithread
                if (connection == null) {
                    String url = "jdbc:mysql://192.168.86.40:3306/ortaria";
                    String user = "leandro";
                    String password = "dev"; // Assurez-vous que votre mot de passe est correct (vide par défaut avec WAMP)

                    // Assurez-vous d'avoir le driver JDBC MySQL dans votre classpath
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    connection = DriverManager.getConnection(url, user, password);
                    System.out.println("Connexion à la base de données réussie.");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null; // Retourner null en cas d'échec de connexion
        }
        return connection; // Retourner la connexion si elle est établie avec succès
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Assurer que la référence est nettoyée
                System.out.println("Connexion à la base de données fermée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
