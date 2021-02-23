package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMapVertexPlayer;
	
	private List<Player> dreamTeam;
	private Double gradoTitolaritaMigliore;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public String creaGrafo(Double mediaGolPartita) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMapVertexPlayer = new HashMap<Integer, Player>();
		this.dao.getVertici(mediaGolPartita, this.idMapVertexPlayer);
		// GRAFO - Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, idMapVertexPlayer.values());
		// GRAFO - Aggiungo gli archi
		for( Adiacenza a : this.dao.getAdiacenze(idMapVertexPlayer) ) {
			if( a.getPeso() > 0 ) {
				Graphs.addEdge(this.grafo, a.getPlayer1(), a.getPlayer2(), a.getPeso());
			} else if( a.getPeso() < 0 ) {
				Graphs.addEdgeWithVertices(this.grafo, a.getPlayer2(), a.getPlayer1(), -a.getPeso());
			}
		}
		String message = String.format("Grafo creato\n"+"#VERTICI: %d\n"+"#ARCHI: %d\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
		return message;
	}
	
	public List<PlayerPeso> topPlayerBattuti() {
		Integer outDegreeMassimo = 0;
		Player topPlayer = new Player(null, null);
		
		for( Player p : this.grafo.vertexSet() ) {
			if(this.grafo.outDegreeOf(p) > outDegreeMassimo) {
				outDegreeMassimo = this.grafo.outDegreeOf(p);
				topPlayer = new Player(p.getPlayerID(), p.getName());
			}
		}
		
		List<PlayerPeso> topPlayerBattuti = new ArrayList<>();
		
		for( DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(topPlayer) ) {
			PlayerPeso playerBattuto = new PlayerPeso( this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e) );
			topPlayerBattuti.add(playerBattuto);
		}
		
		Collections.sort(topPlayerBattuti);
		return topPlayerBattuti;
	}
	
	public Player topPlayer() {
		Integer outDegreeMassimo = 0;
		Player topPlayer = new Player(null, null);
		
		for( Player p : this.grafo.vertexSet() ) {
			if(this.grafo.outDegreeOf(p) > outDegreeMassimo) {
				outDegreeMassimo = this.grafo.outDegreeOf(p);
				topPlayer = new Player(p.getPlayerID(), p.getName());
			}
		}
		
		return topPlayer;
	}
	
	public List<Player> dreamTeam(Integer K) {
		this.gradoTitolaritaMigliore = 0.0;
		this.dreamTeam = new ArrayList<Player>();
		
		List<Player> parziale = new ArrayList<Player>();
		List<Player> giocatoriDisponibili = new ArrayList<Player>(this.grafo.vertexSet());
		
		this.ricorsiva(parziale, giocatoriDisponibili, K);
		
		return this.dreamTeam;
	}
	
	private void ricorsiva(List<Player> parziale, List<Player> giocatoriDisponibili, Integer K) {
		// CONDIZIONE DI TERMINAZIONE
		if( parziale.size() == K ) {
			Double gradoTitolaritaAttuale = this.calcolaGradoTitolarita(parziale);
			if( gradoTitolaritaAttuale > this.gradoTitolaritaMigliore ) {
				System.out.println("Grado attuale : "+gradoTitolaritaAttuale);
				System.out.println("Grado migliore : "+gradoTitolaritaMigliore);
				this.gradoTitolaritaMigliore = gradoTitolaritaAttuale;
				this.dreamTeam = new ArrayList<>(parziale);
			}
			return ;
		}
		
		for( Player p : giocatoriDisponibili ) {
			if( !parziale.contains(p) ) {
				// RISOLUZIONE DEL PROBLEMA AL LIVELLO N
				parziale.add(p);
				// AGGIORNO LE STRUTTURE DATI
				List<Player> giocatoriRimasti = new ArrayList<Player>(giocatoriDisponibili);
				giocatoriRimasti.removeAll(Graphs.successorListOf(this.grafo, p));
				// CHIAMATA RICORSIVA AL LIVELLO N + 1
				ricorsiva(parziale, giocatoriRimasti, K);
				// BACKTRACKING
				parziale.remove(p);
			}
		}
		
	}

	private Double calcolaGradoTitolarita(List<Player> parziale) {
		Double gradoTitolarita = 0.0;
		
		// Itero per la dimensione della lista
		for( Player p : parziale ) {
			
			Double gradoArchiEntranti = 0.0;
			List<DefaultWeightedEdge> archiEntranti = new ArrayList<DefaultWeightedEdge>(this.grafo.incomingEdgesOf(p));
			
			for( DefaultWeightedEdge dwe : archiEntranti ) {
				gradoArchiEntranti = gradoArchiEntranti + this.grafo.getEdgeWeight(dwe);
			}
			
			Double gradoArchiUscenti = 0.0;
			List<DefaultWeightedEdge> archiUscenti = new ArrayList<DefaultWeightedEdge>(this.grafo.outgoingEdgesOf(p));
			
			for( DefaultWeightedEdge dwe : archiUscenti ) {
				gradoArchiUscenti = gradoArchiUscenti + this.grafo.getEdgeWeight(dwe);
			}
			
			gradoTitolarita = gradoTitolarita + (gradoArchiUscenti - gradoArchiEntranti);
		}
		return gradoTitolarita;
	}
	
	public Double getGradoTitolarita() {
		System.out.println("Grado migliore ASSOLUTO : "+this.gradoTitolaritaMigliore);
		return this.gradoTitolaritaMigliore;
	}
	
	public List<Player> getDreamTeam() {
		return this.dreamTeam;
	}
	
}