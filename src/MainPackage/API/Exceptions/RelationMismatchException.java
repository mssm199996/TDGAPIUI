/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage.API.Exceptions;

/**
 *
 * @author mssm1996
 */
public class RelationMismatchException extends Exception{
    public RelationMismatchException(){
        super("Vous avez melange deux relation de types differents dans une meme structure");
    }
}
