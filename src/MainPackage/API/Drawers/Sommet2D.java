/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Drawers;

import MainPackage.API.Composants.Autres.Sommet;
import com.sun.glass.ui.Screen;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 *
 * @author mssm1996
 */
public class Sommet2D extends Circle implements Drawable, Comparable{
    
    private SimpleObjectProperty<Sommet> sommet = new SimpleObjectProperty<Sommet>(null);
    private Text label2D = new Text();
    
    public Sommet2D(Sommet sommet){
        this.sommet.setValue(sommet);
        this.sommet.getValue().setSommet2D(this);
        
        this.initGraphicSettings(
                  "-fx-fill: rgb(255,255,255); "
                + "-fx-stroke-width: 2px; "
                + "-fx-stroke: rgba(0,0,0,1);",
                  "-fx-fill: rgb(0,0,0);");
        this.initLook();
        this.initBindings();
        this.initLabel(sommet);
        this.initListeners();
    }
    
    private void initLook(){
        this.setCenterX(2 * Screen.getMainScreen().getWidth() / 5);
        this.setCenterY(2 * Screen.getMainScreen().getHeight() / 5);
        this.setRadius(25.0);
    }
    
    private void initBindings(){
        this.parentProperty().addListener(new ChangeListener<Parent>(){
            @Override
            public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                if(newValue != null){
                    if(label2D.getParent() != null)
                        (((Pane)label2D.getParent()).getChildren()).remove(label2D);
                    (((Pane)newValue).getChildren()).add(label2D);
                }
            }
        });
    }
    
    private void initListeners(){
        this.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                
                double dx = getScene().getWidth() - ((Pane)getParent()).getWidth();
                double dy = getScene().getHeight()- ((Pane)getParent()).getHeight();

                double xPos = event.getSceneX() - dx;
                double yPos = event.getSceneY() - dy;
                
                setCenterX(xPos);
                setCenterY(yPos);
            }            
        });
        
        this.layoutXProperty().addListener((observable) -> { this.setLayoutX(0.0); });
        this.layoutYProperty().addListener((observable) -> { this.setLayoutY(0.0); });
    }
    
    private void initLabel(Sommet sommet){
        this.label2D.textProperty().bind(sommet.labelProperty());
        this.label2D.layoutXProperty().addListener((observable) -> { this.label2D.setLayoutX(0.0); });
        this.label2D.layoutYProperty().addListener((observable) -> { this.label2D.setLayoutY(0.0); });
        this.label2D.xProperty().bind(this.centerXProperty().subtract(this.label2D.prefWidth(0) / 2));
        this.label2D.yProperty().bind(this.centerYProperty().add(this.label2D.prefHeight(0)/4));
    }
    
    public void setSommet(Sommet sommet){
        this.sommet.setValue(sommet);
    }
    
    public Sommet getSommet(){
        return this.sommet.getValue();
    }
    
    public void initGraphicSettings(String circleCss,String labelCss){    
        this.label2D.setStyle(labelCss);
        this.setStyle(circleCss);
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Sommet2D)
            return this.getSommet().compareTo(((Sommet2D) o).getSommet());
        return -1;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Sommet2D)
            return this.getSommet().equals(((Sommet2D) o).getSommet());
        return false;
    }
    
    public Text getLabel(){
        return this.label2D;
    }
    
    public void setLabel(Text t){
        this.label2D = t;
    }
    
    @Override
    public String toString(){
        return this.getSommet().getLabel();
    }

    @Override
    public String UIBehavior() {
        return "Sommet: " + this.getSommet().getLabel();
    }
}
