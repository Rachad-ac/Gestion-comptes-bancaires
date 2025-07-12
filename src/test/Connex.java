
package test;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class Connex {
	private static final String url = "jdbc:mysql://localhost:3306/banque_db";
    private static final String user = "root"; 
    private static final String password = ""; 

    public static Connection getConnection() {
    	 Connection conn = null;
    	 try {
 			Class.forName("com.mysql.cj.jdbc.Driver");
 			conn = DriverManager.getConnection(url, user, password);
 		
 			String tableComptes = "CREATE TABLE IF NOT EXISTS comptes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "password VARCHAR(50) UNIQUE NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "num VARCHAR(50) UNIQUE NOT NULL, " +
                    "solde DOUBLE NOT NULL DEFAULT 0.0" +
                    ") ENGINE=InnoDB;";

 			String tableHistorique = "CREATE TABLE IF NOT EXISTS historique (" +
                       "id INT AUTO_INCREMENT PRIMARY KEY, " +
                       "num_compte VARCHAR(50) NOT NULL, " +
                       "type_operation VARCHAR(50) NOT NULL, " +
                       "montant DOUBLE NOT NULL, " +
                       "date_operation DATETIME NOT NULL, " +
                       "FOREIGN KEY (num_compte) REFERENCES comptes(num) ON DELETE CASCADE" +
                       ") ENGINE=InnoDB;";

 			try (Statement stmt = conn.createStatement()) {
 			        stmt.executeUpdate(tableComptes);
 			        stmt.executeUpdate(tableHistorique);
 			        System.out.println("Tables vérifiées et créées avec succès !");
 			    } catch (Exception e) {
 			        System.out.println("Erreur création des tables : " + e.getMessage());
 			    }
         } catch (Exception e) {
 			System.out.println("Erreur : " + e.getMessage());
 			e.printStackTrace();
         }
    	return conn;
    }
        
}
