package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;

public class Model {
    private ObservableList<Group> groups;

    public Model(){
        groups = FXCollections.observableArrayList();
        //groups = FXCollections.emptyObservableList();
    }

    public void save(File file){
        JSONWorkspace jsonWorkspace = new JSONWorkspace();
        try {
            jsonWorkspace.loadGroups(groups);
            jsonWorkspace.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(File file){
        try {
            groups.clear();
            groups.addAll(JSONWorkspace.fromFile(file));
            //We put again the contacts to active the listeners that create the treeitems.
            for (Group group : groups){
                ObservableList<Contact> contacts = FXCollections.observableArrayList();
                contacts.addAll(group.getContacts());
                group.getContacts().clear();
                group.getContacts().setAll(contacts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeGroup(Group group){
        groups.remove(group);
    }

    public ObservableList<Group> getGroups() {
        return groups;
    }

    public void newGroup(){
        groups.add(new Group());
    }
}
