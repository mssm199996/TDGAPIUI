/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Composants.Liaison;

import MainPackage.API.Composants.Autres.Sommet;

/**
 *
 * @author mssm1996
 */
public class Arete extends Relation {
    public Arete(String label, Sommet sommetGauche, Sommet sommetDroit) {
        super(label, sommetGauche, sommetDroit);
    }
}
