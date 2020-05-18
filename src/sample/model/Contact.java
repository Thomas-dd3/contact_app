package sample.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;

public class Contact {
    private SimpleStringProperty prenom, nom, ville, pays, sexe;
    private SimpleBooleanProperty valide;

    private ObservableMap<String, String> observableControlMap;

    public Contact() {
        prenom = new SimpleStringProperty(this, "prenom", "");
        nom = new SimpleStringProperty(this, "nom", "");
        ville = new SimpleStringProperty(this, "ville", "");
        pays = new SimpleStringProperty(this, "pays", "");
        sexe = new SimpleStringProperty(this, "sexe", "");
        valide = new SimpleBooleanProperty(this, "valide", false);

        observableControlMap = FXCollections.observableHashMap();
    }

    public void launchVerification(){
        observableControlMap.put(prenom.getName(), "Le prénom doit être renseigné.");
        observableControlMap.put(nom.getName(), "Le nom doit être renseigné.");
        observableControlMap.put(ville.getName(), "La ville doit être renseignée.");
        observableControlMap.put(pays.getName(), "Le pays doit être renseigné.");
        observableControlMap.put("sexeHomme", "Le sexe doit être renseigné.");
        observableControlMap.put("sexeFemme", "Le sexe doit être renseigné.");


        Boolean valideTemp = true;
        if (prenom.getValue().isBlank()){
            valideTemp = false;
        }
        else observableControlMap.remove(prenom.getName(), "Le prénom doit être renseigné.");
        if (nom.getValue().isBlank()){
            valideTemp = false;
        }
        else observableControlMap.remove(nom.getName(), "Le nom doit être renseigné.");
        if (ville.getValue().isBlank()){
            valideTemp = false;
        }
        else observableControlMap.remove(ville.getName(), "La ville doit être renseignée.");
        if (pays.getValue().isBlank()){
            valideTemp = false;
        }
        else observableControlMap.remove(pays.getName(), "Le pays doit être renseigné.");
        if (sexe.getValue().isBlank()){
            valideTemp = false;
        }
        else{
            observableControlMap.remove("sexeHomme", "Le sexe doit être renseigné.");
            observableControlMap.remove("sexeFemme", "Le sexe doit être renseigné.");
        }
        if (valideTemp){
            valide.setValue(true);
        }
    }

    public void loadContact(Contact contact){
        prenom.setValue(contact.prenom.getValue());
        nom.setValue(contact.nom.getValue());
        ville.setValue(contact.ville.getValue());
        pays.setValue(contact.pays.getValue());
        sexe.setValue(contact.sexe.getValue());
        valide.setValue(false);
        observableControlMap.clear();
        observableControlMap.putAll(contact.observableControlMap);
    }

    public void reset(){
        prenom.setValue("");
        nom.setValue("");
        ville.setValue("");
        pays.setValue("");
        sexe.setValue("");
        valide.setValue(false);
        observableMapReset();
        observableControlMap.clear();
    }

    public void observableMapReset(){
        observableControlMap.put(prenom.getName(), "clear");
        observableControlMap.put(nom.getName(), "clear");
        observableControlMap.put(ville.getName(), "clear");
        observableControlMap.put(pays.getName(), "clear");
        observableControlMap.put("sexeHomme", "clear");
        observableControlMap.put("sexeFemme", "clear");
    }

    public String getPrenom() {
        return prenom.get();
    }

    public SimpleStringProperty prenomProperty() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public String getNom() {
        return nom.get();
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getVille() {
        return ville.get();
    }

    public SimpleStringProperty villeProperty() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville.set(ville);
    }

    public String getPays() {
        return pays.get();
    }

    public SimpleStringProperty paysProperty() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays.set(pays);
    }

    public String getSexe() {
        return sexe.get();
    }

    public SimpleStringProperty sexeProperty() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe.set(sexe);
    }

    public boolean isValide() {
        return valide.get();
    }

    public SimpleBooleanProperty valideProperty() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide.set(valide);
    }

    public ObservableMap<String, String> getObservableControlMap() {
        return observableControlMap;
    }

    /*public void setObservableControlMap(ObservableMap<String, String> observableControlMap) {
        this.observableControlMap = observableControlMap;
    }*/
    //Correction du setter pour la desérialization avec Jackson
    public void setObservableControlMap(HashMap<String, String> observableControlMap) {
        this.observableControlMap.putAll(observableControlMap);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "prenom=" + prenom.getValue() +
                ", nom=" + nom.getValue() +
                ", ville=" + ville.getValue() +
                ", pays=" + pays.getValue() +
                ", sexe=" + sexe.getValue() +
                ", valide=" + valide.getValue() +
                '}';
    }
}
