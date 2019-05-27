/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

/**
 *
 * @author fer-m
 */
public class VehicleJDBC {


    private String matricula;
    private String modelo;
    private int año;
    private String dniPropietario;

    public VehicleJDBC(String matricula, String modelo, int año) throws MyException {

        setMatricula(matricula);
        setModelo(modelo);
        setAño(año);
        

    }
        /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) throws MyException {
        String patron1 = "\\d{4}-?\\w{3}";

        if (matricula.matches(patron1)) {
            this.matricula = matricula;
        } else {
            throw new MyException("Error: matricula mal introducida- Caracteres no validos");
        }

    }

    /**
     * @return the modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * @return the año
     */
    public int getAño() {
        return año;
    }

    /**
     * @param año the año to set
     */
    public void setAño(int año) throws MyException {
        try {
            this.año = año;
        } catch (NumberFormatException ex) {
            throw new MyException("Error : caracteres de año invalidos");

        }
    }

    /**
     * @return the dniPropietario
     */
    public String getDniPropietario() {
        return dniPropietario;
    }

    /**
     * @param dniPropietario the dniPropietario to set
     */
    public void setDniPropietario(String dniPropietario) throws MyException {
        String patron2 = "\\d{8}-?[a-zA-Z]{1}";

        if (dniPropietario.matches(patron2)) {
            this.dniPropietario = dniPropietario;
        } else {
            throw new MyException("error: dni mal formado, fallo de sintaxis");
        }
    }

}
