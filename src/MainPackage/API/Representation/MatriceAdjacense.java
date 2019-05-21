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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author mssm1996
 */
public class MatriceAdjacense implements ComportementMatrice{

    private SimpleObjectProperty<TreeSet<Sommet>> ensembleSommets = new SimpleObjectProperty<TreeSet<Sommet>>(null);
    private SimpleObjectProperty<TreeSet<Relation>> ensembleRelations = new SimpleObjectProperty<TreeSet<Relation>>(null);
    private SimpleObjectProperty<boolean[][]> matriceRelations = new SimpleObjectProperty<boolean[][]>(null);
    
    public MatriceAdjacense(Sommet ... sommets) throws RelationMismatchException {
        if(this.checkMismatch(sommets)) 
            throw new RelationMismatchException();
        this.chargerSommets(sommets);
    }
    
    public MatriceAdjacense(Relation ... relations) throws RelationMismatchException{
        if(this.checkMismatch(relations))
            throw new RelationMismatchException();
        this.chargerRelations(relations);
    }
    
    @Override
    public void chargerSommets(Sommet... sommets){
        this.ensembleSommets.setValue(new TreeSet<Sommet>());
        this.ensembleRelations.setValue(new TreeSet<Relation>());
        
        // Chargeons tout les sommets (sans doublons)
        for(Sommet sommet: sommets)
            this.ensembleSommets.getValue().add(sommet);
                             
        this.matriceRelations.setValue(new boolean[this.ensembleSommets.getValue().size()][this.ensembleSommets.getValue().size()]);
        
        // Supposons qu'on n'a aucune relation, et commençons à en construire
        for(int i = 0; i < this.matriceRelations.getValue().length; i++) 
            for(int j = 0; j < this.matriceRelations.getValue()[i].length; j++)
                this.matriceRelations.getValue()[i][j] = false; 
                
        // Cette liste va servir à utiliser la methode indexOf un peu plus tard,
        // car cette derniere n'existe pas dans l'objet TreeSet et j'en ai affreusement 
        // besoin :o !
        ArrayList<Sommet> sommetsWithoutDoubles = new ArrayList<Sommet>();
        
        for(Sommet sommet: this.ensembleSommets.getValue()) 
            sommetsWithoutDoubles.add(sommet);
            
        // Je me pointe dans une ligne précise avec i (imaginez la matrice devant
        // vos yeux :p) !
        for(int i = 0; i < sommetsWithoutDoubles.size(); i++){
            Sommet sommet = sommetsWithoutDoubles.get(i);
            
            // Pour n'importe quel relation, je sais déja qu'une de ses position
            // est la ligne i (parce que je collecte seulement la liste des relations
            // du sommet i !) => reste à trouver sa position j :p 
            for(Relation relation: sommet.getListeRelations()){
                
                // La position j est l'index de l'autre coté (l'autre sommet auquel
                // la relation est fixée) :p ! autrement dit c'est soit celui du gauche
                // ou alors celui de la droite, mais surtout pas le i-eme !!! 
                int j = sommetsWithoutDoubles.indexOf((sommet.equals(relation.getSommetDroit()) ? 
                                                       relation.getSommetGauche() : 
                                                       relation.getSommetDroit()));
                try{
                    /*
                        -Imaginez la relation (A,B): Gauche: A; Droite: B;
                        L'algorithme va l'inserer deux fois, quand on fera l'iteration
                        pour le sommet A , et une fois pour B;
                        
                        -Et cela n'est permi que si on a un Graphe Oriente :p
                        
                        - Solution =>  Pour inserer 2 fois:
                           
                            1)- Il faudrait soit avoir un GNO (et donc une arete)
                            2)- Soit avoir un Arc qui va de A -> B pour inserer dans la ligne A 
                                    ou un Arc B -> A pour inserer dans la ligne B,
                                    et donc on testera le sommet de gauche pour savoir !
                        
                        Testez l'arnarque sur un cahier ;) sinon, impossible à piger :p
                    */
                    if((relation instanceof Arete) || ((relation instanceof Arc) && relation.getSommetGauche().equals(sommet))){
                        this.matriceRelations.getValue()[i][j] = true;
                        this.ensembleRelations.getValue().add(relation);
                    }
                }
                catch(IndexOutOfBoundsException exp){}
            }
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
        
        this.matriceRelations.setValue(new boolean[this.ensembleSommets.getValue().size()][this.ensembleSommets.getValue().size()]);
        
        // Comme d'habitude, on suppose que tout est blanc et on commence à peindre :p
        for(int i = 0; i < this.matriceRelations.getValue().length; i++) 
            for(int j = 0; j < this.matriceRelations.getValue()[i].length; j++)
                this.matriceRelations.getValue()[i][j] = false;
                
        // Cette liste va servir à utiliser la methode indexOf, car cette derniere n'existe
        // pas dans l'objet TreeSet et j'en ai affreusement besoin :o !
        ArrayList<Sommet> sommetsWithoutDoubles = new ArrayList<Sommet>();
        
        for(Sommet sommet: this.ensembleSommets.getValue()) 
            sommetsWithoutDoubles.add(sommet);
        
        for(Relation relation: relations){
            Sommet sommetGauche = relation.getSommetGauche();
            Sommet sommetDroit = relation.getSommetDroit();
            
            // Pensez au fait que sommetsWithoutDoubles est trié (on l'a conçue
            // à partir d'un TreeSet) et donc la i-eme (j-eme) position d'une 
            // relation dans une matrice est la meme dans la liste sometsWithoutDoubles :D !
            // Sympa l'arnaque :o ?
            int i = sommetsWithoutDoubles.indexOf(sommetGauche);
            int j = sommetsWithoutDoubles.indexOf(sommetDroit);
            
            this.matriceRelations.getValue()[i][j] = true;
            
            // Un graphe construit a partir d'arete (GNO) est symetrique ;) 
            if(relation instanceof Arete) 
                this.matriceRelations.getValue()[j][i] = true;
        }
    } 
    
    @Override
    public String toString(){
        String matriceDisplay = "";
        
        Sommet[] labelColumns = new Sommet[this.ensembleSommets.getValue().size()];
        Iterator<Sommet> iterator = this.ensembleSommets.get().iterator();
        
        for(int i = 0; iterator.hasNext(); i++){
            Sommet sommet = iterator.next();
            matriceDisplay += "\t" + sommet.getLabel();
            labelColumns[i] = sommet;
        }
        
        for(int a = 0; a < this.matriceRelations.getValue().length; a++){
            
            matriceDisplay += "\n" + labelColumns[a].getLabel();
            
            for(int b = 0; b < this.matriceRelations.getValue()[a].length; b++)
                if(!this.matriceRelations.getValue()[a][b])
                    matriceDisplay += "\t0";
                else matriceDisplay += "\t1";
        }
            
        return matriceDisplay + "\n";
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
    public boolean[][] getMatriceRelations() {
        return matriceRelations.getValue();
    }

    /**
     * @param matriceRelations the matriceRelations to set
     */
    public void setMatriceRelations(boolean[][] matriceRelations) {
        this.matriceRelations.setValue(matriceRelations);
    }
    
    public TreeSet<Relation> getEnsembleRelation(){
        return this.ensembleRelations.getValue();
    }
    
    public void setEnsembleRelation(TreeSet<Relation> ensembleRelation){
        this.ensembleRelations.setValue(ensembleRelation);
    }
}
