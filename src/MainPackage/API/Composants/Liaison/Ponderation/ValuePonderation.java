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
public class ValuePonderation implements ComportementPonderation{

    private SimpleDoubleProperty ponderationValue = new SimpleDoubleProperty(0.0);
    
    public ValuePonderation(double value){
        this.setPoid(value);
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof ValuePonderation)
            return this.getPoid() == ((ValuePonderation) o).getPoid();
        return false;
    }
    
    @Override
    public double getPoid() {
        return this.ponderationValue.getValue();
    }

    @Override
    public void setPoid(double poid) {
        this.ponderationValue.setValue(poid);
    }   

    @Override
    public SimpleDoubleProperty poidProperty() {
        return this.ponderationValue;
    }
}
