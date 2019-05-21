/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Composants.Autres;

import MainPackage.API.Composants.Liaison.Relation;
import MainPackage.API.Drawers.Sommet2D;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mssm1996
 */
public class Sommet implements Comparable{

    private SimpleObjectProperty<ArrayList<Relation>> listeRelations = new SimpleObjectProperty<ArrayList<Relation>>(new ArrayList<Relation>());
    private SimpleObjectProperty<Sommet2D> sommet2D = new SimpleObjectProperty<Sommet2D>(null);
    private SimpleStringProperty label = new SimpleStringProperty("No Label");
    
    public Sommet(String label){
        this.label.set(label);
    }
    
    public Sommet(String label,Relation ... relations){
        this.label.set(label);
        this.listeRelations.getValue().addAll(Arrays.asList(relations));
    }
    
    public SimpleStringProperty labelProperty(){
        return this.label;
    }
    
    public SimpleObjectProperty<ArrayList<Relation>> listeRelationsProperty(){
        return this.listeRelations;
    }
    
    public SimpleObjectProperty<Sommet2D> sommet2DProperty(){
        return this.sommet2D;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label.getValue();
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label.setValue(label);
    }

    /**
     * @return the listeRelations
     */
    public ArrayList<Relation> getListeRelations() {
        return listeRelations.getValue();
    }

    /**
     * @param listeRelations the listeRelations to set
     */
    public void setListeRelations(ArrayList<Relation> listeRelations) {
        this.listeRelations.setValue(listeRelations);
    }
    
    public Sommet2D getSommet2D(){
        return this.sommet2D.get();
    }
    
    public void setSommet2D(Sommet2D sommet2D){
        this.sommet2D.setValue(sommet2D);
    }
    
    @Override 
    public String toString(){
        return "Description du Sommet: " + this.label.getValue() + "\n";
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Sommet)
            return this.hashCode() == o.hashCode();
        return false;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Sommet)
            return (this.label.getValue().compareTo(((Sommet) o).label.getValue()));
        return 0;
    }

    public boolean isIncidentTo(Relation relation){
        for(Relation r: this.listeRelations.getValue())
            if(relation.equals(r)) return true;
        return false;
    }
}
