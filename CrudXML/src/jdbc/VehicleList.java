/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

import dom.StudentDOM;
import java.util.ArrayList;

/**
 *
 * @author fer-m
 */
public class VehicleList {
    public static ArrayList<VehicleJDBC> vehicleList;
    public static int id_siguiente;
    public static void inicializar(){
        vehicleList = new ArrayList<VehicleJDBC>();
        id_siguiente = 0;
    }
}
