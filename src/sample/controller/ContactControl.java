package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ContactControl {
    private Parent root = null;
    private ContactController contactController;

    public ContactControl() {
        FXMLLoader fxmlLoader = new
                FXMLLoader(getClass().getResource("../view/contact_form.fxml"));
        try {
            root = fxmlLoader.load();
            contactController = fxmlLoader.getController();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Parent getRoot() {
        return root;
    }

    public ContactController getContactController() {
        return contactController;
    }
}
