/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Composants.Autres;

import MainPackage.API.Composants.Liaison.ArcPondere;
import MainPackage.API.Composants.Liaison.Arete;
import MainPackage.API.Composants.Liaison.AretePondere;
import MainPackage.API.Composants.Liaison.Relation;
import MainPackage.API.Exceptions.RelationMismatchException;
import MainPackage.API.Representation.MatriceAdjacense;
import MainPackage.API.Representation.MatricePonderation;
import MainPackage.API.Representation.MatriceIncidence;
import javafx.beans.property.SimpleObjectProperty;
import MainPackage.API.Composants.Liaison.Ponderation.ValuePonderation;
import MainPackage.API.Drawers.Graphe2D;
import java.util.TreeSet;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mssm1996
 */
public class Graphe implements Comparable{
    
    private SimpleObjectProperty<MatriceIncidence> matriceIncidence = new SimpleObjectProperty<MatriceIncidence>(null);
    private SimpleObjectProperty<MatricePonderation> matricePonderation = new SimpleObjectProperty<MatricePonderation>(null);
    private SimpleObjectProperty<MatriceAdjacense> matriceAdjacense = new SimpleObjectProperty<MatriceAdjacense>(null);
    private SimpleStringProperty nomGrapheProperty = new SimpleStringProperty();
    private SimpleObjectProperty<Graphe2D> graphe2DProperty = new SimpleObjectProperty<Graphe2D>(null);
    private SimpleObjectProperty<TypeGraphe> typeGrapheProperty = new SimpleObjectProperty<TypeGraphe>(TypeGraphe.NOT_PONDERATED_NOT_ORIENTED);
    
    public Graphe(String nomGraphe, MatriceIncidence matriceIncidence) throws RelationMismatchException{
        this.matriceIncidence.setValue(matriceIncidence);
        this.matricePonderation.setValue(new MatricePonderation((Sommet[]) (this.getMatriceIncidence().getEnsembleSommets().toArray(new Sommet[]{}))));
        this.matriceAdjacense.setValue(new MatriceAdjacense((Sommet[]) this.getMatriceIncidence().getEnsembleSommets().toArray(new Sommet[]{})));
        this.nomGrapheProperty.setValue(nomGraphe);
    }
    
    public Graphe(String nomGraphe, MatricePonderation matricePonderation) throws RelationMismatchException{
        this.matricePonderation.setValue(matricePonderation);
        this.matriceIncidence.setValue(new MatriceIncidence((Sommet[]) this.getMatricePonderation().getEnsembleSommets().toArray(new Sommet[]{})));
        this.matriceAdjacense.setValue(new MatriceAdjacense((Sommet[]) this.getMatricePonderation().getEnsembleSommets().toArray(new Sommet[]{})));
        this.nomGrapheProperty.setValue(nomGraphe);
    }
    
    public Graphe(String nomGraphe, MatriceAdjacense matriceAdjacense) throws RelationMismatchException{
        this.matriceAdjacense.setValue(matriceAdjacense);
        this.matriceIncidence.setValue(new MatriceIncidence((Sommet[]) this.getMatriceAdjacense().getEnsembleSommets().toArray(new Sommet[]{})));
        this.matricePonderation.setValue(new MatricePonderation((Sommet[]) this.getMatriceAdjacense().getEnsembleSommets().toArray(new Sommet[]{})));
        this.nomGrapheProperty.setValue(nomGraphe);
    }
    
    public Graphe(String nomGraphe, Sommet ... sommets) throws RelationMismatchException{
        this.matricePonderation.setValue(new MatricePonderation(sommets));
        this.matriceIncidence.setValue(new MatriceIncidence(sommets));
        this.matriceAdjacense.setValue(new MatriceAdjacense(sommets));
        this.nomGrapheProperty.setValue(nomGraphe);
    }
    
    public Graphe(String nomGraphe, Relation ... relations) throws RelationMismatchException{
        this.matriceAdjacense.setValue(new MatriceAdjacense(relations));
        this.matriceIncidence.setValue(new MatriceIncidence(relations));
        if(relations[0].getPonderation() instanceof ValuePonderation)
            this.matricePonderation.setValue(new MatricePonderation(relations));
        this.nomGrapheProperty.setValue(nomGraphe);
    }
    /**
     * @return the matriceIncidence
     */
    public MatriceIncidence getMatriceIncidence() {
        return matriceIncidence.getValue();
    }

    /**
     * @param matriceIncidence the matriceIncidence to set
     */
    public void setMatriceIncidence(MatriceIncidence matriceIncidence) {
        this.matriceIncidence.setValue(matriceIncidence);
    }

    /**
     * @return the matriceAdjacence
     */
    public MatricePonderation getMatricePonderation() {
        return matricePonderation.getValue();
    }

    /**
     * @param matriceAdjacence the matriceAdjacence to set
     */
    public void setMatricePonderation(MatricePonderation matricePonderation) {
        this.matricePonderation.setValue(matricePonderation);
    }
    
    public MatriceAdjacense getMatriceAdjacense(){
        return this.matriceAdjacense.getValue();
    }
    
    public void setMatriceAdjacense(MatriceAdjacense matriceAdjacense){
        this.matriceAdjacense.setValue(matriceAdjacense);
    }
    
    public TypeGraphe getTypeGraphe(){
        return this.typeGrapheProperty.getValue();
    }
    
    public void setTypeGraphe(TypeGraphe tg){
        this.typeGrapheProperty.setValue(tg);
    }
    
    public void setNomGraphe(String nomGraphe){
        this.nomGrapheProperty.setValue(nomGraphe);
    }
    
    public String getNomGraphe(){
        return this.nomGrapheProperty.getValue();
    }
    
    public Graphe2D getGraphe2D(){
        return this.graphe2DProperty.getValue();
    }
    
    public void setGraphe2D(Graphe2D g2d){
        this.graphe2DProperty.setValue(g2d);
    }
    
    public TreeSet<Sommet> getSommets(){
        return this.getMatriceAdjacense().getEnsembleSommets();
    }
    
    public TreeSet<Relation> getRelations(){
        return this.getMatriceAdjacense().getEnsembleRelation();
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Graphe)
            return ((Graphe) o).getNomGraphe().equals(this.getNomGraphe());
        return false;
    }
    
    @Override
    public String toString(){
        return this.getNomGraphe();
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Graphe)
            return this.getNomGraphe().compareTo(((Graphe) o).getNomGraphe());
        return -1;
    }
    
    public enum TypeGraphe {
        NOT_PONDERATED_NOT_ORIENTED,
        PONDERATED_NOT_ORIENTED,
        NOT_PONDERATED_ORIENTED,
        PONDERATED_ORIENTED,
    }
}


