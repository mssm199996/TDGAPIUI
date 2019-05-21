/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Composants.Liaison.Ponderation;

import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author mssm1996
 */
public class NoPonderation implements ComportementPonderation{

    @Override
    public double getPoid() {
        throw new UnsupportedOperationException("No ponderation !");
    }

    @Override
    public void setPoid(double poid) {
        throw new UnsupportedOperationException("No Ponderation !");
    }

    @Override
    public SimpleDoubleProperty poidProperty() {
        throw new UnsupportedOperationException("No Ponderation."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
