/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odbc;

import general.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdbc.MyException;

/**
 *
 * @author fernando.martinez
 */
public class ODBCHelper {

    public static Connection conexion;

    private static void connect() throws SQLException {
        String servidorODBC = "jdbc:odbc:" + Config.DsnODBC + "";
        String user = Config.UserODBC;
        String pass = Config.PassODBC;
        conexion = DriverManager.getConnection(servidorODBC, user, pass);

    }

    private static void connectNew() throws SQLException {
        //introducimos los datos de la base de datos
        String servidorMysql = "jdbc:mysql://" + Config.HostJDBC + "/";
        String user = Config.UserJDBC;
        String pass = Config.PassJDBC;
        //cargamos el diver
        conexion = DriverManager.getConnection(servidorMysql, user, pass);
    }

    private static void loadDriver() throws MyException {
        try {
            /*try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
            } catch (InstantiationException ex) {
            throw new MyException(ex.getMessage());
            } catch (IllegalAccessException ex) {
            throw new MyException(ex.getMessage());
            } catch (ClassNotFoundException ex) {
            throw new MyException(ex.getMessage());
            }*/
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException ex) {
            throw new MyException(ex.getMessage());
        }
    }

    public static void cierraConexion() throws SQLException {
        try {
            conexion.close();
        } catch (SQLException ex) {
            throw ex;
        }

    }

    public static void abrirConexion() throws MyException {
        final int ERROR_NO_EXISTE_DATABASE = 1049;
        final int ERROR_USUARIO_INCORRECTO = 1045;
        try {
            loadDriver();
            connect();
        } catch (SQLException ex) {
            //Comprobar que se hace la conexión por primera vez
            //No existe la base de datos
            //si el usuario es correcto
            if (ex.getErrorCode() == ERROR_USUARIO_INCORRECTO) {
                throw new MyException("Error de login: usuario incorrecto");
            }
            if (ex.getErrorCode() == ERROR_NO_EXISTE_DATABASE) {

                throw new MyException(ex.getMessage());

            } else {
                throw new MyException("Imposible abrir la conexión.");
            }
        }

    }
    
        public static ArrayList<StudentODBC> getStudentsList() throws MyException {
        abrirConexion();
        PreparedStatement consulta = null;
        ResultSet result = null;
        try {

            ArrayList<StudentODBC> vehiculos = new ArrayList();
            consulta = conexion.prepareStatement("select * from estudiante order by id ;");
            result = consulta.executeQuery();
            while (result.next()) {
                //Recibimos los datos del resultSet para meterlos a un constructor
                int id = result.getInt(1);
                String nombre = result.getString(2);
                int edad = result.getInt(3);
                //String dni = result.getString(4);
//Metemos los datos en un objeto y a su vez en el arraylist
                StudentODBC s = new StudentODBC(id, nombre, edad);
                vehiculos.add(s);

            }
            cierraConexion();
            return vehiculos;

        } catch (SQLException ex) {
            throw new MyException("Error al realizar la consulta en Relleno Lista Vehiculos");
        } finally {
            if (consulta != null && result != null) {
                try {
                    consulta.close();
                    result.close();
                } catch (SQLException ex) {
                    throw new MyException("Error al cerrar ");
                }

            }

        }
    }
        
        public static void guardaVehiculo(StudentODBC v) throws MyException {
        abrirConexion();
        String sentencia = "INSERT INTO estudiante VALUES(?,?,?);";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sentencia);
            //introducimos los datos con los valores
            ps.setInt(1, v.getId());
            ps.setString(2, v.getNombre());
            ps.setInt(3, v.getEdad());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new MyException("Imposible crear el empleado: " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                    cierraConexion();
                } catch (SQLException ex) {
                    throw new MyException("Imposible terminar correctamente, comprueba los datos: " + ex.getMessage());
                }
            }
        }
    }
        
        public static void eliminoCoches(StudentODBC v) throws MyException {
        String sentencia = "DELETE  From estudiante where id = ?;";
        abrirConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sentencia);
            //ponemos valor a la? y hacemos el delete
            ps.setInt(1, v.getId());
            ps.executeUpdate();
            cierraConexion();
        } catch (SQLException ex) {
            throw new MyException("Error de: " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    throw new MyException("Imposible terminar correctamente, comprueba los datos: " + ex.getMessage());
                }
            }
        }

    }
        public static void updateVehicle(StudentODBC v) throws MyException {
        String sentencia = "UPDATE estudiante SET nombre = ?,edad = ? where id=?;";
        abrirConexion();
        PreparedStatement ps = null;

        try {
            ps = conexion.prepareStatement(sentencia);
            //Ponemos los valores de las ? y hacemos el update
            ps.setString(1, v.getNombre());
            ps.setString(2, String.valueOf(v.getEdad()));
            ps.setInt(3, v.getId());
            ps.executeUpdate();

            cierraConexion();
        } catch (SQLException ex) {
            throw new MyException("Error de SQL: " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    throw new MyException("Imposible terminar correctamente, comprueba los datos: " + ex.getMessage());
                }
            }
        }

    }
}
