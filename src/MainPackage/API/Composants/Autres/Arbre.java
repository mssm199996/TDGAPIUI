/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Composants.Autres;

import MainPackage.API.Composants.Liaison.Relation;
import MainPackage.API.Exceptions.RelationMismatchException;

/**
 *
 * @author mssm1996
 */
public class Arbre extends Graphe{
    
    public Arbre(String nomArbre, Sommet ... sommets) throws RelationMismatchException{
        super(nomArbre, sommets);
    }
    
    public Arbre(String nomArbre, Relation ... relations) throws RelationMismatchException{
        super(nomArbre, relations);
    }
}
