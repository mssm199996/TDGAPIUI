
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Composants.Liaison;

import MainPackage.API.Composants.Autres.Sommet;
import MainPackage.API.Composants.Liaison.Ponderation.ComportementPonderation;
import MainPackage.API.Composants.Liaison.Ponderation.NoPonderation;
import MainPackage.API.Drawers.Relation2D;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mssm1996
 */
public abstract class Relation implements Comparable {
    
    private SimpleStringProperty label = new SimpleStringProperty("No Label");
    private SimpleObjectProperty<Sommet> sommetGauche = new SimpleObjectProperty<Sommet>(null),
                                         sommetDroit = new SimpleObjectProperty<Sommet>(null);
    private SimpleObjectProperty<Relation2D> relation2D = new SimpleObjectProperty<Relation2D>(null);
    protected SimpleObjectProperty<ComportementPonderation> ponderationValue = new SimpleObjectProperty<ComportementPonderation>(new NoPonderation());
    
    public Relation(String label, Sommet sommetGauche, Sommet sommetDroit){
        this.label.set(label);
        this.sommetGauche.setValue(sommetGauche);
        this.sommetDroit.setValue(sommetDroit);
        
        this.sommetDroit.getValue().getListeRelations().add(this);
        this.sommetGauche.getValue().getListeRelations().add(this);
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
     * @return the sommetGauche
     */
    public Sommet getSommetGauche() {
        return sommetGauche.getValue();
    }

    /**
     * @param sommetGauche the sommetGauche to set
     */
    public void setSommetGauche(Sommet sommetGauche) {
        this.sommetGauche.setValue(sommetGauche);
    }

    /**
     * @return the sommetDroit
     */
    public Sommet getSommetDroit() {
        return sommetDroit.getValue();
    }

    /**
     * @param sommetDroit the sommetDroit to set
     */
    public void setSommetDroit(Sommet sommetDroit) {
        this.sommetDroit.setValue(sommetDroit);
    }
    
    public void setRelation2D(Relation2D relation2D){
        this.relation2D.setValue(relation2D);
    }
    
    public Relation2D getRelation2D(){
        return this.relation2D.getValue();
    }
    
    @Override
    public int compareTo(Object o) {
        return (int)(this.label.getValue().compareTo(((Relation)o).getLabel())); 
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Relation)
            return this.getSommetGauche().equals(((Relation) o).getSommetGauche()) && this.getSommetDroit().equals(((Relation) o).getSommetDroit());
        return false;
    }
    
    public SimpleObjectProperty<Sommet> sommetGaucheProperty(){
        return this.sommetGauche;
    }
    
    public SimpleObjectProperty<Sommet> sommetDroitProperty(){
        return this.sommetDroit;
    }
    
    public SimpleStringProperty labelProperty(){
        return this.label;
    }
    
    public SimpleObjectProperty<Relation2D> relation2DProperty(){
        return this.relation2D;
    }
    
    public boolean isIncidentTo(Sommet sommet){
        return (this.sommetDroit.getValue().equals(sommet) || this.sommetGauche.getValue().equals(sommet));
    }    
    
    @Override
    public String toString(){
        return "Description de l'arete: " + this.label.getValue() + "\n"
                + "Sommet Gauche: " + this.sommetGauche.getValue().getLabel() + "\n" 
                + "Sommet Droit: " + this.sommetDroit.getValue().getLabel() + "\n";
    }
    
    public ComportementPonderation getPonderation(){
        return this.ponderationValue.getValue();
    }
}
