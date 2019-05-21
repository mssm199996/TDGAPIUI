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
import java.util.LinkedList;

/**
 *
 * @author mssm1996
 */
public interface ComportementMatrice {
    public abstract void chargerSommets(Sommet ... sommets);
    public abstract void chargerRelations(Relation ... relations);
    
    public default boolean checkMismatch(Relation ... relations) throws RelationMismatchException {
        for(Relation r1: relations)
            for(Relation r2: relations)
                if((r1 instanceof Arete && r2 instanceof Arc) || 
                        (r1 instanceof Arc && r2 instanceof Arete))
                        return true;
        return false;
    }
    
    public default boolean checkMismatch(Sommet ... sommets) throws RelationMismatchException {
        LinkedList<Relation> relations = new LinkedList<Relation>();
        
        for(Sommet sommet: sommets) 
            relations.addAll(sommet.getListeRelations());
        
        for(Relation r1: relations)
            for(Relation r2: relations)
                if((r1 instanceof Arete && r2 instanceof Arc) || 
                   (r1 instanceof Arc && r2 instanceof Arete))
                        return true;
        return false;
    }
}
