/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Drawers;

import MainPackage.API.Composants.Liaison.Arc;
import MainPackage.API.Composants.Liaison.ArcPondere;
import MainPackage.API.Composants.Liaison.AretePondere;
import MainPackage.API.Composants.Liaison.Ponderation.ValuePonderation;
import MainPackage.API.Composants.Liaison.Relation;
import MainPackage.API.Exceptions.NoSommet2DException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
/**
 *
 * @author mssm1996
 */
public class Relation2D extends Line implements Drawable, Comparable{
    
    private SimpleObjectProperty<Relation> relation = new SimpleObjectProperty<Relation>(null);
    private Arrow arrow = null;
    private Text label2D = null;
    
    public Relation2D(Relation relation) throws NoSommet2DException {
        Sommet2D sg = relation.getSommetGauche().getSommet2D();
        Sommet2D sd = relation.getSommetDroit().getSommet2D();
                      
        if(sd == null || sg == null) 
            throw new NoSommet2DException();
        
        this.relation.setValue(relation);
             relation.setRelation2D(this);
             
        this.initLabelBindings();
        this.initComponentsBindings();
        this.initParentBindings();
        this.initGraphicSettings("-fx-stroke: rgba(0,0,0,1);",
                "-fx-fill: rgb(0,0,0);");
        
        if(relation instanceof Arc)
            this.arrow = new Arrow(relation);        
    }
    
    public void setSommet(Relation relation){
        this.relation.setValue(relation);
    }
    
    public Relation getRelation(){
        return this.relation.getValue();
    }
    
    public void initGraphicSettings(String lineCss,String labelCss){
        this.setStyle(labelCss);
        this.label2D.setStyle(labelCss);
    }
    
    private void initLabelBindings(){
        this.label2D = new Text();
        this.label2D.setX(Double.MIN_VALUE);
        this.label2D.setY(Double.MIN_VALUE);
        
        Relation relation = this.getRelation();

        this.label2D.textProperty().bind(relation.labelProperty().concat(
            (relation instanceof AretePondere ? ": " + ((AretePondere) relation).getPoid() : (
                    relation instanceof ArcPondere ? ": " + ((ArcPondere) relation).getPoid() : ""))));
        this.label2D.strokeProperty().bind(this.strokeProperty());
        
        this.label2D.layoutXProperty().addListener(event -> this.setLayoutX(0.0));
        this.label2D.layoutYProperty().addListener(event -> this.setLayoutY(0.0));
        
        this.label2D.layoutXProperty().addListener(event -> this.label2D.setLayoutX(0.0));
        this.label2D.layoutYProperty().addListener(event -> this.label2D.setLayoutY(0.0));
        
        this.label2D.xProperty().bind((this.startXProperty().add((this.endXProperty().subtract(this.startXProperty())).multiply(0.8))).subtract(this.label2D.getText().length() * 7/2 * this.label2D.getStrokeWidth()));
        this.label2D.yProperty().bind((this.startYProperty().add((this.endYProperty().subtract(this.startYProperty())).multiply(0.8))).subtract(this.label2D.getText().length() * this.label2D.getStrokeWidth()));
    }
    
    private void initComponentsBindings(){
        // Layout ne doit pas influencer les composantes des vecteurs         
        this.layoutXProperty().addListener(event -> this.setLayoutX(0.0));
        this.layoutYProperty().addListener(event -> this.setLayoutY(0.0));
        //
        
        Sommet2D sg = this.getRelation().getSommetGauche().getSommet2D();
        Sommet2D sd = this.getRelation().getSommetDroit().getSommet2D();
                
        sg.centerXProperty().addListener((observable) -> {
            this.updateComponents(sg,sd);
            if(this.arrow != null)
                this.arrow.updateArrowsComponents();
        });
        sg.centerYProperty().addListener((value) -> {
            this.updateComponents(sg,sd); 
            if(this.arrow != null)
                this.arrow.updateArrowsComponents();
        });

        sd.centerXProperty().addListener((observable) -> {
            this.updateComponents(sg,sd);
            if(this.arrow != null)
                this.arrow.updateArrowsComponents();
        });
        sd.centerYProperty().addListener((observable) -> { 
            this.updateComponents(sg,sd);
            if(this.arrow != null)
                this.arrow.updateArrowsComponents();
        });
    }
    
