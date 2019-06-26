

/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
/**
 * Sample Skeleton for 'FlightDelays.fxml' Controller Class
 */



public class FlightDelaysController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoArrivo"
    private ComboBox<Airport> cmbBoxAeroportoArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	String distMedia = distanzaMinima.getText();
    	if(distMedia!=null && !distMedia.isEmpty()) {
    		if(model.isDigit(distMedia)) {
    			txtResult.setText(model.creaGrafo(Integer.parseInt(distMedia)));
    			cmbBoxAeroportoPartenza.getItems().addAll(model.getVertici());
    			cmbBoxAeroportoArrivo.getItems().addAll(model.getVertici());
    		}else {
    			showMessage("Errore: Inserisci una distanza minima valida");
    		}
    	}else {
    		showMessage("Errore: Inserisci una distanza minima");
    	}

    }

    @FXML
    void doTestConnessione(ActionEvent event) {
    	Airport partenza = cmbBoxAeroportoPartenza.getValue();
    	Airport arrivo = cmbBoxAeroportoArrivo.getValue();
    	if(partenza!=null && arrivo!=null) {
    		if(!partenza.equals(arrivo)) {
    			txtResult.setText(model.trovaPercorso(partenza, arrivo));
    		}else {
    			showMessage("Errore: Selezionare 2 aeroporti differenti");
    		}
    	}else {
    		showMessage("Errore: Selezionare un aeroporto di partenza e uno di arrivo dai rispettivi menù a tendina");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxAeroportoArrivo != null : "fx:id=\"cmbBoxAeroportoArrivo\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'FlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;
	}
    
    private void showMessage(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message);
		alert.show();
	}
}




