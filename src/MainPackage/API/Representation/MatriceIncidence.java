/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Representation;

import MainPackage.API.Composants.Liaison.Arc;
import MainPackage.API.Composants.Liaison.Arete;
import MainPackage.API.Composants.Liaison.Relation;
import MainPackage.API.Composants.Autres.Sommet;
import MainPackage.API.Composants.Liaison.ArcPondere;
import MainPackage.API.Composants.Liaison.AretePondere;
import MainPackage.API.Exceptions.RelationMismatchException;
import java.util.Iterator;
import java.util.TreeSet;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author mssm1996
 */
public class MatriceIncidence implements ComportementMatrice{
    
    private SimpleObjectProperty<TreeSet<Sommet>> ensembleSommets = new SimpleObjectProperty<TreeSet<Sommet>>(null);
    private SimpleObjectProperty<TreeSet<Relation>> ensembleRelations = new SimpleObjectProperty<TreeSet<Relation>>(null);
    private SimpleObjectProperty<Integer[][]> matriceRelations = new SimpleObjectProperty<Integer[][]>(null);

    public MatriceIncidence(Sommet ... sommets) throws RelationMismatchException {
        if(this.checkMismatch(sommets)) 
            throw new RelationMismatchException();
        this.chargerSommets(sommets);
    }
    
    public MatriceIncidence(Relation ... relations) throws RelationMismatchException {
        if(this.checkMismatch(relations)) 
            throw new RelationMismatchException();
        this.chargerRelations(relations);
    }

    @Override
    public void chargerSommets(Sommet... sommets) {
        this.ensembleSommets.setValue(new TreeSet<Sommet>());
        this.ensembleRelations.setValue(new TreeSet<Relation>());
        
        // La routine :o, chargement sans doublons !
        for(Sommet sommet: sommets){
            this.ensembleSommets.getValue().add(sommet);
            
            for(Relation relation: sommet.getListeRelations())
                this.ensembleRelations.getValue().add(relation);
        }
        
        this.matriceRelations.setValue(new Integer[this.ensembleSommets.getValue().size()][this.ensembleRelations.getValue().size()]);
        
        // Supposons qu'il n y a aucune relation, et commençons la construction !
        for(int i = 0; i < this.matriceRelations.getValue().length; i++) 
            for(int j = 0; j < this.matriceRelations.getValue()[i].length; j++)
                this.matriceRelations.getValue()[i][j] = new Integer(0); 
        
        int i = 0;
        
        // Algorithme assez simple, consiste à parcourir tout les sommets
        // et pour chaqu'un, trouver les aretes qui y sont incidents.
        // Si incident => {1/-1}
        for(Sommet sommet: this.ensembleSommets.getValue()){

            int j = 0;
            
            for(Relation relation: this.ensembleRelations.getValue()){
                
                // Verifions si sommet est incident à relation
                if(relation.getSommetDroit().equals(sommet) || relation.getSommetGauche()
                        .equals(sommet)){
                    if(relation instanceof Arete || relation instanceof AretePondere) this.matriceRelations.getValue()[i][j] = 1;
                    else if(relation instanceof Arc){
                        if(((Arc) relation).getSommetGauche().equals(sommet)) 
                            this.matriceRelations.getValue()[i][j] = 1;
                        else this.matriceRelations.getValue()[i][j] = -1;
                    }
                    else if(relation instanceof ArcPondere){
                        if(((ArcPondere) relation).getSommetGauche().equals(sommet)) 
                            this.matriceRelations.getValue()[i][j] = 1;
                        else this.matriceRelations.getValue()[i][j] = -1;
                    }
                }
                j++;
            }
            i++;
        }
    }

    @Override
    public void chargerRelations(Relation... relations) {
        this.ensembleSommets.setValue(new TreeSet<Sommet>());
        this.ensembleRelations.set(new TreeSet<Relation>());
        
        // Toujours et toujours le cas !, relations et sommets non doublés !!!
        for(Relation relation: relations)
            this.ensembleRelations.getValue().add(relation);
        
        for(Relation relation: this.ensembleRelations.getValue()){
            this.ensembleSommets.getValue().add(relation.getSommetDroit());
            this.ensembleSommets.getValue().add(relation.getSommetGauche());
        }
        
        this.matriceRelations.setValue(new Integer[this.ensembleSommets.getValue().size()][this.ensembleSommets.getValue().size()]);
        
        // Comme d'habitude, on suppose que tout est blanc et on commence à peindre :p
        for(int i = 0; i < this.matriceRelations.getValue().length; i++) 
            for(int j = 0; j < this.matriceRelations.getValue()[i].length; j++)
                this.matriceRelations.getValue()[i][j] = new Integer(0);
        
        int i = 0;
        
        // Meme principe que l'algorithme precedent
        for(Sommet sommet: this.ensembleSommets.getValue()){
            
            int j = 0;
                        
            for(Relation relation: this.ensembleRelations.getValue()){
                // Verifions si sommet est incident à relation
                if(relation.getSommetDroit().equals(sommet) || relation.getSommetGauche().equals(sommet)) {
                    if(relation instanceof Arete || relation instanceof AretePondere) 
                        this.matriceRelations.getValue()[i][j] = 1;
                    else if(relation instanceof Arc || relation instanceof ArcPondere){
                        if(relation.getSommetGauche().equals(sommet)) 
                            this.matriceRelations.getValue()[i][j] = 1;
                        else this.matriceRelations.getValue()[i][j] = -1;
                    }
                }
                j++;
            }
            i++;
        }
    }

    /**
     * @return the ensembleSommets
     */
    public TreeSet<Sommet> getEnsembleSommets() {
        return ensembleSommets.getValue();
    }

    /**
     * @param ensembleSommets the ensembleSommets to set
     */
    public void setEnsembleSommets(TreeSet<Sommet> ensembleSommets) {
        this.ensembleSommets.setValue(ensembleSommets);
    }

    /**
     * @return the matriceRelations
     */
    public Integer[][] getMatriceRelations() {
        return matriceRelations.getValue();
    }

    /**
     * @param matriceRelations the matriceRelations to set
     */
    public void setMatriceRelations(Integer[][] matriceRelations) {
        this.matriceRelations.setValue(matriceRelations);
    }
    
    public TreeSet<Relation> getEnsembleRelation(){
        return this.ensembleRelations.getValue();
    }
    
    public void setEnsembleRelation(TreeSet<Relation> ensembleRelation){
        this.ensembleRelations.setValue(ensembleRelation);
    }
    
    @Override
    public String toString(){
        String matrixDisplay = "";
        
        Sommet[] labelLines = new Sommet[this.ensembleSommets.getValue().size()];
        
        Iterator<Relation> relationIterator = this.ensembleRelations.getValue().iterator();
        Iterator<Sommet> sommetIterator = this.ensembleSommets.getValue().iterator();
        
        for(int i = 0; relationIterator.hasNext() || sommetIterator.hasNext() ; i++){
            Relation relation = (relationIterator.hasNext() ? relationIterator.next() : null);
            Sommet sommet = (sommetIterator.hasNext() ? sommetIterator.next() : null);
            
            if(sommet != null) labelLines[i] = sommet;
            if(relation != null) matrixDisplay += "\t" + relation.getLabel();
        }
                
        for(int a = 0; a < this.matriceRelations.getValue().length; a++){
            
            matrixDisplay += "\n" + labelLines[a].getLabel();
            
            for(int b = 0; b < this.matriceRelations.getValue()[a].length; b++)
                matrixDisplay += "\t" + this.matriceRelations.getValue()[a][b];
        }
        
        return matrixDisplay + "\n";
    }
}
