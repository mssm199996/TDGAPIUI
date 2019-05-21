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
public class NoSommet2DException extends Exception{
    public NoSommet2DException(){
        super("Vous vous appretez a instancier une relation2D d'une relation de Sommets2D inconnus (null)");
    }
}
