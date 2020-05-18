package sample.controller;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.Contact;
import sample.model.Group;
import sample.model.Model;

import java.io.File;

public class Controller {
    @FXML
    private TreeView<Object> contactsTreeV;
    @FXML
    private Button ajouterBtn, retirerBtn;
    @FXML
    private Pane contactPane;
    @FXML
    private MenuItem loadMi, saveMi;

    private TreeItem<Object> racineTreeI;
    private TreeItem<Object> selectedItem;

    private Model model;
    private ContactControl contactControl;
    private Contact editingContact;
    private Stage stage;

    public Controller() {
        model = new Model();
        contactControl = new ContactControl();
        editingContact = contactControl.getContactController().getContact();
        racineTreeI = new TreeItem<Object>();
    }

    public void initialize() {
        contactPane.getChildren().add(contactControl.getRoot());
        //On ne peut pas créer de valeur au départ sans avoir sélectionné un groupe.
        contactPane.visibleProperty().set(false);

        contactsTreeV.setEditable(true);
        contactsTreeV.setCellFactory(param -> new TextFieldTreeCellImpl());

        racineTreeI.setValue("Fiche de contacts");
        racineTreeI.setExpanded(true);
        contactsTreeV.setRoot(racineTreeI);

        ajouterBtn.setOnAction(e -> {
            model.newGroup();
        });

        model.getGroups().addListener((ListChangeListener<? super Group>) changeGroup -> {
            changeGroup.next();
            if (changeGroup.wasAdded()) {
                for (Group group : changeGroup.getAddedSubList()) {
                    Node groupIcon = new ImageView(new Image("file:lib/group.png"));
                    TreeItem<Object> grpItem = new TreeItem<Object>(group, groupIcon);
                    grpItem.setExpanded(true);

                    contactsTreeV.getRoot().getChildren().add(grpItem);

                    //Ajout du listener
                    group.getContacts().addListener((ListChangeListener<? super Contact>) changeContact -> {
                        changeContact.next();
                        if (changeContact.wasAdded()) {
                            for (Contact contact : changeContact.getAddedSubList()) {
                                Node contactIcon = new ImageView(new Image("file:lib/contact.png"));
                                TreeItem<Object> contactItem = new TreeItem<Object>(contact, contactIcon);
                                grpItem.getChildren().add(contactItem);
                                contactsTreeV.getSelectionModel().select(contactItem);
                            }
                        } else if (changeContact.wasRemoved() && selectedItem != null && selectedItem != racineTreeI) {
                            for (Contact contact : changeContact.getRemoved()) {
                                selectedItem.getParent().getChildren().remove(selectedItem);
                            }
                        }
                    });
                }
            } else if (changeGroup.wasRemoved()) {
                for (Group group : changeGroup.getRemoved()) {
                    System.out.println(group);
                    System.out.println(contactsTreeV.getRoot().getChildren());
                    contactsTreeV.getRoot().getChildren().remove(selectedItem);
                    //contactsTreeV.refresh();
                }
            }
            
        });

        retirerBtn.setOnAction(e -> {
            if (selectedItem.getValue() instanceof Group) {
                Group group = (Group) selectedItem.getValue();
                model.removeGroup(group);
            } else if (selectedItem.getValue() instanceof Contact) {
                Contact contact = (Contact) selectedItem.getValue();
                ((Group) selectedItem.getParent().getValue()).removeContact(contact);
            }

        });

        contactsTreeV.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null)
                return;
            selectedItem = (TreeItem<Object>) newValue;

            if (newValue.getValue() instanceof String) {
                //On vient de sélectionner la racine
                contactPane.visibleProperty().set(false);
            } else if (newValue.getValue() instanceof Group) {
                //Selection d'un groupe donc on peut créer un contact
                contactPane.visibleProperty().set(true);
                editingContact.reset();
                contactControl.getContactController().resetDisplayCbRb(); //Pas de double binding avec le combobox ou le radio bouton
            } else if (newValue.getValue() instanceof Contact) {
                //Selection d'un contact
                contactPane.visibleProperty().set(true);
                editingContact.loadContact((Contact) newValue.getValue());
            }


        });

        editingContact.valideProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (selectedItem.getValue() instanceof Group) {
                    //Contact valide donc on peut le créer.
                    Group group = (Group) selectedItem.getValue();
                    Contact contact = new Contact();
                    contact.loadContact(editingContact);
                    group.getContacts().add(contact);
                } else if (selectedItem.getValue() instanceof Contact) {
                    Contact contact = (Contact) selectedItem.getValue();
                    contact.loadContact(editingContact);
                    contactsTreeV.refresh();
                }

            }
            editingContact.setValide(false);

        });

        loadMi.setOnAction(e -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open JSON File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            fileChooser.setInitialDirectory(new File("lib"));

            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                model.load(file);
            }

        });

        saveMi.setOnAction(e -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save contacts");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            fileChooser.setInitialDirectory(new File("lib"));

            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                model.save(file);
            }
        });

    }


    private final class TextFieldTreeCellImpl extends TreeCell<Object> {

        private TextField textField;

        public TextFieldTreeCellImpl() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (getItem() instanceof Group) {

                if (textField == null) {
                    createTextField();
                }
                setText(null);
                setGraphic(textField);
                textField.selectAll();

            }

        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getString());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        Group group = (Group) getItem();
                        group.setName(textField.getText());
                        commitEdit(group);
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            if (getItem() instanceof Group) {
                return getItem() == null ? "" : ((Group) getItem()).getName();
            }
            if (getItem() instanceof Contact) {
                return getItem() == null ? "" : ((Contact) getItem()).getPrenom() + " " + ((Contact) getItem()).getNom();
            }
            return getItem() == null ? "" : getItem().toString();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}