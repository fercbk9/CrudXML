/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odbc;

import java.util.ArrayList;

/**
 *
 * @author fernando.martinez
 */
public class StudentList {

    public static ArrayList<StudentODBC> studentList;
    public static int id_siguiente;

    public static void inicializar() {
        studentList = new ArrayList<>();
        id_siguiente = 0;
    }
}
