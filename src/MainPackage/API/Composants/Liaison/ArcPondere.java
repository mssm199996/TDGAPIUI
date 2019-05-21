/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Composants.Liaison;

import MainPackage.API.Composants.Autres.Sommet;
import javafx.beans.property.SimpleDoubleProperty;
import MainPackage.API.Composants.Liaison.Ponderation.ComportementPonderation;
import MainPackage.API.Composants.Liaison.Ponderation.ValuePonderation;

/**
 *
 * @author mssm1996
 */
public class ArcPondere extends Arc {     
    public ArcPondere(String label, double poid, Sommet sommetGauche, Sommet sommetDroit) {
        super(label, sommetGauche, sommetDroit);
        this.ponderationValue.setValue(new ValuePonderation(poid));
    }
    
    /**
     * @return the poid
     */
    public double getPoid() {
        return this.ponderationValue.getValue().getPoid();
    }

    /**
     * @param poid the poid to set
     */
    public void setPoid(double poid) {
        this.ponderationValue.getValue().setPoid(poid);
    }
    
    public SimpleDoubleProperty poidProperty(){
        return this.ponderationValue.getValue().poidProperty();
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof ArcPondere)
            return super.equals(o) && this.ponderationValue.getValue().getPoid() == ((ArcPondere) o).ponderationValue
                    .getValue().getPoid();
        return false;
    }
    
    @Override
    public int compareTo(Object o){
        if(o instanceof ArcPondere)
            return ((int)(this.getPoid() - ((ArcPondere) o).getPoid())) == 0 ?  super.compareTo(o) : (int)(this.getPoid() - ((ArcPondere) o).getPoid());
        return 0;
    }
}
