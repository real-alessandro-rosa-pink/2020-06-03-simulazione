/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.PlayerPeso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	Double mediaGolPartitaInteger;
    	String mediaGolPartitaString = this.txtGoals.getText();
    	try {
    		mediaGolPartitaInteger = Double.parseDouble(mediaGolPartitaString);
    	} catch(NumberFormatException nfe) {
    		txtResult.appendText("ERRORE : formato della media gol per partita non valido\n");
    		return ;
    	}
    	
    	txtResult.appendText(this.model.creaGrafo(mediaGolPartitaInteger));
    	
    	this.btnTopPlayer.setDisable(false);
    	this.btnDreamTeam.setDisable(false);
    	this.txtK.setDisable(false);
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	txtResult.appendText("DETERMINAZIONE DEL DREAM TEAM\n\n");
    	
    	String numeroGiocatoriString = this.txtK.getText();
    	Integer numeroGiocatoriInteger;
    	try {
    		numeroGiocatoriInteger = Integer.parseInt(numeroGiocatoriString);
    	} catch(NumberFormatException nfe) {
    		txtResult.appendText("ERRORE : Formato del numero di giocatori non valido\n");
    		return ;
    	}
    	
    	for( Player dt : this.model.dreamTeam(numeroGiocatoriInteger) ) {
    		txtResult.appendText(dt.toString()+"\n");
    	}
    	
    	this.txtResult.appendText("\nGrado titolarità: "+this.model.getGradoTitolarita()+"\n");
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	txtResult.appendText("TOP PLAYER: "+this.model.topPlayer().toString()+"\n\n");
    	
    	List<PlayerPeso> topPlayerBattuti = new ArrayList<>();
    	topPlayerBattuti = this.model.topPlayerBattuti();
    	
    	for( PlayerPeso pp : topPlayerBattuti ) {
    		txtResult.appendText(pp.toString()+"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.btnTopPlayer.setDisable(true);
    	this.btnDreamTeam.setDisable(true);
    	this.txtK.setDisable(true);
    }
}
