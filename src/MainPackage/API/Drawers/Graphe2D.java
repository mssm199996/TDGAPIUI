/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Drawers;

import MainPackage.API.Composants.Autres.Graphe;
import java.util.TreeSet;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author MSSM
 */
public class Graphe2D implements Drawable, Comparable{
    
    private SimpleObjectProperty<Graphe> grapheProperty = new SimpleObjectProperty<Graphe>();
    private SimpleObjectProperty<TreeSet<Sommet2D>> ensembleSommetsProperty = new SimpleObjectProperty<TreeSet<Sommet2D>>(null);
    private SimpleObjectProperty<TreeSet<Relation2D>> ensembleRelationsProperty = new SimpleObjectProperty<TreeSet<Relation2D>>(null);
    
    public Graphe2D(Graphe g){
        this.grapheProperty.setValue(g);
        this.grapheProperty.getValue().setGraphe2D(this);
        this.ensembleRelationsProperty.setValue(new TreeSet<Relation2D>());
        this.ensembleSommetsProperty.setValue(new TreeSet<Sommet2D>());
    }
    
    public void setGraphe(Graphe g){
        this.grapheProperty.setValue(g);
    }
    
    public Graphe getGraphe(){
        return this.grapheProperty.getValue();
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Graphe2D)
            return this.getGraphe().equals(((Graphe2D) o).getGraphe());
        return false;
    }
    
    @Override
    public String toString(){
        return this.getGraphe().getNomGraphe();
    }
    
    public TreeSet<Sommet2D> getEnsembleSommet(){
        return this.ensembleSommetsProperty.getValue();
    }
    
    public TreeSet<Relation2D> getEnsembleRelation(){
        return this.ensembleRelationsProperty.getValue();
    }
    
    public void setEnsembleSommet(TreeSet<Sommet2D> sommets2Ds){
        this.ensembleSommetsProperty.setValue(sommets2Ds);
    }
    
    public void setEnsembleRelation(TreeSet<Relation2D> relations2D){
        this.ensembleRelationsProperty.setValue(relations2D);
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Graphe2D)
            return this.getGraphe().compareTo(((Graphe2D) o).getGraphe());
        return -1;
    }
    
    public boolean isThereVertex(String vertexName){
        for(Sommet2D s : this.getEnsembleSommet())
            if(s.getSommet().getLabel().equals(vertexName))
                return true;
        return false;
    }

    @Override
    public String UIBehavior() {
        return "Graphe: " + this.getGraphe().getNomGraphe();
    }
}
