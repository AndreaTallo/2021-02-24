package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private Map<Integer,Player> idMap;
	private	double pesoMigliore=0.0;
	private Simulator simulator;
	private List<Action> azioni;

	Player best = null;
	
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao=new PremierLeagueDAO();
		idMap=new HashMap<Integer,Player>();
		this.dao.listAllPlayers(idMap);
		simulator=new Simulator(this);
		
		
	}
	
	public List<Match> getListaMatch(){
		return dao.listAllMatches();
		
	}
	
	public void creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(m,idMap));
		
		for(Adiacenza a : dao.getAdiacenze(m, idMap)) {
			if(a.getPeso() >= 0) {
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), 
							a.getP2(), a.getPeso());
				}
			} else {
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), 
							a.getP1(), (-1) * a.getPeso());
				}
			}
			
			azioni=dao.getActions(m);
		}
		
		
		
		
	}
	public Player calcolaGiocatoreMigliore() {
	
		
		
		for(Player pp:this.grafo.vertexSet()) {
			double peso=0.0;
			
			
			
			for(DefaultWeightedEdge dd:this.grafo.outgoingEdgesOf(pp) ) 
				peso=peso+this.grafo.getEdgeWeight(dd);
			
			for(DefaultWeightedEdge d1:this.grafo.incomingEdgesOf(pp) ) 
				peso=peso-this.grafo.getEdgeWeight(d1);
			
			if(peso>pesoMigliore) {
				pesoMigliore=peso;
				best=pp;
			}
				
			}
		
		
		
		
		return best;
	}
	
	public double getPesoMigliore() {
		return this.pesoMigliore;
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public void simula(int i) {
		simulator.init(i,azioni);
		simulator.run();
		
	}
	
	

     
     public int getNumeroEspulsiCasa() {
    	 return simulator.getEspulsiCasa();
    	 
     }
     public int getNumeroEspulsiTrasferta() {
    	 return simulator.getEspulsiTrasferta();
    	 
     }
     public int getNumeroGolCasa() {
    	 return simulator.getNumGolSquadraCasa();
    	 
     }
     public int getNumeroGolTrasferta() {
    	 return simulator.getNumGolSquadraTrasferta();
    	 
     }
     
	
}
