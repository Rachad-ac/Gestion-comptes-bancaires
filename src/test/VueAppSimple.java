
package test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class VueAppSimple {
	private static Connection conn = Connex.getConnection();
	public static String sortie = "" , sql = "" , update = "";
	public static int num ;
	
	public static void addCompte() {
		String name, password;
		int n;
        Double solde;
        sql = "INSERT INTO comptes (password , name , num , solde) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			n = Integer.parseInt(JOptionPane.showInputDialog("Combien de compte souhaitez vous ajouter ?"));
			for (int i = 0 ; i < n ; i++) {
		        name = JOptionPane.showInputDialog("Entrez le nom du titulaire numero : "+ (i + 1));
		        password = JOptionPane.showInputDialog("Entrez le mot de passe du compte numero : "+ (i + 1));
                num = Integer.parseInt(JOptionPane.showInputDialog("Entrez le numero du compte numero : "+ ( i+ 1)));
                solde = Double.parseDouble(JOptionPane.showInputDialog("Entrez le montant initial du compte numero : "+ (i + 1)));
                stmt.setString(1,password);
                stmt.setString(2,name);
                stmt.setInt(3,num);
                stmt.setDouble(4,solde);
                stmt.executeUpdate();
            	JOptionPane.showMessageDialog(null,"Compte numero 0 "+ (i + 1) +" enregistre avec succes");
			}
        } catch (Exception e) {
			System.out.println("ERREUR INSERT INTO : "+ e.getMessage());

        }
	}
	
    public static void getCompte() {
    	num = Integer.parseInt(JOptionPane.showInputDialog("Donner le numero du compte a rechercher : ")); 
    	sql = "SELECT * FROM comptes WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){ 	
            stmt.setInt(1, num);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	sortie += new Compte(rs.getInt("id"), rs.getString("password") ,rs.getString("name"), 
            			             rs.getInt("num"), rs.getDouble("solde")).Afficher();
                JOptionPane.showMessageDialog(null,"Compte trouver !!! \n\n"+sortie);
                sortie = "";
            }else {
            	JOptionPane.showMessageDialog(null,"Numero incorret \n"
            									  +"Compte introuvable !!!");
            }
        } catch (Exception e) {
			System.out.println("ERREUR SELECT * : "+ e.getMessage());

        }
    }

    public static void getAllComptes() {
    	sql = "SELECT * FROM comptes";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        	if (rs != null) {
        		while (rs.next()) {
                	sortie += new Compte(rs.getInt("id") , rs.getString("password") , rs.getString("name"), 
                			             rs.getInt("num"), rs.getDouble("solde")).Afficher();
                }
        		if (sortie.length() > 0 ) {
        			JOptionPane.showMessageDialog(null,"Listes des comptes :\n\n"+sortie);
        			sortie = "";
        		}else {
            	 JOptionPane.showMessageDialog(null,"Aucun compte disponible !!!");
        		}
        	}
        } catch (Exception e) {
			System.out.println("ERREUR SELECT NUM : "+ e.getMessage());
        }
     }
    
    public static void getAllHistory () {
    	num  = Integer.parseInt(JOptionPane.showInputDialog("Donner le numero du compte a rechercher : ")); 
   	    sql = "SELECT * FROM historique WHERE num_compte = ?";
   	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
   	        stmt.setInt(1, num);
   	        ResultSet rs = stmt.executeQuery();
      	        if (rs != null) {
      	        	while (rs.next()) {
            	         String dateOperation = History.getDate(rs.getTimestamp("date_operation").toLocalDateTime());
            	         sortie += new History(rs.getInt("id") , rs.getInt("num_compte") , rs.getString("type_operation") ,
            	        		 	           rs.getDouble("montant") , dateOperation).afficherHistorique();
      	        	}	
      	        	if (sortie.length() > 0) {
          	        	JOptionPane.showMessageDialog(null,"Historique des transations : \n\n"+ sortie);
          	        	sortie = "";
      	        	}else {
      	        		JOptionPane.showMessageDialog(null,"Aucune historique disponible pour ce compte!!!");
      	        	}
      	        }
   	    } catch (Exception e) {
   	        System.out.println("Erreur affichage historique : " + e.getMessage());
   	    }
   	}
    
    public static void updateDepot () {
    	double newSolde , montant;
    	sql = "SELECT solde FROM comptes WHERE num = ?";
    	try (PreparedStatement st = conn.prepareStatement(sql)) {
            num = Integer.parseInt(JOptionPane.showInputDialog("Donner le numero du compte : ")); 
        	st.setInt(1,num);
        	ResultSet rs = st.executeQuery();
        	update = "UPDATE comptes SET solde = ? WHERE num = ?";
        	PreparedStatement stmt = conn.prepareStatement(update);
    		montant = Double.parseDouble(JOptionPane.showInputDialog("Donner le montant a deposer : "));
        	if (rs.next()) {
        		newSolde = montant + rs.getDouble("solde");
    			stmt.setDouble(1, newSolde);
    			stmt.setInt(2,num);
                stmt.executeUpdate();
    			JOptionPane.showMessageDialog(null,"Transation enregistrer !!!\n\n"
    											   +"Details operation"
    											   +"\nNature : Depot"
    					                           +"\nMontant : "+ montant +" F CFA"
                                                   +"\nNouveau solde : "+ newSolde +" F CFA");
            	History.addHistory(num , "Depot" , montant);
        	}else {
    			JOptionPane.showMessageDialog(null,"Numero incorrect , compte introuvable !!! \n\t\tEchec depot");
        	}
        } catch (Exception e) {
			System.out.println("ERREUR UPDATE SOLDE + : "+ e.getMessage());
        }
    }
    
    public static void updateRetrait () {
    	double newSolde , montant;
    	String pass;
    	sql = "SELECT * FROM comptes WHERE num = ?";
    	try (PreparedStatement st = conn.prepareStatement(sql)) {
        	num = Integer.parseInt(JOptionPane.showInputDialog("Donner le numero du titulaire : "));
    		montant = Double.parseDouble(JOptionPane.showInputDialog("Donner le montant a retirer : "));
        	st.setInt(1,num);
        	ResultSet rs = st.executeQuery();
        	update = "UPDATE comptes SET solde = ? WHERE num = ?";
        	PreparedStatement stmt = conn.prepareStatement(update);
        	if (rs.next()) {
        		pass = JOptionPane.showInputDialog("Entrer votre mot de passe");
        		if(montant <= rs.getDouble("solde") && pass.equals(rs.getString("password"))) {
        			newSolde = rs.getDouble("solde") - montant;
        			stmt.setDouble(1, newSolde);
        			stmt.setInt(2,num);
                    stmt.executeUpdate();
                	History.addHistory(num , "Retrait" , - montant);
                    JOptionPane.showMessageDialog(null,"Transation enregistrer !!!\n"
                    								  +"Details operation"
							   						  +"\nNature : Retrait"
							   						  +"\nMontant : "+ montant +" F CFA"
							   						  +"\nNouveau solde : "+ newSolde +" F CFA");
        		}else {
        			JOptionPane.showMessageDialog(null,"Solde issufissant ou mot de passe incorrect");
        		}
        	}else {
    			JOptionPane.showMessageDialog(null,"Numero inccoret , compte introuvable !!! \nEchec retrait");
        	}
        } catch (Exception e) {
			System.out.println("ERREUR UPDATE SOLDE - : "+ e.getMessage());
        }
    }
    
    public static void removeCompte () {
    	num = Integer.parseInt(JOptionPane.showInputDialog("Donner le numero du compte a rechercher : "));
		sql = "SELECT * FROM comptes WHERE num = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)){
        	st.setInt(1,num);
        	ResultSet rs = st.executeQuery();
    		update = "DELETE FROM comptes WHERE num = ?";
        	PreparedStatement stmt = conn.prepareStatement(update);
        	if(rs.next()) {
        		stmt.setInt(1, num);
                stmt.executeUpdate();
            	JOptionPane.showMessageDialog(null,"Le compte est bien supprimmer !!! \n");
        	}else {
            	JOptionPane.showMessageDialog(null,"Numero incorect , compte introuvable !!! \nEchec suppression");
        	} 
        } catch (Exception e) {
			System.out.println("ERREUR DELETE : "+ e.getMessage());
        }
    }
    
    public static void removeHistory () {
    	int id = Integer.parseInt(JOptionPane.showInputDialog("Donner l'ID de l'historique a rechercher : "));
		sql = "SELECT id FROM historique WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)){
        	st.setInt(1,id);
        	ResultSet rs = st.executeQuery();
    		update = "DELETE FROM historique WHERE id = ?";
        	PreparedStatement stmt = conn.prepareStatement(update);
        	if(rs.next()) {
        		stmt.setInt(1,id);
                stmt.executeUpdate();
            	JOptionPane.showMessageDialog(null,"L'historique avec "+ id+" est bien supprimmer !!! \\n");
        	}else {
            	JOptionPane.showMessageDialog(null,"ID incorect , historique introuvable !!! \n\t\tEchec suppression");
        	} 
        } catch (Exception e) {
			System.out.println("ERREUR DELETE : "+ e.getMessage());
        }
    }
    
    public static int menu () {
    	int choixMenu = Integer.parseInt(JOptionPane.showInputDialog(
    			                     "----------------------- Menu -------------------------\n"
    			                     +"1 - Creer des comptes \n"
    			                     +"2 - Afficher les comptes \n"
    			                     +"3 - Rechercher un compte \n"
    			                     +"4 - Effectuer un depot \n"
    			                     +"5 - Effectuer un retrait \n"
    			                     +"6 - Supprimer un compte\n"
    			                     +"7 - Supprimer une historique \n"
    			                     +"8 - Afficher l'historique transation \n"
    			                     +"9 - Quitter \n"
    			                     +"Faites votre choix \n"
    			                     +"-------------------------------------------------------"));
    	return choixMenu;
    }

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel( new NimbusLookAndFeel() );

		if (conn != null) {
			try {
				boolean next = true;
				 while (next) {
						int choix = menu();
				    	switch (choix) {
						case 1 : addCompte();
			    			break;
						case 2 : getAllComptes();
							break;
						case 3 : getCompte();
			    			break;
						case 4 : updateDepot();
			    			break;
						case 5 : updateRetrait();
							break;
						case 6 : removeCompte();
			    			break;
						case 7 : removeHistory();
		    				break;
						case 8 : getAllHistory();
		    				break;
						case 9 : int a = JOptionPane.showConfirmDialog(null,"Es tu sur de quitter l'app !!!");
						 		 if (a == 0) {
						 			 JOptionPane.showMessageDialog(null,"Merci d'avoir fais appel a nous");
						 			 next = false;
						 			 conn.close();
									 System.out.println("Connexion  fermer");
						 		 }
							break;	
			    		default : JOptionPane.showMessageDialog(null,"Desole ! ce choix n'existe pas");
			    			break;
						}
				 }
			}catch (Exception e) {
				System.out.println("Echec fermeture connexion  : " + e.getMessage());
			}
		}else {
			System.out.println("Echec connexion ");
		}
	}
		
}