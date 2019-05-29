package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> idMap;
	private Map<Airport, Airport> visita;

	public Model() {
		super();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<Integer, Airport>();
		visita = new HashMap<>();
	}



	public boolean isDigit(String distanza) {
		if(distanza.matches("\\d+")) {
			return true;
		}
		return false;
	}
	
	public void creaGrafo(int distanzaMedia) {
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		dao.loadAllAirports(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Rotta rotta:dao.getRotte(idMap, distanzaMedia)) {
			DefaultWeightedEdge edge = grafo.getEdge(rotta.getIdP(), rotta.getIdA());
			if(edge==null) {
				Graphs.addEdge(grafo, rotta.getIdP(), rotta.getIdA(), rotta.getMedia());
			}
			else {
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso+rotta.getMedia())/2;
				grafo.setEdgeWeight(edge, newPeso);
			}
		}
	}
	
	public boolean testConnessione(Integer id1, Integer id2) {
		Set<Airport> visitati = new HashSet<Airport>();
		Airport partenza = idMap.get(id1);
		Airport arrivo = idMap.get(id2);
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(grafo, partenza);
		while(it.hasNext()) {
			visitati.add(it.next());
		}
		if(visitati.contains(arrivo)) {
			return true;
		}else {
			return false;
		}
	}
	
	public String trovaPercorso(Integer id1, Integer id2){
		String risultato="";
		List<Airport> percorso = new ArrayList<Airport>();
		Airport partenza = idMap.get(id1);
		Airport arrivo = idMap.get(id2);
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(grafo, partenza);
		visita.put(partenza, null);
		it.addTraversalListener(new TraversalListener<Airport, DefaultWeightedEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {
				Airport sorgente = grafo.getEdgeSource(ev.getEdge());
				Airport destinazione = grafo.getEdgeTarget(ev.getEdge());
				if(!visita.containsKey(destinazione) && visita.containsKey(sorgente)) {
					visita.put(destinazione, sorgente);
				}else if(visita.containsKey(destinazione) && !visita.containsKey(sorgente)){
					visita.put(sorgente, destinazione);
				}
			}
			
			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		while (it.hasNext()) {
			it.next();
		}
		if(!visita.containsKey(partenza) || !visita.containsKey(arrivo)) {
			return null;
		}
		Airport step = arrivo;
		while(!step.equals(partenza)) {
			percorso.add(step);
			step= visita.get(step);
		}
		percorso.add(step);
		for(Airport a: percorso) {
			risultato+=a.getAirportName()+"\n";
		}
		return risultato;
	}

	public List<Airport> getAllAirports() {
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		return dao.loadAllAirports(idMap);
	}
	
	public String getVertici() {
		String risultato="";
		risultato="Grafo creato. Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size()+"\n";
		for(Airport a: grafo.vertexSet()) {
			risultato+=a.getAirportName()+"\n";
		}
		return risultato;
	}

}
