package sample.controller;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.model.Country;
import sample.model.Contact;

import java.util.HashMap;
import java.util.Map;

public class ContactController {
    @FXML
    private TextField prenomTf, nomTf, villeTf;
    @FXML
    private ComboBox<String> paysCb;
    @FXML
    private RadioButton femmeRbtn, hommeRbtn;
    @FXML
    private ToggleGroup sexeGroup;
    @FXML
    private Button validerBtn;


    private Map<String, Control> controlMap;

    private Contact contact;

    public ContactController() {
        contact = new Contact();
        controlMap = new HashMap<String, Control>();
    }

    public void initialize(){
        controlMap.put("prenom", prenomTf);
        controlMap.put("nom", nomTf);
        controlMap.put("ville", villeTf);
        controlMap.put("pays", paysCb);
        controlMap.put("sexeHomme", hommeRbtn);
        controlMap.put("sexeFemme", femmeRbtn);

        paysCb.getItems().addAll(Country.getCountries());

        prenomTf.textProperty().bindBidirectional(contact.prenomProperty());
        nomTf.textProperty().bindBidirectional((contact.nomProperty()));
        villeTf.textProperty().bindBidirectional(contact.villeProperty());
        paysCb.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            contact.setPays(newVal);
        });
        contact.paysProperty().addListener((obs, oldVal, newVal) -> {
            paysCb.getSelectionModel().select(newVal);
        });


        femmeRbtn.setUserData("Femme");
        hommeRbtn.setUserData("Homme");

        sexeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                contact.setSexe(newVal.getUserData().toString());
            }
        });

        contact.sexeProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals("Homme")){
                hommeRbtn.setSelected(true);
            }
            else if (newVal.equals("Femme")){
                femmeRbtn.setSelected(true);
            }
            else {
                femmeRbtn.setSelected(false);
                hommeRbtn.setSelected(false);
            }

        });


        validerBtn.setOnAction( e -> contact.launchVerification());

        contact.getObservableControlMap().addListener((MapChangeListener<? super String, ? super String>) change -> {
            Control control = controlMap.get(change.getKey());
            if(change.wasAdded()) {
                if (change.getValueAdded().equals("clear")){
                    control.setStyle("-fx-border-color: none ;");
                }else {
                    control.setStyle("-fx-border-color: red ;");
                    control.setTooltip(new Tooltip(change.getValueAdded()));
                }
            }
            if (change.wasRemoved()) {
                if (change.getValueRemoved().equals("clear")){
                    control.setStyle("-fx-border-color: none ;");
                }else{
                    control.setStyle("-fx-border-color: blue ;");
                    Tooltip.uninstall(control, new Tooltip(change.getValueAdded()));
                }
            }

        });

    }

    public Contact getContact() {
        return contact;
    }

    public void resetDisplayCbRb() {
        //paysCb.getSelectionModel().clearSelection();
        //paysCb.setValue(null);
        //femmeRbtn.setSelected(false);
        //hommeRbtn.setSelected(false);
    }
}
