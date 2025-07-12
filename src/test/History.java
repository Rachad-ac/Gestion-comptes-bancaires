package test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Timestamp;


public class History{
    private int id , numCompte;
    private String typeOperation , dateOperation ;  
    private double montant;
    private static String sql = "";
    private static Connection conn = Connex.getConnection();

    public History(int id, int numCompte, String typeOperation, double montant, String date) {
        this.id = id;
        this.numCompte = numCompte;
        this.typeOperation = typeOperation;
        this.montant = montant;
        this.dateOperation = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getnumCompte() {
        return numCompte;
    }

    public void setnumCompte(int numCompte) {
        this.numCompte = numCompte;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }
    
    public static Timestamp dateNow (){
        ZonedDateTime dateTimeActuel = ZonedDateTime.now(ZoneId.of("UTC")); 
        return Timestamp.from(dateTimeActuel.toInstant());
    }
    
   public static String getDate (LocalDateTime date) {
	  LocalDateTime dateOperation = date;
      DateTimeFormatter formatterUser = DateTimeFormatter.ofPattern("dd MMMM yyyy 'a' HH:mm:ss", Locale.FRENCH);
      return dateOperation.format(formatterUser);
   }
   
   public static void addHistory (int num , String type_operation , double montant){
        sql = "INSERT INTO historique ( num_compte, type_operation, montant, date_operation ) VALUES (?, ? ,? ,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, num);
			stmt.setString(2,type_operation);
			stmt.setDouble(3, montant );
            stmt.setTimestamp(4,dateNow());
            stmt.executeUpdate();
        } catch (Exception e) {
			System.out.println("ERREUR INSERT INTO HISTORY : "+ e.getMessage());

        }
    }

    public String afficherHistorique() {
        return "ID HISTORY : "+id+"  |  NUM COMPTE : "+numCompte+"  |  TYPE OPERATION : " +typeOperation +
        		"  |  MONTANT : "+ montant + " F CFA  "+"  |  DATE : " + dateOperation
        		+"\n--------------------------------------------------------------------------------------------------------"
        		+ "---------------------------------------------------------------------------------------------------------\n";
    }
}
