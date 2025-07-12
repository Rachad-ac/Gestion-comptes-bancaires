package test;

public class Compte {
	private String name , password;
	private int id , num;
	private double solde;
	
	public Compte (int id ,String password ,String name , int num , double solde) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.num = num;
		this.solde = solde;
	}
	
	public int getId () {
		return id;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public String getPassword () {
		return password;
	}
	
	public void setPassword (String password) {
		this.password = password;
	}
	
	public void setNum (int num) {
		this.num = num;
	}
	
	public int getNum () {
		return num;
	}
	
	public String getName () {
		return name;
	}
	public void setName (String name) {
		this.name = name;
	}
	
	public double getSolde () {
		return solde;
	}

	public void setSolde (double solde) {
		this.solde = solde;
	}
	
	public String Afficher () { 
		return "ID COMPTE : "+id+"  |  TITULAIRE : "+ name.toUpperCase() +"  |  MOT DE PASSE : "+ password 
				+"   |  "+"NUM: "+ num +"   |  "+"SOLDE : "+ solde+" F CFA"
				+"\n---------------------------------------------------------------------------------------"
				+ "----------------------------------------------------------------------------------------\n";
	}
}
