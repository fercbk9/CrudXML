/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import dom.DomHelper;
import dom.StudensList;
import dom.StudentDOM;
import java.io.IOException;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author fernando.martinez
 */
public class Config {

    public static String DatabaseODBC;
    public static String DsnODBC;
    public static String UserODBC;
    public static String PassODBC;

    public static String HostJDBC;
    public static String DatabaseJDBC;
    public static String UserJDBC;
    public static String PassJDBC;

    Properties configFile;

    public Config() {
        configFile = new java.util.Properties();
        try {
            configFile.load(this.getClass().getClassLoader().
                    getResourceAsStream("general/config.cfg"));
        } catch (IOException eta) {
            eta.printStackTrace();
        }
    }

    public String getProperty(String key) {
        String value = this.configFile.getProperty(key);
        return value;
    }
    public static void readProperties(Config cfg){
        DatabaseJDBC = cfg.getProperty("JDBCDatabase");
        HostJDBC = cfg.getProperty("JDBCHost");
        UserJDBC = cfg.getProperty("JDBCUser");
        PassJDBC = cfg.getProperty("JDBCPass");
        
        DatabaseODBC = cfg.getProperty("ODBCDatabase");
        DsnODBC = cfg.getProperty("ODBCDsn");
        UserODBC = cfg.getProperty("ODBCUser");
        PassODBC = cfg.getProperty("ODBCPass");
    }
}
