package dom;

import java.io.StringWriter;
import java.util.ArrayList;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import javax.xml.parsers.*;

/**
 *
 * @author fernando.martinez
 */
public class DomHelper {

    public static Document getDocument(String path_to_file) {
        Document d = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            d = db.parse(path_to_file);
        } catch (Exception e) {
            d = null;
            e.printStackTrace();
        }
        return d;
    }

    public static String getXMLContent(Document d) {
        String result = "";
        try {
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            DOMSource source = new DOMSource(d);
            tf.transform(source, sr);
            result = sw.toString();
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }

    public static void saveXMLContent(Document d, String path_to_file) {
        try {
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(d);
            StreamResult sr = new StreamResult(path_to_file);
            tf.transform(source, sr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void readXMLToList(){
        StudensList.inicializar();
        String id;
        String name;
        int age;
         try {
            Document d = (Document) DomHelper.getDocument("src\\data\\students.xml");
            NodeList nl = d.getElementsByTagName("student");
            for (int i = 0; i < nl.getLength(); i++) {
                Element student = (Element) nl.item(i);
                id = student.getElementsByTagName("id").item(0).getTextContent();
                name = student.getElementsByTagName("name").item(0).getTextContent();
                age = Integer.parseInt(student.getElementsByTagName("age").item(0).getTextContent());
                StudentDOM std = new StudentDOM(id, name, age);
                StudensList.studentsList.add(std);
                StudensList.id_siguiente = Integer.parseInt(id) + 1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
        public static boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
}
