/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

/**
 *Clase que controla y lanza excepciones
 * @author Alumno
 */
public class MyException extends Exception {
    //Creamos el constructor de myException para los errores

    /**
     * Con este metodo cogemos todas las excepciones y las mandamos
     *
     * @param mensaje
     */
    public MyException(String mensaje) {

        super(mensaje);
    }
}
