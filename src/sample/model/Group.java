package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Contact;

import java.util.ArrayList;

public class Group {
    private String name = "Nouveau Groupe";
    private ObservableList<Contact> contacts;

    public Group(){
        contacts = FXCollections.observableArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    /*public void setContacts(ObservableList<Contact> contacts) {
        this.contacts = contacts;
    } */
    //Correction des setter pour la desérialization avec Jackson
    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts.addAll(contacts);
    }

    public void removeContact(Contact contact){
        contacts.remove(contact);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
