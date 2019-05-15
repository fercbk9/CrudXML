package dom;

import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 *
 * @author fernando.martinez
 */
public class StudentDOM {

    private String id;
    private String name;
    private int age;
    
    public StudentDOM(String id, String name,int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }
    
    //In this method, you can send a Student Object like a parameter.
    public void Add() {
        try {
            Document d = (Document) DomHelper.getDocument("src\\data\\students.xml");
            Element students = d.getDocumentElement();
            //Create student tag
            Element student = d.createElement("student");
            //Create id tag
            Element id = d.createElement("id");
            id.appendChild(d.createTextNode(getId()));
            student.appendChild(id);
            //Create name tag
            Element name = d.createElement("name");
            name.appendChild(d.createTextNode(getName()));
            student.appendChild(name);
            //Create age tag
            Element age = d.createElement("age");
            age.appendChild(d.createTextNode(String.valueOf(getAge())));
            student.appendChild(age);
            //Append tag student to a parent tag students
            students.appendChild(student);
            //Write to file
            DomHelper.saveXMLContent(d, "src\\data\\students.xml");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Delete() {
        try {
            Document d = (Document) DomHelper.getDocument("src\\data\\students.xml");
            NodeList nl = d.getElementsByTagName("student");
            for (int i = 0; i < nl.getLength(); i++) {
                Element student = (Element) nl.item(i);
                if (student.getElementsByTagName("id").item(0).getTextContent().equals(getId())) {
                    student.getParentNode().removeChild(student);
                }
            }
            //Write to file
            DomHelper.saveXMLContent(d, "src\\data\\students.xml");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Update() {
        try {
            Document d = (Document) DomHelper.getDocument("src\\data\\students.xml");
            NodeList nl = d.getElementsByTagName("student");
            for (int i = 0; i < nl.getLength(); i++) {
                Element student = (Element) nl.item(i);
                if (student.getElementsByTagName("id").item(0).getTextContent().equals(getId())) {
                    //Update value of name
                    student.getElementsByTagName("name").item(0).setTextContent(getName());
                    //Update value of age
                    student.getElementsByTagName("age").item(0).setTextContent(String.valueOf(getAge()));
                }
            }
            //Write to file
            DomHelper.saveXMLContent(d, "src\\data\\students.xml");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }
}
