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
public interface ComportementPonderation {
    public abstract double getPoid();
    public abstract void setPoid(double poid);
    public abstract SimpleDoubleProperty poidProperty();
}
