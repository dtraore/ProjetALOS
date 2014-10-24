/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetalossimple;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import static projetalossimple.ProjetAlosSimple.depotDocument;

/**
 *
 * @author DG
 */
public class Main {
    public static void main(String[] args) throws XMLStreamException {
         try {
             //Convertion file en byte
             Path path = Paths.get("M2.xml");
             byte[] data = Files.readAllBytes(path);
             int i=ProjetAlosSimple.depotDocument("M2",data);
             path = Paths.get("Projet.xml");
             data = Files.readAllBytes(path);
             i=ProjetAlosSimple.depotDocument("test",data);
             //System.out.println(i);
             //arret();
             //demarrage();
         } catch (IOException ex) {
             Logger.getLogger(ProjetAlosSimple.class.getName()).log(Level.SEVERE, null, ex);
         }
       String[] keyword={"mot1","mot2"};
        String par="ou";
        System.out.println("***************");
        for(List<String> doc:ProjetAlosSimple.rechercheDocument(keyword,par)){
            for(String s:doc)
                System.out.println(s);
        }
    }
    
}
