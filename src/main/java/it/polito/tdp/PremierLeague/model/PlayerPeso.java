package it.polito.tdp.PremierLeague.model;

public class PlayerPeso implements Comparable<PlayerPeso> {
	
	private Player player;
	private Double peso;
	
	public PlayerPeso(Player player, Double peso) {
		super();
		this.player = player;
		this.peso = peso;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return this.player.toString()+" | "+this.peso;
	}

	@Override
	public int compareTo(PlayerPeso other) {
		return -this.peso.compareTo(other.getPeso());
	}
	
}