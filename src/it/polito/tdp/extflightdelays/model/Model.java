package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
	
	private Map<Integer, Airport> idMap;
	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private Map<Airport, Airport> visitati;
	
	public Model() {
		idMap = new HashMap<>();
		visitati = new HashMap<>();
		}

	public boolean isDigit(String distMedia) {
		if(distMedia.matches("\\d+")) {
			return true;
		}
		return false;
	}

	public String creaGrafo(int distMedia) {
		String risultato="";
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao.loadAllAirports(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		List<Rotta> rotte = dao.getRotte(idMap, distMedia);
		for(Rotta r: rotte) {
			DefaultWeightedEdge edge = grafo.getEdge(r.getA1(), r.getA2());
			if(edge==null) {
				Graphs.addEdgeWithVertices(grafo, r.getA1(), r.getA2(), r.getPeso());
			}else {
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso + r.getPeso())/2;
				grafo.setEdgeWeight(edge, newPeso);
			}
		}
		risultato="Grafo Creato! Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size();
		return risultato;
	}

	public List<Airport> getVertici() {
		List<Airport> vertici = new LinkedList<>(grafo.vertexSet());
		Collections.sort(vertici);
		return vertici;
	}

	public String trovaPercorso(Airport partenza, Airport arrivo) {
		String risultato="";
		BreadthFirstIterator<Airport, DefaultWeightedEdge> iterator = new BreadthFirstIterator<>(grafo, partenza);
		List<Airport> percorso = new ArrayList<Airport>();
		visitati.put(partenza, null);
		iterator.addTraversalListener(new TraversalListener<Airport, DefaultWeightedEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> edge) {
				Airport source = grafo.getEdgeSource(edge.getEdge());
				Airport target = grafo.getEdgeTarget(edge.getEdge());
				if(!visitati.containsKey(target) && visitati.containsKey(source)) {
					visitati.put(target, source);
				}else if (!visitati.containsKey(source) && visitati.containsKey(target)){
					visitati.put(source, target);
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
		while (iterator.hasNext()) {
			iterator.next();
		}
		if(!visitati.containsKey(partenza) || !visitati.containsKey(arrivo)){
			return null;
		}
		Airport step = arrivo;
		while(!step.equals(partenza)) {
			percorso.add(step);
			step=visitati.get(step);
		}
		percorso.add(step);
		for(Airport p: percorso) {
			risultato+=p.getAirportName()+"\n";
		}
		System.out.println(percorso.size());
		return risultato;
	}

	public boolean getConnessione(Airport partenza, Airport arrivo) {
		Set<Airport> connessioni = new HashSet<>();
		BreadthFirstIterator<Airport, DefaultWeightedEdge> iterator = new BreadthFirstIterator<>(grafo, partenza);
		
		while(iterator.hasNext()) {
			connessioni.add(iterator.next());
		}
		if(connessioni.contains(arrivo)) {
			return true;
		}else {
			return false;
		}
	}
}
