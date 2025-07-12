package test;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;


public class VueAppSwing {
	private static Connection conn = Connex.getConnection();
	public static String sortie = "" , sql = "" , update = "";
	public static int num;
	private static DefaultTableModel tableModel , tableModel1;
	
	private static void addCompte() {
		// Création de la fenêtre
        JFrame Fcreat = new JFrame();
        Fcreat.setTitle("Ajouter un compte");
        Fcreat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Fcreat.setSize(415, 230);
        Fcreat.setLocationRelativeTo(null);
        // Création du panneau principal
        JPanel Pcreat = new JPanel();
        JPanel P = new JPanel();
        JPanel pb = new JPanel();
        pb.setLayout(new FlowLayout(FlowLayout.RIGHT, 15,10));
        P.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10)); 
        Pcreat.setLayout(new GridLayout(5, 2, 5, 5));
        // Création des labels et des champs de texte
        JLabel nameLabel = new JLabel("Nom du titulaire :");
        JTextField names = new JTextField(20);
        JLabel numeLabel = new JLabel("Numéro de compte :");
        JTextField numes = new JTextField(20);
        JLabel passLabel = new JLabel("Mot de passe :");
        JTextField pass = new JTextField(20);
        JLabel soldeLabel = new JLabel("Solde initiale :");
        JTextField soldes = new JTextField(20);
        // Création du bouton Valider avec une taille réduite
        JButton Btn = new JButton("Ajouter");
        // Ajout des composants au panneau
        Pcreat.add(nameLabel);
        Pcreat.add(names);
        Pcreat.add(numeLabel);
        Pcreat.add(numes);
        Pcreat.add(passLabel);
        Pcreat.add(pass);
        Pcreat.add(soldeLabel);
        Pcreat.add(soldes);
        Pcreat.add(new JLabel()); 
        Pcreat.add(Btn);
        // Ajout du panneau à la fenêtre
        pb.add(Btn);
        P.add(Pcreat);
        Fcreat.add(P);
        Fcreat.add(pb, BorderLayout.SOUTH);
        Fcreat.setVisible(true);       
        Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	String name, password;
                Double solde;
                sql = "INSERT INTO comptes (password , name , num , solde) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        			name = names.getText();
        			password = pass.getText();
        			num = Integer.parseInt(numes.getText());
        			solde = Double.parseDouble(soldes.getText());
        			stmt.setString(1,password);
        			stmt.setString(2,name);
        			stmt.setInt(3,num);
        			stmt.setDouble(4,solde);
        			stmt.executeUpdate();
        			JOptionPane.showMessageDialog(null,"Compte enregistre avec succes");
        					
                } catch (Exception e) {
        			System.out.println("ERREUR INSERT INTO : "+ e.getMessage());

                } 
            }
        });
	}
    
	public static void getCompte(JTextField numes ) {
    	num = Integer.parseInt(numes.getText()); 
    	sql = "SELECT * FROM comptes WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){ 	
            stmt.setInt(1, num);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { 
            	tableModel.setRowCount(0);
            	tableModel.addRow(new Object[]{
                		rs.getInt("id"),
                		rs.getString("name"), 
                		rs.getString("password") ,
        				rs.getInt("num"),
        				rs.getDouble("solde")}); 
            }else {
            	JOptionPane.showMessageDialog(null,"Numero incorret \n"
            									  +"Compte introuvable !!!");
            }
        } catch (Exception e) {
			System.out.println("ERREUR SELECT * : "+ e.getMessage());

        }
    }
	
	public static void getAllComptes() {
		tableModel.setRowCount(0);
    	sql = "SELECT * FROM comptes";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        	if (rs != null) {
        		while (rs.next()) {
                	tableModel.addRow(new Object[]{
                    		rs.getInt("id"),
                    		rs.getString("name"), 
                    		rs.getString("password") ,
            				rs.getInt("num"),
            				rs.getDouble("solde")});
                }		
        	}
        } catch (Exception e) {
			System.out.println("ERREUR SELECT NUM : "+ e.getMessage());
        }
     }
	
	public static void getAllHistorys() {
        // Création de la fenêtre
        JFrame h = new JFrame();
        h.setTitle("Historique des transactions");
        h.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        h.setSize(750, 300);
        h.setLocationRelativeTo(null);
        h.setLayout(new BorderLayout());

        // Création du panneau principal
        JPanel Ph = new JPanel(new BorderLayout());

        // En-têtes de la table
        String[] sorti = {"ID history", "Numéro compte", "Type opération", "Montant", "Date et heure"};
        tableModel1 = new DefaultTableModel(sorti, 0);

        // Création de la JTable avec le modèle
        JTable tableh = new JTable(tableModel1);
        tableh.setShowGrid(true); // Afficher les bordures des cellules
        tableh.setGridColor(Color.BLACK); // Définir la couleur des bordures

        // Ajout de la JTable à un JScrollPane
        JScrollPane scrollPaneh = new JScrollPane(tableh);
        scrollPaneh.setPreferredSize(new Dimension(550, 300)); // Taille préférée du JScrollPane

        // Ajout du JScrollPane au panneau principal
        Ph.add(scrollPaneh, BorderLayout.CENTER);

        // Ajout du panneau principal à la fenêtre
        h.add(Ph, BorderLayout.CENTER);
        // Demander le numéro de compte à l'utilisateur
        String input = JOptionPane.showInputDialog(h, "Donnez le numéro du compte à rechercher :");
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(h, "Veuillez entrer un numéro de compte valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int num;
        try {
            num = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(h, "Le numéro de compte doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Requête SQL pour récupérer l'historique des transactions
        String sql = "SELECT * FROM historique WHERE num_compte = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, num);
            ResultSet rs = stmt.executeQuery();

            // Vider la table avant d'ajouter de nouvelles données
            tableModel1.setRowCount(0);

            // Ajouter les données à la table
            while (rs.next()) {
                String dateOperation = History.getDate(rs.getTimestamp("date_operation").toLocalDateTime());
                tableModel1.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("num_compte"),
                    rs.getString("type_operation"),
                    rs.getDouble("montant"),
                    dateOperation
                });
            }
            // Afficher un message si aucun résultat n'est trouvé
            if (tableModel1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(h, "Aucune transaction trouvée pour le compte " + num, "Information", JOptionPane.INFORMATION_MESSAGE);
            }else {
            	 h.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(h, "Erreur lors de la récupération de l'historique : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Affichage de la fenêtre
       //h.setVisible(true);
    }
	
	 public static void getAllHistory () {
		 JFrame h = new JFrame();
		 h.setTitle("Historique transactions");
	     h.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	     h.setSize(500, 300);
	     h.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	     h.setLocationRelativeTo(null);
	     JPanel Ph = new JPanel();
	     Ph.setLayout(new FlowLayout());
	     String[] sorti = {"ID history", "Numero compte", "Type operation", "Nature","Date et heur"};
	     tableModel1 = new DefaultTableModel(sorti, 0);
	     JTable tableh = new JTable(tableModel1);
	     JScrollPane scrollPaneh = new JScrollPane(tableh);
	  	  
         int rowHeight = tableh.getRowHeight(); 
         int visibleRowCount = 5; 
         scrollPaneh.setPreferredSize(new Dimension(scrollPaneh.getPreferredSize().width, rowHeight * visibleRowCount));
         Ph.add(scrollPaneh);
         h.add(Ph);
	    	num  = Integer.parseInt(JOptionPane.showInputDialog("Donner le numero du compte a rechercher : ")); 
	   	    sql = "SELECT * FROM historique WHERE num_compte = ?";
	   	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	   	        stmt.setInt(1, num);
	   	        ResultSet rs = stmt.executeQuery();
	      	        if (rs != null) {
	      	        	while (rs.next()) {
	      	        		String dateOperation = History.getDate(rs.getTimestamp("date_operation").toLocalDateTime());
	                    	tableModel1.addRow(new Object[]{
	                    			rs.getInt("id") , 
	                    			rs.getInt("num_compte") ,
	                    			rs.getString("type_operation") ,
 	        		 	            rs.getDouble("montant") , 
 	        		 	            dateOperation});
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
	        	num = Integer.parseInt(JOptionPane.showInputDialog("Donner le numero du compte : "));
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
	 
	 public static void transfere() {
		 double newSolde , montant;
	    	String pass;
	    	try {
	    		num = Integer.parseInt(JOptionPane.showInputDialog("Donner le votre numero de compte : "));
	        	int numb = Integer.parseInt(JOptionPane.showInputDialog("Donner le numero du beneficier : "));
	    		montant = Double.parseDouble(JOptionPane.showInputDialog("Donner le montant a Transferer : "));
	        	ResultSet rs = getCompte(num);
	        	update = "UPDATE comptes SET solde = ? WHERE num = ?";
	        	PreparedStatement stmt = conn.prepareStatement(update);
	        	if (rs.next()) {
	        		pass = JOptionPane.showInputDialog("Entrer votre mot de passe");

        			if(montant <= rs.getDouble("solde") && pass.equals(rs.getString("password"))) {

    					newSolde = rs.getDouble("solde") - montant;
	        			stmt.setDouble(1, newSolde);
	        			stmt.setInt(2,num);
	                    stmt.executeUpdate();
	                    
	                    System.out.println("OK R");
	                    ResultSet rsb = getCompte(numb);
	                    if (rsb.next()) {
	                    	Double newSoldeb = montant + rsb.getDouble("solde") ;
	                    	updateSolde(numb, newSoldeb);
	                    	History.addHistory(num, "Transfert", -montant);
	                    	JOptionPane.showMessageDialog(null,
	                                "Transaction enregistrée !\n" +
	                                "Détails de l'opération :\n" +
	                                "Nature : Transfert\n" +
	                                "Montant : " + montant + " F CFA\n" +
	                                "Numéro bénéficiaire : " + numb + "\n" +
	                                "Nouveau solde : " + newSolde + " F CFA"
	                            );
	                    }
	                   
	        		}else {
	        			JOptionPane.showMessageDialog(null,"Solde issufissant ou mot de passe incorrect");
	        		}
        		
	        	}else {
	    			JOptionPane.showMessageDialog(null,"Numero inccoret , compte introuvable !!! \nEchec Transfere");
	        	}
	        } catch (Exception e) {
				System.out.println("ERREUR UPDATE SOLDE - : "+ e.getMessage());
	        }
	    	
	 }
	 
	// Méthode pour mettre à jour le solde d'un compte
	    private static void updateSolde(int num, double newSolde) throws SQLException {
	        String update = "UPDATE comptes SET solde = ? WHERE num = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(update)) {
	            stmt.setDouble(1, newSolde);
	            stmt.setInt(2, num);
	            stmt.executeUpdate();
	        }
	    }

	    // Méthode pour récupérer les informations d'un compte
	    private static ResultSet getCompte(int num) throws SQLException {
	        String sql = "SELECT * FROM comptes WHERE num = ?";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, num);
	        return stmt.executeQuery();
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
	            	JOptionPane.showMessageDialog(null,"ID incorect , historique introuvable !!! \\n\\t\\tEchec suppression");
	        	} 
	        } catch (Exception e) {
				System.out.println("ERREUR DELETE : "+ e.getMessage());
	        }
	    }
	 
	public static void main(String[] args) throws Exception{
		UIManager.setLookAndFeel( new NimbusLookAndFeel() );
		 // Création de la fenêtre
        JFrame frame = new JFrame();
        frame.setTitle("Gestion de compte bancaire");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        frame.setLocationRelativeTo(null);

        // Création du panneau principal
        JPanel Pane = new JPanel();
        Pane.setLayout(new BorderLayout());
        // Création de la barre de menus
        JMenuBar menuBar = new JMenuBar();
        
        // Création du menu "Comptes"
        JMenu edit = new JMenu("Edit");
        JMenuItem addCompteItem = new JMenuItem("Créer des comptes");  
        edit.add(addCompteItem);

        // Création du menu "Transactions"
        JMenu remove = new JMenu("Supprimer");
        JMenuItem deleteCompteItem = new JMenuItem("Supprimer un compte");     
        JMenuItem deleteHistoryItem = new JMenuItem("Supprimer une historique");  
        remove.add(deleteCompteItem);
        remove.addSeparator();
        remove.add(deleteHistoryItem);
        
        JMenu trans = new JMenu("Transactions");
        JMenuItem transItem = new JMenuItem("Effectuer un transfere");
        JMenuItem depotItem = new JMenuItem("Effectuer un dépôt");
        JMenuItem retraitItem = new JMenuItem("Effectuer un retrait");
        trans.add(transItem);
        trans.addSeparator();
        trans.add(depotItem);
        trans.addSeparator();
        trans.add(retraitItem);
        
        JMenu show = new JMenu("Afficher");
        JMenuItem getCompteItem = new JMenuItem("Afficher les comptes");
        JMenuItem showHistoryItem = new JMenuItem("Afficher l'historique des transactions");
        show.add(getCompteItem);
        show.addSeparator();
        show.add(showHistoryItem);
        
        // Création du menu "Quitter"
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.setPreferredSize(new Dimension(1,1));
        // Ajout des menus à la barre de menus
        menuBar.add(edit);
        menuBar.add(show);
        menuBar.add(trans);
        menuBar.add(remove);
        menuBar.add(exitItem);

        // Ajout de la barre de menus à la fenêtre
        frame.setJMenuBar(menuBar);
       
        
        // Création du panneau de droite
        JPanel Prigth = new JPanel();
        Prigth.setLayout(new BorderLayout(5, 5));

        // Panneau supérieur pour la recherche
        JPanel PrigthTop = new JPanel();
        JTextField search = new JTextField(65);
        JButton btnSearch = new JButton("Search");
        JButton btn = new JButton("Actualiser");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                getAllComptes();
            }
        });
        PrigthTop.add(search);
        PrigthTop.add(btnSearch);
        PrigthTop.add(btn);

        // Ajout du panneau supérieur au panneau de droite
        Prigth.add(PrigthTop, BorderLayout.NORTH);

        // Panneau inférieur pour la table
        JPanel PrigthBottom = new JPanel();
        PrigthBottom.setLayout(new BorderLayout());

        // Création de la JTable avec un modèle vide pour les compte
        String[] sortie = {"ID", "Nom", "Mot de passe", "Numéro Compte", "Solde"};
        tableModel = new DefaultTableModel(sortie, 0);
        JTable table = new JTable(tableModel);
        table.setShowGrid(true); 
        table.setGridColor(Color.BLACK);

        // Ajout de la JTable à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        int rowHeight = table.getRowHeight();
        int visibleRowCount = 17; 
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, rowHeight * visibleRowCount));

        // Ajout du JScrollPane au panneau inférieur
        PrigthBottom.add(scrollPane);

        // Ajout du panneau inférieur au panneau de droite
        Prigth.add(PrigthBottom);

        // Ajout des panneaux gauche et droit au panneau principal
        Pane.add(Prigth); 

        // Ajout du panneau principal à la fenêtre
        frame.add(Pane);

        // Affichage de la fenêtre
        frame.setVisible(true);

       addCompteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addCompte();
            }
        });
        
     // Action du bouton "Search"
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                getCompte(search);
            }
        });
         
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                frame.dispose();
            }
        });
        
        getCompteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	getAllComptes();
            }
        });
        
        deleteCompteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	removeCompte ();    
            	getAllComptes();
            	}
        });

        depotItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	updateDepot ();            }
        });

        retraitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	updateRetrait ();            }
        });
        
        transItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	transfere();            }
        });
        
        showHistoryItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	getAllHistorys ();
            }
        });

        deleteHistoryItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	removeHistory ();            }
        });
        
	}
}

