/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import MainPackage.API.Composants.Autres.Graphe;
import MainPackage.API.Composants.Autres.Sommet;
import MainPackage.API.Composants.Liaison.ArcPondere;
import MainPackage.API.Composants.Liaison.Arete;
import MainPackage.API.Composants.Liaison.AretePondere;
import MainPackage.API.Composants.Liaison.Relation;
import MainPackage.API.Exceptions.NoSommet2DException;
import MainPackage.API.Exceptions.RelationMismatchException;
import MainPackage.API.Representation.MatriceIncidence;
import MainPackage.API.Representation.MatricePonderation;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mssm1996
 */
public class TDGAPIUI extends Application {
    
    @Override
    public void start(Stage primaryStage) throws NoSommet2DException, RelationMismatchException, IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/MainPackage/MVC/Fxmls/MainWindowFXML.fxml")), 
                800, 600);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest((event) -> { System.exit(0); });
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
