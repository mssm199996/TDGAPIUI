/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.MVC.Controllers;

import MainPackage.API.Composants.Autres.Arbre;
import MainPackage.API.Composants.Autres.Graphe;
import MainPackage.API.Composants.Autres.Graphe.TypeGraphe;
import MainPackage.API.Composants.Autres.Sommet;
import MainPackage.API.Composants.Liaison.Arc;
import MainPackage.API.Composants.Liaison.ArcPondere;
import MainPackage.API.Composants.Liaison.Arete;
import MainPackage.API.Composants.Liaison.AretePondere;
import MainPackage.API.Composants.Liaison.Relation;
import MainPackage.API.Drawers.Drawable;
import MainPackage.API.Drawers.Graphe2D;
import MainPackage.API.Drawers.Relation2D;
import MainPackage.API.Drawers.Sommet2D;
import MainPackage.API.Exceptions.NoSommet2DException;
import MainPackage.API.Exceptions.RelationMismatchException;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXColorPicker;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;


/**
 * FXML Controller class
 *
 * @author MSSM
 */
public class MainWindowController implements Initializable {

    @FXML
    private JFXColorPicker colorChooser;

    @FXML
    private TreeView<Drawable> componentsTree;

    @FXML
    private Pane workSpace;
    

    @FXML
    void addGraph(ActionEvent event) throws RelationMismatchException, NoSommet2DException {
        TextInputDialog dialogForName = new TextInputDialog("Graphe " + this.componentsTree.getRoot().getChildren().size());
                        dialogForName.setTitle("Nouveau graphe");
                        dialogForName.setHeaderText("Nom du graphe");
                        dialogForName.setContentText("Veuillez saisir le nom du graphe:");

        Optional<String> resultName = dialogForName.showAndWait();
                        
        if (resultName.isPresent() && !this.existGraph2DNode(resultName.get())){
            ChoiceDialog<String> dialogType = new ChoiceDialog<>("Non pondere et non oriente", new String[]{
                            "Non pondere et non oriente",
                            "Pondere et non oriente",
                            "Non pondere et oriente",
                            "Pondere et oriente"});
                                 dialogType.setTitle("Nouveau graphe");
                                 dialogType.setHeaderText("Nom du graphe");
                                 dialogType.setContentText("Veuillez choisir le type du graphe:");

            Optional<String> resultType = dialogType.showAndWait();
                             
            if(resultType.isPresent()){
                Graphe graphe = new Graphe(resultName.get(),new Sommet[]{});
                       graphe.setTypeGraphe(
                            resultType.get().equals("Non pondere et non oriente") ? TypeGraphe.NOT_PONDERATED_NOT_ORIENTED :
                            resultType.get().equals("Pondere et non oriente") ? TypeGraphe.PONDERATED_NOT_ORIENTED :
                            resultType.get().equals("Non pondere et oriente") ? TypeGraphe.NOT_PONDERATED_ORIENTED :
                            TypeGraphe.PONDERATED_ORIENTED
                       );
                this.createGraph2DNode(graphe);
            }
        }
        else if(resultName.isPresent()){
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Nouveau graphe");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez introduire un nom de graphe qui n'éxiste pas déja");
                  alert.showAndWait();
        }
    }

    @FXML
    void addRelation(ActionEvent event) throws RelationMismatchException, NoSommet2DException {
        TreeItem<Drawable> firstSommet = this.componentsTree.getSelectionModel().getSelectedItems().get(0);
        TreeItem<Drawable> secondSommet = this.componentsTree.getSelectionModel().getSelectedItems().get(1);
                
        if(firstSommet != null && secondSommet != null){
            if(firstSommet.getValue() instanceof Sommet2D && secondSommet.getValue() instanceof Sommet2D){
                TreeItem<Drawable> firstSommetParent = (TreeItem<Drawable>) firstSommet.getParent();
                TreeItem<Drawable> secondSommetParent = (TreeItem<Drawable>) secondSommet.getParent();
                
                if(firstSommetParent.getValue().equals(secondSommetParent.getValue())){
                    TextInputDialog dialogForName = new TextInputDialog("");
                                    dialogForName.setTitle("Nouvelle relation");
                                    dialogForName.setHeaderText("Nom du la relation");
                                    dialogForName.setContentText("Veuillez saisir le nom de la relation:");
                                    
                    Optional<String> resultName = dialogForName.showAndWait();
                    
                    if(resultName.isPresent() && !this.existRelation2DNode(resultName.get())){
                        
                        if(((Graphe2D)firstSommetParent.getValue()).getGraphe().getTypeGraphe() == TypeGraphe.PONDERATED_NOT_ORIENTED || 
                           ((Graphe2D)firstSommetParent.getValue()).getGraphe().getTypeGraphe() == TypeGraphe.PONDERATED_ORIENTED){
                            TextInputDialog dialogForPonderation = new TextInputDialog("");
                                            dialogForPonderation.setTitle("Nouvelle relation");
                                            dialogForPonderation.setHeaderText("Ponderation de la relation");
                                            dialogForPonderation.setContentText("Veuillez saisir la valeur de la ponderation de la relation:");
                                            
                            Optional<String> resultPonderation = dialogForPonderation.showAndWait();
                            
                            if(resultPonderation.isPresent()){
                                try {
                                    if(((Graphe2D)firstSommetParent.getValue()).getGraphe().getTypeGraphe() == TypeGraphe.NOT_PONDERATED_ORIENTED ||
                                            ((Graphe2D)firstSommetParent.getValue()).getGraphe().getTypeGraphe() == TypeGraphe.PONDERATED_ORIENTED){
                                        ChoiceDialog<String> dialogType = new ChoiceDialog<>("De " + ((Sommet2D)firstSommet.getValue()).getSommet().getLabel() + " à " + ((Sommet2D)secondSommet.getValue()).getSommet().getLabel(), new String[]{
                                                        "De " + ((Sommet2D)firstSommet.getValue()).getSommet().getLabel() + " à " + ((Sommet2D)secondSommet.getValue()).getSommet().getLabel(),
                                                        "De " + ((Sommet2D)secondSommet.getValue()).getSommet().getLabel() + " à " + ((Sommet2D)firstSommet.getValue()).getSommet().getLabel()});
                                                             dialogType.setTitle("Nouvelle relation");
                                                             dialogType.setHeaderText("Direction de la relation");
                                                             dialogType.setContentText("Veuillez choisir la direction de la relation:");

                                        Optional<String> resultDirection = dialogType.showAndWait();

                                        if(resultDirection.isPresent()){
                                            if(("De " + ((Sommet2D)firstSommet.getValue()).getSommet().getLabel() + " à " + ((Sommet2D)secondSommet.getValue()).getSommet().getLabel()).equals(resultDirection.get()))
                                                this.replaceGraph2DNode(resultName.get(), Double.parseDouble(resultPonderation.get()), firstSommet, secondSommet);
                                            else this.replaceGraph2DNode(resultName.get(), Double.parseDouble(resultPonderation.get()), secondSommet, firstSommet);
                                        }
                                    }
                                    else
                                        this.replaceGraph2DNode(resultName.get(), Double.parseDouble(resultPonderation.get()), firstSommet, secondSommet);
                                }
                                catch(NumberFormatException exp){
                                    exp.printStackTrace();
                                    
                                    Alert alert = new Alert(AlertType.ERROR);
                                          alert.setTitle("Nouvelle relation");
                                          alert.setHeaderText("Erreur...");
                                          alert.setContentText("Veuillez choisir une ponderation valide (nombre réel)");
                                          alert.showAndWait();
                                }
                            }
                        }
                        else {
                            if(((Graphe2D)firstSommetParent.getValue()).getGraphe().getTypeGraphe() == TypeGraphe.NOT_PONDERATED_ORIENTED ||
                                    ((Graphe2D)firstSommetParent.getValue()).getGraphe().getTypeGraphe() == TypeGraphe.PONDERATED_ORIENTED){
                            
                                if(((Graphe2D)firstSommetParent.getValue()).getGraphe().getTypeGraphe() == TypeGraphe.NOT_PONDERATED_ORIENTED ||
                                            ((Graphe2D)firstSommetParent.getValue()).getGraphe().getTypeGraphe() == TypeGraphe.PONDERATED_ORIENTED){
                                        ChoiceDialog<String> dialogType = new ChoiceDialog<>("De " + ((Sommet2D)firstSommet.getValue()).getSommet().getLabel() + " à " + ((Sommet2D)secondSommet.getValue()).getSommet().getLabel(), new String[]{
                                                        "De " + ((Sommet2D)firstSommet.getValue()).getSommet().getLabel() + " à " + ((Sommet2D)secondSommet.getValue()).getSommet().getLabel(),
                                                        "De " + ((Sommet2D)secondSommet.getValue()).getSommet().getLabel() + " à " + ((Sommet2D)firstSommet.getValue()).getSommet().getLabel()});
                                                             dialogType.setTitle("Nouvelle relation");
                                                             dialogType.setHeaderText("Direction de la relation");
                                                             dialogType.setContentText("Veuillez choisir la direction de la relation:");

                                        Optional<String> resultDirection = dialogType.showAndWait();

                                        if(resultDirection.isPresent()){
                                            if(("De " + ((Sommet2D)firstSommet.getValue()).getSommet().getLabel() + " à " + ((Sommet2D)secondSommet.getValue()).getSommet().getLabel()).equals(resultDirection.get()))
                                                this.replaceGraph2DNode(resultName.get(), 0.0, firstSommet, secondSommet);
                                            else this.replaceGraph2DNode(resultName.get(), 0.0, secondSommet, firstSommet);
                                        }
                                    }
                            }
                            else 
                                this.replaceGraph2DNode(resultName.get(), 0.0, firstSommet, secondSommet);
                        }
                            
                    }
                    else if(resultName.isPresent()){
                        Alert alert = new Alert(AlertType.ERROR);
                              alert.setTitle("Nouvelle relation");
                              alert.setHeaderText("Erreur...");
                              alert.setContentText("Veuillez choisir un nom de relation inéxistant");
                              alert.showAndWait();
                    }
                }
                else {
                    Alert alert = new Alert(AlertType.ERROR);
                          alert.setTitle("Nouvelle relation");
                          alert.setHeaderText("Erreur...");
                          alert.setContentText("Veuillez choisir 2 sommets, le premier celui de gauche et le second celui de droite");
                          alert.showAndWait();
                }
            }
            else {
                Alert alert = new Alert(AlertType.ERROR);
                      alert.setTitle("Nouvelle relation");
                      alert.setHeaderText("Erreur...");
                      alert.setContentText("Veuillez choisir 2 sommets, le premier celui de gauche et le second celui de droite");
                      alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Nouvelle relation");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez choisir 2 sommets, le premier celui de gauche et le second celui de droite");
                  alert.showAndWait();
        }
    }

    @FXML
    void addVertex(ActionEvent event) throws RelationMismatchException, NoSommet2DException {
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();
        
        if((selectedTreeItem != null) && (selectedTreeItem.getValue() instanceof Graphe2D)){
            TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Nouveau sommet");
                        dialog.setHeaderText("Nom du sommet");
                        dialog.setContentText("Veuillez saisir le nom du sommet:");

            Optional<String> result = dialog.showAndWait();
            
            if (result.isPresent() && !this.existSommet2DNode(result.get()))
                this.replaceGraph2DNode(result.get(), selectedTreeItem);
            else if(result.isPresent()){
                Alert alert = new Alert(AlertType.ERROR);
                      alert.setTitle("Ajout d'un sommet");
                      alert.setHeaderText("Erreur...");
                      alert.setContentText("Veuillez saisir le nom d'un sommet non éxistant.");
                      alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Ajout d'un sommet");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }

    @FXML
    void deleteComponent(ActionEvent event) throws RelationMismatchException, NoSommet2DException {
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();
        
        if(selectedTreeItem != null){
            if(selectedTreeItem.getValue() instanceof Graphe2D)
                this.deleteGraph2DNode( selectedTreeItem);
            else if(selectedTreeItem.getValue() instanceof Sommet2D)
                this.deleteSommet2DNode(selectedTreeItem);
            else if(selectedTreeItem.getValue() instanceof Relation2D)
                this.deleteRelation2DNode(selectedTreeItem);
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Suppression d'un element");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un element.");
                  alert.showAndWait();
        }
    }
    
    @FXML
    void printAdjacenseMatrix(ActionEvent event) {
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();
        
        if(selectedTreeItem != null && selectedTreeItem.getValue() instanceof Graphe2D){
            Graphe2D selectedGraphe2D = (Graphe2D) selectedTreeItem.getValue();
            Graphe selectedGraphe = selectedGraphe2D.getGraphe();
            
            this.constructAdjancenseMatrix(selectedGraphe);
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Affichage d'une matrice");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }

    @FXML
    void printIncidenceMatrix(ActionEvent event) {
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();
        
        if(selectedTreeItem != null && selectedTreeItem.getValue() instanceof Graphe2D){
            Graphe2D selectedGraphe2D = (Graphe2D) selectedTreeItem.getValue();
            Graphe selectedGraphe = selectedGraphe2D.getGraphe();
           
            this.constructIncidenceMatrix(selectedGraphe);
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Affichage d'une matrice");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }

    @FXML
    void printPonderationMatrix(ActionEvent event) {
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();

        if(selectedTreeItem != null && selectedTreeItem.getValue() instanceof Graphe2D){
            Graphe2D selectedGraphe2D = (Graphe2D) selectedTreeItem.getValue();
            Graphe selectedGraphe = selectedGraphe2D.getGraphe();

            if(selectedGraphe.getTypeGraphe() == TypeGraphe.PONDERATED_NOT_ORIENTED || selectedGraphe.getTypeGraphe() == TypeGraphe.PONDERATED_ORIENTED)
                this.constructPonderationMatrix(selectedGraphe);
            else {
                Alert alert = new Alert(AlertType.ERROR);
                      alert.setTitle("Affichage d'une matrice");
                      alert.setHeaderText("Erreur...");
                      alert.setContentText("Graphe non pondere ne dispose pas d'une matrice de ponderation.");
                      alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Affichage d'une matrice");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }
    
    @FXML
    void applyBellmanFord(ActionEvent event) {
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();

        if(selectedTreeItem != null && selectedTreeItem.getValue() instanceof Graphe2D){
            // Tu travailleras avec ce graphe
            Graphe targetedGraph = ((Graphe2D) selectedTreeItem.getValue()).getGraphe();
            
            // Tu remplies ces bestioles
            PathMatrixLine line1 = new PathMatrixLine(
                    targetedGraph.getSommets().first(), 
                    targetedGraph.getSommets().last(), 
                            targetedGraph.getRelations().first(),
                            targetedGraph.getRelations().last());
            
            // Fin
            this.constructPathMatrix(line1);
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Affichage d'une matrice");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }

    @FXML
    void applyDjikskra(ActionEvent event) {
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();

        if(selectedTreeItem != null && selectedTreeItem.getValue() instanceof Graphe2D){
            // Tu travailleras avec ce graphe
            Graphe targetedGraph = ((Graphe2D) selectedTreeItem.getValue()).getGraphe();
            
            // Tu remplies ces bestioles
            PathMatrixLine line1 = new PathMatrixLine(
                    targetedGraph.getSommets().first(), 
                    targetedGraph.getSommets().last(), 
                            targetedGraph.getRelations().first(),
                            targetedGraph.getRelations().last());
            
            // Fin
            this.constructPathMatrix(line1);
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Affichage d'une matrice");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }

    @FXML
    void applyKruskal(ActionEvent event) throws NoSommet2DException, NoSommet2DException, RelationMismatchException {
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();

        if(selectedTreeItem != null && selectedTreeItem.getValue() instanceof Graphe2D){
            // Tu travailleras avec ce graphe
            Graphe targetedGraph = ((Graphe2D) selectedTreeItem.getValue()).getGraphe();
            
            // Initialise l'arbre ici
            Arbre arbre = null;
                  
            // Fin
            this.createCopyGraph2DNode(arbre);
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Affichage d'une matrice");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }

    @FXML
    void applyPrim(ActionEvent event) throws NoSommet2DException, RelationMismatchException {
        // On va créer un nouveau graphe (arbre) et on l'ajoute
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();

        if(selectedTreeItem != null && selectedTreeItem.getValue() instanceof Graphe2D){
            // Tu travailleras avec ce graphe
            Graphe targetedGraph = ((Graphe2D) selectedTreeItem.getValue()).getGraphe();
            
            // Initialise l'arbre ici c'est tout
            Arbre arbre = null;
                  
            // Fin
            this.createCopyGraph2DNode(arbre);
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Affichage d'une matrice");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }

    @FXML
    void applyWelshPowel(ActionEvent event) {
        // On va juste colorier les sommets
        TreeItem<Drawable> selectedTreeItem = this.componentsTree.getSelectionModel().getSelectedItem();

        if(selectedTreeItem != null && selectedTreeItem.getValue() instanceof Graphe2D){
            
            // Applique ton algos sur ce graphe et pour chaque sommet tu devrai avoir une couleur
            Graphe targetedGraph = ((Graphe2D) selectedTreeItem.getValue()).getGraphe();
            
            // Exemple de comment colorier
            this.colorUsRandomlySir(targetedGraph.getSommets().first(), targetedGraph.getSommets().last());
            this.colorUsRandomlySir(targetedGraph.getSommets().lower(targetedGraph.getSommets().last()));
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
                  alert.setTitle("Affichage d'une matrice");
                  alert.setHeaderText("Erreur...");
                  alert.setContentText("Veuillez d'abords selectionner un graphe.");
                  alert.showAndWait();
        }
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.initComponentsTree();
            initColorChooser();
        } catch (RelationMismatchException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Sommet a = new Sommet("a");
        Sommet b = new Sommet("b");
        Sommet c = new Sommet("c");
        
        Relation e1 = new ArcPondere("e1", 5.0, a, b);
        Relation e2 = new ArcPondere("e2", 2.5, b, c);
        Relation e3 = new ArcPondere("e3", 4.2, c, a);
        
        try {
            Graphe g = new Graphe("G", a, b, c);
                   g.setTypeGraphe(TypeGraphe.PONDERATED_ORIENTED);
            this.createGraph2DNode(g);
            
        } catch (RelationMismatchException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSommet2DException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initComponentsTree() throws RelationMismatchException {
        TreeItem<Drawable> rootItem = new TreeItem<Drawable> (new Drawable(){
            @Override
            public String UIBehavior() {
                return "Yeaaah i'm the boss and whateeuveuur youu douu, yu weuull neeuuuveuuur seuucceuuud!";
            }
        });
        rootItem.setExpanded(true);
        this.componentsTree.setCellFactory(new Callback<TreeView<Drawable>, TreeCell<Drawable>>() {
            @Override
            public TreeCell<Drawable> call(TreeView<Drawable> param) {
                return new TreeCell<Drawable>(){
                    @Override
                    protected void updateItem(Drawable item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        }
                        else {
                            String imagePath = "/MainPackage/MVC/Icons/GrapheTree.png";
                            
                            if(item instanceof Sommet2D)
                                imagePath = "/MainPackage/MVC/Icons/CircleTree.png";
                            else if(item instanceof Relation2D)
                                imagePath = "/MainPackage/MVC/Icons/RelationTree.png";
                            
                            setGraphic(new ImageView(new Image(getClass().getResourceAsStream(imagePath))));
                            setText(item.UIBehavior());
                        }
                    } 
                };
            }
        });
        this.componentsTree.setShowRoot(false);
        this.componentsTree.setRoot(rootItem);
        this.componentsTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.componentsTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Drawable>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Drawable>> observable, TreeItem<Drawable> oldValue, TreeItem<Drawable> newValue) {
                if(newValue != null && componentsTree.getSelectionModel().getSelectedItems().size() == 1){
                    Drawable item = newValue.getValue();

                    if(item instanceof Sommet2D)
                        colorChooser.setValue((Color)((Sommet2D) item).getFill());
                    else if(item instanceof Relation2D)
                        colorChooser.setValue((Color)(((Relation2D) item).getStroke()));
                }
            }
        });
    }
    
    private void initColorChooser(){
        this.colorChooser.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                if(newValue != null){
                    ObservableList<TreeItem<Drawable>> selectedDrawables = componentsTree.getSelectionModel().getSelectedItems();
                    
                    if(selectedDrawables != null)
                        for(TreeItem<Drawable> drawableTreeItem: selectedDrawables){
                            Drawable drawable = drawableTreeItem.getValue();

                            if(drawable instanceof Sommet2D)
                                ((Sommet2D) drawable).setFill(newValue);
                            else if(drawable instanceof Relation2D)
                                ((Relation2D) drawable).setStroke(newValue);
                        }
                }
            }
        });
    }
    
    private void createGraph2DNode(Graphe graphe) throws NoSommet2DException{
        Graphe2D graphe2D = new Graphe2D(graphe);
        
        TreeItem<Drawable> graphTreeItem = new TreeItem<Drawable>(graphe2D);
                           graphTreeItem.setGraphic(workSpace);
                           graphTreeItem.setExpanded(true);
        
        for(Sommet sommet: graphe.getSommets())
            graphe2D.getEnsembleSommet().add(new Sommet2D(sommet));
        
        for(Relation relation: graphe.getRelations())
            this.workSpace.getChildren().add(new Relation2D(relation));
        
        for(Sommet sommet: graphe.getSommets())
            this.workSpace.getChildren().add(sommet.getSommet2D());
        
        for(Sommet sommet: graphe.getSommets()){
            TreeItem<Drawable> sommetTreeItem = new TreeItem<Drawable>(sommet.getSommet2D());
                               sommetTreeItem.setExpanded(true);
            
            for(Relation relation: graphe.getRelations()){
                if(relation.isIncidentTo(sommet)){
                    TreeItem<Drawable> relationTreeItem = new TreeItem<Drawable>(relation.getRelation2D());
                                       relationTreeItem.setExpanded(true);
                    sommetTreeItem.getChildren().add(relationTreeItem);
                }
                graphe2D.getEnsembleRelation().add(relation.getRelation2D());
            }
            
            graphTreeItem.getChildren().add(sommetTreeItem);
        }
                
        this.componentsTree.getRoot().getChildren().add(graphTreeItem);
        this.componentsTree.getSelectionModel().clearSelection();
        this.componentsTree.getSelectionModel().select(graphTreeItem);
    }
    private void createCopyGraph2DNode(Graphe graphe) throws NoSommet2DException, RelationMismatchException{
        List<Sommet> ancientSommets = new ArrayList<Sommet>();
                          ancientSommets = (List<Sommet>)
                                  Arrays.asList((Sommet[])graphe.getSommets().toArray(new Sommet[]{}));
                                      
        Sommet[] newSommets = new Sommet[graphe.getSommets().size()];
            
        for(int i = 0; i < newSommets.length; i++)
            newSommets[i] = new Sommet(ancientSommets.get(i).getLabel());
        
        switch(graphe.getTypeGraphe()){
            case NOT_PONDERATED_NOT_ORIENTED:
                for(Relation relation: graphe.getRelations()){
                    new Arete(relation.getLabel(), newSommets[ancientSommets.indexOf(relation.getSommetGauche())], 
                                                   newSommets[ancientSommets.indexOf(relation.getSommetDroit())]);
                }
                break;
            case NOT_PONDERATED_ORIENTED:
                for(Relation relation: graphe.getRelations()){
                    new Arc(relation.getLabel(), newSommets[ancientSommets.indexOf(relation.getSommetGauche())], 
                                                   newSommets[ancientSommets.indexOf(relation.getSommetDroit())]);
                }
                break;
            case PONDERATED_NOT_ORIENTED:
                for(Relation relation: graphe.getRelations()){
                    new AretePondere(relation.getLabel(), relation.getPonderation().getPoid(), 
                            newSommets[ancientSommets.indexOf(relation.getSommetGauche())], 
                            newSommets[ancientSommets.indexOf(relation.getSommetDroit())]);
                }
                break;
            case PONDERATED_ORIENTED:
                for(Relation relation: graphe.getRelations()){
                    new ArcPondere(relation.getLabel(), relation.getPonderation().getPoid(), 
                            newSommets[ancientSommets.indexOf(relation.getSommetGauche())], 
                            newSommets[ancientSommets.indexOf(relation.getSommetDroit())]);
                }
                break;
        }
        
        Graphe newArbre = new Graphe(graphe.getNomGraphe(), newSommets);
               newArbre.setTypeGraphe(graphe.getTypeGraphe());
               
        this.createGraph2DNode(newArbre);
    }
    
    private void deleteGraph2DNode(TreeItem<Drawable> graphe2DTi){        
            for(Sommet2D s: ((Graphe2D)graphe2DTi.getValue()).getEnsembleSommet()){
                this.workSpace.getChildren().remove(s);
                this.workSpace.getChildren().remove(s.getLabel());
            }
            for(Relation2D r: ((Graphe2D)graphe2DTi.getValue()).getEnsembleRelation()){
                this.workSpace.getChildren().remove(r);
                this.workSpace.getChildren().remove(r.getLabel());
                if(r.getArrow() != null){
                    this.workSpace.getChildren().remove(r.getArrow().getHighestStroke());
                    this.workSpace.getChildren().remove(r.getArrow().getLowestStroke());
                }
            }
            
            this.componentsTree.getRoot().getChildren().remove(graphe2DTi);
            ((Graphe2D)graphe2DTi.getValue()).getGraphe().setGraphe2D(null);
    }
    private void deleteSommet2DNode(TreeItem<Drawable> sommet2DTi) throws RelationMismatchException, NoSommet2DException{
        Sommet2D sommet2D = (Sommet2D) sommet2DTi.getValue();
        Graphe2D graphe2D = (Graphe2D) (sommet2DTi.getParent().getValue());
        Graphe graphe = graphe2D.getGraphe();
        
        Sommet[] ancientSommets = new Sommet[graphe.getSommets().size()];
                 ancientSommets = graphe.getSommets().toArray(ancientSommets);
    
        Sommet[] newSommets = new Sommet[graphe.getSommets().size() - 1];
        
        for(int i = 0,j = 0; i < ancientSommets.length; i++)
            if(ancientSommets[i] != sommet2D.getSommet())
                newSommets[j++] = ancientSommets[i];
        
        Graphe newGraphe = new Graphe(graphe.getNomGraphe(), newSommets);
               newGraphe.setTypeGraphe(graphe.getTypeGraphe());
        
        this.deleteGraph2DNode(sommet2DTi.getParent());
        this.createGraph2DNode(newGraphe);
    }
    private void deleteRelation2DNode(TreeItem<Drawable> relation2DTi) throws RelationMismatchException, NoSommet2DException {
        Relation2D relation2D = (Relation2D) relation2DTi.getValue();
        Graphe2D graphe2D = (Graphe2D) (relation2DTi.getParent().getParent().getValue());
        Graphe graphe = graphe2D.getGraphe();

        Relation[] ancientRelations = new Relation[graphe.getRelations().size()];
                   ancientRelations = graphe.getRelations().toArray(ancientRelations);
                   
        Relation[] newRelations = new Relation[graphe.getRelations().size() - 1];
        
        for(int i = 0,j = 0; i < ancientRelations.length; i++)
            if(ancientRelations[i] != relation2D.getRelation())
                newRelations[j++] = ancientRelations[i];
        
        Graphe newGraphe = new Graphe(graphe.getNomGraphe(), newRelations);
               newGraphe.setTypeGraphe(graphe.getTypeGraphe());
        
        this.deleteGraph2DNode(relation2DTi.getParent().getParent());
        this.createGraph2DNode(newGraphe);
    }
    
    private boolean existGraph2DNode(String name){
        for(TreeItem<Drawable> ti : this.componentsTree.getRoot().getChildren())
            if(((Graphe2D)(ti.getValue())).getGraphe().getNomGraphe().equals(name))
                return true;
        return false;
    }
    private boolean existRelation2DNode(String name){
        for(TreeItem<Drawable> ti : this.componentsTree.getRoot().getChildren())
            for(Relation2D rel2D: ((Graphe2D)ti.getValue()).getEnsembleRelation())
                if(rel2D.getRelation().getLabel().equals(name))
                    return true;
        return false;
    }
    private boolean existSommet2DNode(String name){
        for(TreeItem<Drawable> ti : this.componentsTree.getRoot().getChildren())
            for(Sommet2D som2D: ((Graphe2D)ti.getValue()).getEnsembleSommet())
                if(som2D.getLabel().getText().equals(name))
                    return true;
        return false;
    }
    
    private void replaceGraph2DNode(String newVertex, TreeItem<Drawable> graphe2DTi) throws NoSommet2DException, RelationMismatchException{
        Graphe ancientG = ((Graphe2D)graphe2DTi.getValue()).getGraphe();
        
        Sommet[] ancientSommets = new Sommet[ancientG.getSommets().size()];
                 ancientSommets = ancientG.getSommets().toArray(ancientSommets);
                         
        Sommet[] newSommets = new Sommet[ancientSommets.length + 1];
                
        for(int i = 0; i < ancientSommets.length; i++)
            newSommets[i] = ancientSommets[i];
                
            newSommets[newSommets.length - 1] = new Sommet(newVertex);
                
        Graphe newG = new Graphe(ancientG.getNomGraphe(), newSommets);
               newG.setTypeGraphe(ancientG.getTypeGraphe());
                
        this.deleteGraph2DNode(graphe2DTi);
        this.createGraph2DNode(newG);
    }
    private void replaceGraph2DNode(String relationName, double ponderation, TreeItem<Drawable> s2dgti, TreeItem<Drawable> s2ddti) throws RelationMismatchException, NoSommet2DException{        
        Sommet2D s2dg = (Sommet2D) s2dgti.getValue();
        Sommet2D s2dd = (Sommet2D) s2ddti.getValue();
                
        Graphe ancientGraphe = ((Graphe2D) (s2dgti.getParent().getValue())).getGraphe();
        Relation newRelation = null;
        
        switch (ancientGraphe.getTypeGraphe()) {
            case NOT_PONDERATED_NOT_ORIENTED:
                newRelation = new Arete(relationName, s2dg.getSommet(), s2dd.getSommet());
                break;
            case NOT_PONDERATED_ORIENTED:
                newRelation = new Arc(relationName, s2dg.getSommet(), s2dd.getSommet());
                break;
            case PONDERATED_NOT_ORIENTED:
                newRelation = new AretePondere(relationName, ponderation, s2dg.getSommet(), s2dd.getSommet());
                break;
            default:
                newRelation = new ArcPondere(relationName, ponderation, s2dg.getSommet(), s2dd.getSommet());
                break;
        }
        
        Sommet[] newSommets = new Sommet[ancientGraphe.getSommets().size()];
                 newSommets = ancientGraphe.getSommets().toArray(newSommets);
                 
        Graphe newGraphe = new Graphe(ancientGraphe.getNomGraphe(), newSommets);
               newGraphe.setTypeGraphe(ancientGraphe.getTypeGraphe());
        
        this.deleteGraph2DNode(s2dgti.getParent());
        this.createGraph2DNode(newGraphe);
    }

    private void constructAdjancenseMatrix(Graphe graphe){        
        boolean[][] matrix = graphe.getMatriceAdjacense().getMatriceRelations();
        
        Sommet[] sommets = new Sommet[graphe.getSommets().size()];
                 sommets = graphe.getSommets().toArray(sommets);
        
        LinkedList<AdjacenseMatrixLine> lines = new LinkedList<AdjacenseMatrixLine>();
        
        for(int i = 0; i < matrix.length; i++)
            lines.add(new AdjacenseMatrixLine(sommets[i].getLabel(), matrix[i]));
        
        int nbColumn = matrix[0].length;
        
        TableView<AdjacenseMatrixLine> table = new TableView<AdjacenseMatrixLine>();
                           
        for(int i = 0; i < nbColumn + 1; i++){
            final int k = i;
            if(i == 0){
                TableColumn<AdjacenseMatrixLine, String> columnName = new TableColumn<AdjacenseMatrixLine,String>();
                                                         columnName.setPrefWidth(50.0);
                                                         columnName.setStyle("-fx-alignment: center; -fx-pref-height: 50;");
                                                         columnName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AdjacenseMatrixLine, String>, ObservableValue<String>>() {
                                                             @Override
                                                             public ObservableValue<String> call(TableColumn.CellDataFeatures<AdjacenseMatrixLine, String> param) {
                                                                 return new SimpleStringProperty(param.getValue().getSommetName());
                                                             }
                                                         });
                table.getColumns().add(columnName);
            }
            else {
                TableColumn<AdjacenseMatrixLine, Boolean> columnI = new TableColumn<AdjacenseMatrixLine, Boolean>();
                                                          columnI.setText(sommets[i - 1].getLabel());
                                                          columnI.setPrefWidth(50.0);
                                                          columnI.setCellFactory(new Callback<TableColumn<AdjacenseMatrixLine, Boolean>, TableCell<AdjacenseMatrixLine, Boolean>>() {
                                                                @Override
                                                                public TableCell<AdjacenseMatrixLine, Boolean> call(TableColumn<AdjacenseMatrixLine, Boolean> param) {
                                                                    return new TableCell<AdjacenseMatrixLine, Boolean>(){
                                                                        @Override
                                                                        protected void updateItem(Boolean item,boolean empty){
                                                                            super.updateItem(item, empty);
                                                                            if(empty){
                                                                                setText(null);
                                                                                setGraphic(null);
                                                                            }
                                                                            else{
                                                                                CheckBox cell = new CheckBox();
                                                                                         cell.setSelected(item.booleanValue());
                                                                                         cell.setDisable(true);
                                                                                         cell.setOpacity(1.0);
                                                                                setGraphic(cell);
                                                                            }
                                                                        }
                                                                    };
                                                                }
                                                          });
                                                          columnI.setStyle("-fx-alignment: center; -fx-pref-height: 50;");
                                                          columnI.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AdjacenseMatrixLine, Boolean>, ObservableValue<Boolean>>() {
                                                                @Override
                                                                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<AdjacenseMatrixLine, Boolean> param) {
                                                                    return new SimpleBooleanProperty(param.getValue().getRelations()[k - 1]).asObject();
                                                                }
                                                          });
                table.getColumns().add(columnI);
            }
        }
        
        table.getItems().addAll(lines);
        
        BorderPane borderPane = new BorderPane();
                   borderPane.setCenter(table);
        Stage stage = new Stage();
              stage.setScene(new Scene(borderPane, (nbColumn + 2) * 50, (lines.size() + 1) * 50));
              stage.setTitle("Matrice d'adjacense");
              stage.setResizable(false);
              stage.show();
    }
    private void constructIncidenceMatrix(Graphe graphe) {
        Integer[][] matrix = graphe.getMatriceIncidence().getMatriceRelations();
        
        Sommet[] sommets = new Sommet[graphe.getSommets().size()];
                 sommets = graphe.getSommets().toArray(sommets);
        Relation[] relations = new Relation[graphe.getRelations().size()];
                   relations = graphe.getRelations().toArray(relations);
        
        LinkedList<IncidenceMatrixLine> lines = new LinkedList<IncidenceMatrixLine>();
        
        for(int i = 0; i < matrix.length; i++)
            lines.add(new IncidenceMatrixLine(sommets[i].getLabel(), matrix[i]));
        
        int nbColumn = matrix[0].length;
        
        TableView<IncidenceMatrixLine> table = new TableView<IncidenceMatrixLine>();
                           
        for(int i = 0; i < nbColumn + 1; i++){
            final int k = i;
            if(i == 0){
                TableColumn<IncidenceMatrixLine, String> columnName = new TableColumn<IncidenceMatrixLine,String>();
                                                         columnName.setPrefWidth(50.0);
                                                         columnName.setStyle("-fx-alignment: center; -fx-pref-height: 50;");
                                                         columnName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IncidenceMatrixLine, String>, ObservableValue<String>>() {
                                                             @Override
                                                             public ObservableValue<String> call(TableColumn.CellDataFeatures<IncidenceMatrixLine, String> param) {
                                                                 return new SimpleStringProperty(param.getValue().getSommetName());
                                                             }
                                                         });
                table.getColumns().add(columnName);
            }
            else {
                TableColumn<IncidenceMatrixLine, Integer> columnI = new TableColumn<IncidenceMatrixLine, Integer>();
                                                          columnI.setText(relations[i - 1].getLabel());
                                                          columnI.setPrefWidth(50.0);
                                                          columnI.setCellFactory(new Callback<TableColumn<IncidenceMatrixLine, Integer>, TableCell<IncidenceMatrixLine, Integer>>() {
                                                                @Override
                                                                public TableCell<IncidenceMatrixLine, Integer> call(TableColumn<IncidenceMatrixLine, Integer> param) {
                                                                    return new TableCell<IncidenceMatrixLine, Integer>(){
                                                                        @Override
                                                                        protected void updateItem(Integer item,boolean empty){
                                                                            super.updateItem(item, empty);
                                                                            if(empty || item.intValue() == 0){
                                                                                setText(null);
                                                                                setGraphic(null);
                                                                            }
                                                                            else{
                                                                                Circle cell = new Circle();
                                                                                       cell.setRadius(5.0);
                                                                                       cell.setFill(item.intValue() == 1 ? Paint.valueOf("#0000FF") : item.intValue() == -1.0 ? Paint.valueOf("#FF0000") : Paint.valueOf("#FFFFFF"));
                                                                                setGraphic(cell);
                                                                            }
                                                                        }
                                                                    };
                                                                }
                                                          });
                                                          columnI.setStyle("-fx-alignment: center; -fx-pref-height: 50;");
                                                          columnI.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IncidenceMatrixLine, Integer>, ObservableValue<Integer>>() {
                                                                @Override
                                                                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<IncidenceMatrixLine, Integer> param) {
                                                                    return new SimpleIntegerProperty(param.getValue().getRelations()[k - 1]).asObject();
                                                                }
                                                          });
                table.getColumns().add(columnI);
            }
        }
        
        table.getItems().addAll(lines);
        
        BorderPane borderPane = new BorderPane();
                   borderPane.setCenter(table);
        Stage stage = new Stage();
              stage.setScene(new Scene(borderPane, (nbColumn + 2) * 50, (lines.size() + 1) * 50));
              stage.setTitle("Matrice d'adjacense");
              stage.setResizable(false);
              stage.show();
    }
    private void constructPonderationMatrix(Graphe graphe) {
        Double[][] matrix = graphe.getMatricePonderation().getMatriceRelations();
        
        Sommet[] sommets = new Sommet[graphe.getSommets().size()];
                 sommets = graphe.getSommets().toArray(sommets);
        
        LinkedList<PonderationMatrixLine> lines = new LinkedList<PonderationMatrixLine>();
        
        for(int i = 0; i < matrix.length; i++)
            lines.add(new PonderationMatrixLine(sommets[i].getLabel(), matrix[i]));
        
        int nbColumn = matrix[0].length;
        
        TableView<PonderationMatrixLine> table = new TableView<PonderationMatrixLine>();
                           
        for(int i = 0; i < nbColumn + 1; i++){
            final int k = i;
            if(i == 0){
                TableColumn<PonderationMatrixLine, String> columnName = new TableColumn<PonderationMatrixLine,String>();
                                                         columnName.setPrefWidth(50.0);
                                                         columnName.setStyle("-fx-alignment: center; -fx-pref-height: 50;");
                                                         columnName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PonderationMatrixLine, String>, ObservableValue<String>>() {
                                                             @Override
                                                             public ObservableValue<String> call(TableColumn.CellDataFeatures<PonderationMatrixLine, String> param) {
                                                                 return new SimpleStringProperty(param.getValue().getSommetName());
                                                             }
                                                         });
                table.getColumns().add(columnName);
            }
            else {
                TableColumn<PonderationMatrixLine, String> columnI = new TableColumn<PonderationMatrixLine, String>();
                                                          columnI.setText(sommets[i - 1].getLabel());
                                                          columnI.setPrefWidth(50.0);
                                                          columnI.setStyle("-fx-alignment: center; -fx-pref-height: 50;");
                                                          columnI.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PonderationMatrixLine, String>, ObservableValue<String>>() {
                                                                @Override
                                                                public ObservableValue<String> call(TableColumn.CellDataFeatures<PonderationMatrixLine, String> param) {
                                                                    return (new SimpleStringProperty(param.getValue().getRelations()[k - 1] != null ? Double.toString(param.getValue().getRelations()[k - 1]) : ""));
                                                                }
                                                          });
                table.getColumns().add(columnI);
            }
        }
        
        table.getItems().addAll(lines);
        
        BorderPane borderPane = new BorderPane();
                   borderPane.setCenter(table);
        Stage stage = new Stage();
              stage.setScene(new Scene(borderPane, (nbColumn + 2) * 50, (lines.size() + 1) * 50));
              stage.setTitle("Matrice d'adjacense");
              stage.setResizable(false);
              stage.show();
    }
    private void constructPathMatrix(PathMatrixLine ... paths){
        int nbColumn = 0;
        
        for(PathMatrixLine path: paths)
            if(nbColumn < path.getPath().length)
                nbColumn = path.getPath().length;
        
        TableView<PathMatrixLine> table = new TableView<PathMatrixLine>();
                           
        for(int i = 0; i < nbColumn + 1; i++){
            final int k = i;
            if(i == 0){
                TableColumn<PathMatrixLine, String> columnName = new TableColumn<PathMatrixLine,String>();
                                                         columnName.setPrefWidth(150.0);
                                                         columnName.setText("Chemins \\ Etapes");
                                                         columnName.setStyle("-fx-alignment: center; -fx-pref-height: 50;");
                                                         columnName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PathMatrixLine, String>, ObservableValue<String>>() {
                                                             @Override
                                                             public ObservableValue<String> call(TableColumn.CellDataFeatures<PathMatrixLine, String> param) {
                                                                 return new SimpleStringProperty("[ " + param.getValue().getStart().getLabel() + " ; " + param.getValue().getTarget().getLabel() + " ]");
                                                             }
                                                         });
                table.getColumns().add(columnName);
            }
            else {
                TableColumn<PathMatrixLine, String> columnI = new TableColumn<PathMatrixLine, String>();
                                                          columnI.setText("Etape n° = " + i);
                                                          columnI.setPrefWidth(100.0);
                                                          columnI.setStyle("-fx-alignment: center; -fx-pref-height: 50;");
                                                          columnI.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PathMatrixLine, String>, ObservableValue<String>>() {
                                                                @Override
                                                                public ObservableValue<String> call(TableColumn.CellDataFeatures<PathMatrixLine, String> param) {
                                                                    return (new SimpleStringProperty(((k - 1) < param.getValue().getPath().length) ? param.getValue().getPath()[k - 1].getLabel() + " = ( " + param.getValue().getPath()[k - 1].getSommetGauche().getLabel() + " ; " + param.getValue().getPath()[k - 1].getSommetDroit().getLabel() + " )" : ""));
                                                                }
                                                          });
                table.getColumns().add(columnI);
            }
        }
        
        table.getItems().addAll(paths);
        
        BorderPane borderPane = new BorderPane();
                   borderPane.setCenter(table);
        Stage stage = new Stage();
              stage.setScene(new Scene(borderPane, (nbColumn + 2) * 100, (paths.length + 1) * 50));
              stage.setTitle("Matrice d'adjacense");
              stage.setResizable(false);
              stage.show();
    }
    
    private void colorUsRandomlySir(Sommet ... sommets){
        final int a = 0;
        final int b = 255;
        
        int randomRed = (int) ((b - a) * Math.random() + a);
        int randomGreen = (int) ((b - a) * Math.random() + a);
        int randomBlue = (int) ((b - a) * Math.random() + a);
            
        for(Sommet sommet: sommets)
            sommet.getSommet2D().setFill(Color.rgb(randomRed, randomGreen, randomBlue));
    }
    
    private class AdjacenseMatrixLine {
        private String sommetName = null;
        private boolean[] relations = null;
        
        public AdjacenseMatrixLine(){};
        public AdjacenseMatrixLine(String sommetName, boolean[] relations){
            this.sommetName = sommetName;
            this.relations = relations;
        }
        
        public boolean[] getRelations(){
            return this.relations;
        }
        
        public void setRelations(boolean[] relations){
            this.relations = relations;
        }

        public String getSommetName() {
            return sommetName;
        }

        public void setSommetName(String sommetName) {
            this.sommetName = sommetName;
        }        
    }
    private class IncidenceMatrixLine {
        private String sommetName = null;
        private Integer[] relations = null;
        
        public IncidenceMatrixLine(){};
        public IncidenceMatrixLine(String sommetName, Integer[] relations){
            this.sommetName = sommetName;
            this.relations = relations;
        }
        
        public Integer[] getRelations(){
            return this.relations;
        }
        
        public void setRelations(Integer[] relations){
            this.relations = relations;
        }

        public String getSommetName() {
            return sommetName;
        }

        public void setSommetName(String sommetName) {
            this.sommetName = sommetName;
        }        
    }
    private class PonderationMatrixLine {
        private String sommetName = null;
        private Double[] relations = null;
        
        public PonderationMatrixLine(){};
        public PonderationMatrixLine(String sommetName, Double[] relations){
            this.sommetName = sommetName;
            this.relations = relations;
        }
        
        public Double[] getRelations(){
            return this.relations;
        }
        
        public void setRelations(Double[] relations){
            this.relations = relations;
        }

        public String getSommetName() {
            return sommetName;
        }

        public void setSommetName(String sommetName) {
            this.sommetName = sommetName;
        }        
    }
    private class PathMatrixLine {
        private Sommet start = null,
                       target = null;
        private Relation[] path = null;
    
        public PathMatrixLine(Sommet start, Sommet target, Relation ... path){
            this.start = start;
            this.target = target;
            this.path = path;
        }

        public Relation[] getPath() {
            return path;
        }

        public void setPath(Relation[] path) {
            this.setPath(path);
        }

        public Sommet getStart() {
            return start;
        }

        public void setStart(Sommet start) {
            this.start = start;
        }

        public Sommet getTarget() {
            return target;
        }

        public void setTarget(Sommet target) {
            this.target = target;
        }
    }
}
