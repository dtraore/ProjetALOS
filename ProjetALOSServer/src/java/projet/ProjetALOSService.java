/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Diadie
 */
@WebService(serviceName = "ProjetALOSService")
public class ProjetALOSService {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "depotDocument")
    public int depotDocument(@WebParam(name = "nameFile") String nameFile, @WebParam(name = "contenu") byte[] contenu) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "rechercherDocument")
    public java.lang.String[] rechercherDocument(@WebParam(name = "keyword") java.lang.String[] keyword, @WebParam(name = "param") String param) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "generePDF")
    @Oneway
    public void generePDF(@WebParam(name = "id") int id) {
    }
}
