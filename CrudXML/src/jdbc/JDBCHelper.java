/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

import general.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author fernando.martinez
 */
public class JDBCHelper {

    public static Connection conexion;

    private static void connect() throws SQLException {
        String db = Config.DatabaseJDBC;
        String servidorMysql = "jdbc:mysql://"+ Config.HostJDBC +"/";
        String user = Config.UserJDBC;
        String pass = Config.PassJDBC;
        conexion = DriverManager.getConnection(servidorMysql + db, user, pass);

    }

    private static void connectNew() throws SQLException {
        //introducimos los datos de la base de datos
        String servidorMysql = "jdbc:mysql://"+ Config.HostJDBC +"/";
        String user = Config.UserJDBC;
        String pass = Config.PassJDBC;
        //cargamos el diver
        conexion = DriverManager.getConnection(servidorMysql, user, pass);
    }

    private static void loadDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
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
                try {
//si no existe database la crea con las tablas y los datos
                    creaBaseDeDatos();
                } catch (SQLException ex1) {
                    throw new MyException("Imposible crear la base de datos " + ex1.getMessage());
                }
            } else {
                throw new MyException("Imposible abrir la conexión.");
            }
        } catch (ClassNotFoundException ex) {

            throw new MyException("No se ha cargado la librería, " + ex.getMessage());
        }

    }

    private static void creaBaseDeDatos() throws SQLException, MyException {

        connectNew();
        String sentencia = "CREATE DATABASE  vehiculos;";
        ejecutaUpdate(sentencia);
        cierraConexion();
        abrirConexion();
        crearTablaVehiculo();
        cierraConexion();

    }

    private static void crearTablaVehiculo() {
        try {
            String sentencia = "use vehiculos;\n"
                    + "Drop table if EXISTS vehiculo;\n"
                    + "CREATE TABLE vehiculo (matricula varchar(8),modelo varchar(25),anio int(11)); ";
            //+ ""
            //+ "/*!40000 ALTER TABLE `vehiculo` DISABLE KEYS */;\n"
            //+ "INSERT INTO `vehiculo` VALUES ('0112-HRX','Alfa Romeo 4C',2013,'37865588K'),('2145-HNT','Skoda Octavia',2013,'39899766X'),('2250-HKL','BMW Serie 3',2012,'37865588K'),('2356-GWW','Dacia Sandero',2010,'22805893J'),('2589-JFX','Peugeot 208',2015,'39899766X'),('5285-HVR','Suzuki Vitara',2014,'93011363F'),('5896-FNM','Peugeot 207',2007,'37865588K'),('6598-CPL','Seat Leon',2003,'37865588K');\n"
            //+ "/*!40000 ALTER TABLE `vehiculo` ENABLE KEYS */;";
            System.out.println(sentencia);
            ejecutaUpdate(sentencia);
        } catch (SQLException ex) {
            System.out.println("error creando: " + ex.getErrorCode() + "  " + ex.getMessage());
        }

    }

    public static int ejecutaUpdate(String sentencia) throws SQLException {

        int n = 0;
        PreparedStatement st = conexion.prepareStatement(sentencia);
        n = st.executeUpdate();
        st.close();
        return n;
    }

    public static void guardaVehiculo(VehicleJDBC v) throws MyException {
        abrirConexion();
        String sentencia = "INSERT INTO vehiculo VALUES(?,?,?);";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sentencia);
            //introducimos los datos con los valores
            ps.setString(1, v.getMatricula());
            ps.setString(2, v.getModelo());
            ps.setInt(3, v.getAño());
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

    public static ArrayList<VehicleJDBC> getVehiclesList() throws MyException {
        abrirConexion();
        PreparedStatement consulta = null;
        ResultSet result = null;
        try {

            ArrayList vehiculos = new ArrayList();
            consulta = conexion.prepareStatement("select * from vehiculo order by anio ;");
            result = consulta.executeQuery();
            while (result.next()) {
                //Recibimos los datos del resultSet para meterlos a un constructor
                String matricula = result.getString(1);
                String modelo = result.getString(2);
                int año = result.getInt(3);
                //String dni = result.getString(4);
//Metemos los datos en un objeto y a su vez en el arraylist
                VehicleJDBC v = new VehicleJDBC(matricula, modelo, año);
                vehiculos.add(v);

            }
            cierraConexion();
            return vehiculos;

        } catch (MyException ex) {
            throw new MyException("Error al rellenar el listado de Vehicuylos");
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

    public static void eliminoCoches(VehicleJDBC v) throws MyException {
        String sentencia = "DELETE  From vehiculo where matricula = ?;";
        abrirConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sentencia);
            //ponemos valor a la? y hacemos el delete
            ps.setString(1, v.getMatricula());
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

    public static void updateVehicle(VehicleJDBC v) throws MyException {
        String sentencia = "UPDATE vehiculo SET matricula=?,modelo = ?,anio = ? where matricula=?;";
        abrirConexion();
        PreparedStatement ps = null;

        try {
            ps = conexion.prepareStatement(sentencia);
            //Ponemos los valores de las ? y hacemos el update
            ps.setString(1, v.getMatricula());
            ps.setString(2, v.getModelo());
            ps.setString(3, String.valueOf(v.getAño()));
            ps.setString(4, v.getMatricula());
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
    
    
        public static ArrayList<VehicleJDBC> getVehiclesListFiltered(String matriculaF) throws MyException {
        abrirConexion();
        PreparedStatement consulta = null;
        ResultSet result = null;
        try {

            ArrayList vehiculos = new ArrayList();
            consulta = conexion.prepareStatement("select * from vehiculo where matricula like ? or modelo like ? or anio like ? order by anio ;");
            consulta.setString(1, "%" + matriculaF + "%");
            consulta.setString(2, "%" + matriculaF + "%");
            consulta.setString(3, "%" + matriculaF + "%");
            result = consulta.executeQuery();
            while (result.next()) {
                //Recibimos los datos del resultSet para meterlos a un constructor
                String matricula = result.getString(1);
                String modelo = result.getString(2);
                int año = result.getInt(3);
                //String dni = result.getString(4);
//Metemos los datos en un objeto y a su vez en el arraylist
                VehicleJDBC v = new VehicleJDBC(matricula, modelo, año);
                vehiculos.add(v);

            }
            cierraConexion();
            return vehiculos;

        } catch (MyException ex) {
            throw new MyException("Error al rellenar el listado de Vehicuylos");
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
}
