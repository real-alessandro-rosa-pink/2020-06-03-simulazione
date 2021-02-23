package it.polito.tdp.PremierLeague.model;

public class Adiacenza {
	
	private Player player1;
	private Player player2;
	private Integer peso;
	
	public Adiacenza(Player player1, Player player2, Integer peso) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.peso = peso;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
}