    private void initParentBindings(){        
        this.parentProperty().addListener(new ChangeListener<Parent>(){
            @Override
            public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                if(newValue != null){
                    if(label2D.getParent() != null) 
                        (((Pane)label2D.getParent()).getChildren()).removeAll(label2D);
                    (((Pane)newValue).getChildren()).addAll(label2D);
                }
            }
        });
    }
    
    public void setArrow(Arrow arrow){
        this.arrow = arrow;
    }
    
    public Arrow getArrow(){
        return this.arrow;
    }
    
    private synchronized void updateComponents(Sommet2D sg, Sommet2D sd){
        double dx = this.getRelation().getSommetDroit().getSommet2D().getCenterX() 
                    - 
                    this.getRelation().getSommetGauche().getSommet2D().getCenterX();
        
        double x1 = sg.getCenterX();
        double y1 = sg.getCenterY();
        
        double x2 = sd.getCenterX();
        double y2 = sd.getCenterY();
                
        double alpha = Math.atan((y2 - y1) / (x2 - x1));
        double cosAlpha = Math.cos(alpha);
        double sinAlpha = Math.sin(alpha);
        
        double r1x = (Math.signum(dx) == 0 ? 1.0 : Math.signum(dx)) * cosAlpha * sg.getRadius();
        double r1y = (Math.signum(dx) == 0 ? 1.0 : Math.signum(dx)) * sinAlpha * sg.getRadius();
        
        double r2x = (Math.signum(dx) == 0 ? 1.0 : Math.signum(dx)) * cosAlpha * sd.getRadius();
        double r2y = (Math.signum(dx) == 0 ? 1.0 : Math.signum(dx)) * sinAlpha * sd.getRadius();
                
        this.setStartX(x1 + r1x);
        this.setStartY(y1 + r1y);
        this.setEndX(x2 - r2x);
        this.setEndY(y2 - r2y);
    }

    @Override
    public String toString(){
        return this.getRelation().getLabel();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Relation2D)
            return this.getRelation().equals(((Relation2D) o).getRelation());
        return false;
    }
    
    @Override
    public int compareTo(Object o) {
        if(o instanceof Relation2D)
            return this.getRelation().compareTo(((Relation2D) o).getRelation());
        return -1;
    }
    
    public Text getLabel(){
        return this.label2D;
    }
    
    public void setLabel(Text t){
        this.label2D = t;
    }

    @Override
    public String UIBehavior() {
        return this.getRelation().getClass().getSimpleName() + ": " + this.getRelation().getLabel() + 
                (this.getRelation().getPonderation() instanceof ValuePonderation ? " [" + this.getRelation().getPonderation().getPoid() + "]" : "");
    }
    
    public class Arrow {
        private double alpha = 20;
        private double r = 20;
        private Line highestStroke = null;
        private Line lowestStroke = null;
        private Relation relation = null;
        
        public Arrow(Relation relation){
            this.relation = relation;
            this.highestStroke = new Line();
            this.lowestStroke = new Line();
            this.initLines();
        }
        public Arrow(Relation relation, double alpha, double r){
            this(relation);
            this.alpha = alpha;
            this.r = r;
        }
        
        private void initLines(){
            this.getHighestStroke().layoutXProperty().addListener(event -> this.getHighestStroke().setLayoutX(0.0));
            this.getHighestStroke().layoutYProperty().addListener(event -> this.getHighestStroke().setLayoutY(0.0));
            this.getHighestStroke().strokeProperty().bind(this.getRelation().getRelation2D().strokeProperty());
            this.getLowestStroke().layoutXProperty().addListener(event -> this.getLowestStroke().setLayoutX(0.0));
            this.getLowestStroke().layoutYProperty().addListener(event -> this.getLowestStroke().setLayoutY(0.0));
            this.getLowestStroke().strokeProperty().bind(this.getRelation().getRelation2D().strokeProperty());
            
            this.getRelation().getRelation2D().parentProperty().addListener(new ChangeListener<Parent>(){
                @Override
                public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                    if(newValue != null){
                        if(getHighestStroke().getParent() != null)
                            (((Pane)getHighestStroke().getParent()).getChildren()).removeAll(getHighestStroke());
                        if(getLowestStroke().getParent() != null)
                            (((Pane)getLowestStroke().getParent()).getChildren()).removeAll(getLowestStroke());
                        (((Pane)newValue).getChildren()).addAll(getHighestStroke());
                        (((Pane)newValue).getChildren()).addAll(getLowestStroke());
                    }
                }
            });
        }
        
        public Relation getRelation() {
            return relation;
        }

        public void setRelation(Relation relation) {
            this.relation = relation;
        }
        
        private void updateArrowsComponents(){
            double d = Math.cos(Math.toRadians(this.getAlpha())) * this.getR();
            double h = Math.sin(Math.toRadians(this.getAlpha())) * this.getR();

            double dx = this.getRelation().getSommetDroit().getSommet2D().getCenterX() 
                        - 
                        this.getRelation().getSommetGauche().getSommet2D().getCenterX();

            double dy = this.getRelation().getSommetDroit().getSommet2D().getCenterY() 
                        - 
                        this.getRelation().getSommetGauche().getSommet2D().getCenterY();

            this.getHighestStroke().setEndX(this.getRelation().getRelation2D().getEndX());
            this.getHighestStroke().setEndY(this.getRelation().getRelation2D().getEndY());
            this.getLowestStroke().setEndX(this.getRelation().getRelation2D().getEndX());
            this.getLowestStroke().setEndY(this.getRelation().getRelation2D().getEndY());

            if(dx == 0){
                this.getHighestStroke().setStartX(this.getRelation().getRelation2D().getEndX() + h);
                this.getHighestStroke().setStartY(this.getRelation().getRelation2D().getEndY() - Math.signum(dy) * d);
                this.getLowestStroke().setStartX(this.getRelation().getRelation2D().getEndX() - h);
                this.getLowestStroke().setStartY(this.getRelation().getRelation2D().getEndY() - Math.signum(dy) * d);
            }
            else if(dy == 0){
                this.getHighestStroke().setStartX(this.getRelation().getRelation2D().getEndX() - Math.signum(dx) * d);
                this.getHighestStroke().setStartY(this.getRelation().getRelation2D().getEndY() - h);
                this.getLowestStroke().setStartX(this.getRelation().getRelation2D().getEndX() - Math.signum(dx) * d);
                this.getLowestStroke().setStartY(this.getRelation().getRelation2D().getEndY() + h);
            }
            else {
                double[] v = {
                    -1,
                    -(this.relation.getRelation2D().getEndY() - this.relation.getRelation2D().getStartY()) 
                        / 
                     (this.getRelation().getRelation2D().getEndX() - this.getRelation().getRelation2D().getStartX())
                };

                double[] w = {
                    1,
                    1/v[1]
                };
                // Calcule des coordoonées du point P
                double xb = this.getRelation().getRelation2D().getEndX();
                double yb = this.getRelation().getRelation2D().getEndY();
                double k = (this.getRelation().getRelation2D().getEndY() - this.getRelation().getRelation2D().getStartY()) 
                                / 
                           (this.getRelation().getRelation2D().getEndX() - this.getRelation().getRelation2D().getStartX());
                double kPr = yb - k * this.getRelation().getRelation2D().getEndX();

                double A = k * k + 1;
                double B = xb * (1 + k * k);
                double C = xb * xb * (1 + k * k) - d * d;

                double xp = (B - Math.signum(dx) * Math.sqrt(B * B - A * C)) / A;
                double yp = k * xp + kPr;

                // Calcule des coordoonées du point w

                double s = w[1] / w[0];
                double sPr = yp - s * xp;

                double D = s * s + 1;
                double E = xp * (1 + s * s);
                double F = xp * xp * (1 + s * s) - h * h;

                double xw = (E - Math.signum(dx) * Math.sqrt(E * E - D * F)) / D;
                double yw = s * xw + sPr;

                // Calcule des coordoonées du point w'

                double xwPr = 2 * xp - xw;
                double ywPr = 2 * yp - yw;

                this.getHighestStroke().setStartX(xw);
                this.getHighestStroke().setStartY(yw);
                this.getLowestStroke().setStartX(xwPr);
                this.getLowestStroke().setStartY(ywPr);
            }
        }

        public double getAlpha() {
            return alpha;
        }

        public void setAlpha(double alpha) {
            this.alpha = alpha;
        }

        public double getR() {
            return r;
        }

        public void setR(double r) {
            this.r = r;
        }

        public Line getHighestStroke() {
            return highestStroke;
        }

        public void setHighestStroke(Line highestStroke) {
            this.highestStroke = highestStroke;
        }

        public Line getLowestStroke() {
            return lowestStroke;
        }

        public void setLowestStroke(Line lowestStroke) {
            this.lowestStroke = lowestStroke;
        }
        
        @Override
        public boolean equals(Object o){
            if(o instanceof Arrow)
                return this.getRelation().equals(((Arrow) o).getRelation());
            return false;
        }
    }
}