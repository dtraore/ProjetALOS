/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetalossimple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;


/**
 *
 * @author DG
 */
public class ProjetAlosSimple {
     static int iden=0;
     //structure pour l'indexation
     static List<List<String>> listFiles=new ArrayList<List<String>>();
    //Depose un document dans le serveur
    public static int depotDocument(String nameFile, byte[] contenu) throws FileNotFoundException, IOException{
        List<String> list=new ArrayList<String>();
	try {
             //Convertir byte to file
            FileOutputStream fileOuputStream = new FileOutputStream("depotDoc/"+nameFile+".xml"); 
            fileOuputStream.write(contenu);
            fileOuputStream.close();
            
            //Validation du fichier xml
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schemaGrammar = schemaFactory.newSchema(new File("Projet.xsd"));
            Validator schemaValidator = schemaGrammar.newValidator();
            schemaValidator.setErrorHandler(new StAXValidation.ErrorHandler());
            Source source = new StreamSource("depotDoc/"+nameFile+".xml");
            XMLStreamReader xmlr = xmlif.createXMLStreamReader(source);
            schemaValidator.validate(source);
            
            //Ajout pour l'indexation
            iden=iden+1;
            String pre="";
            list.add(String.valueOf(iden));
            list.add(nameFile+".xml");
            
            //Extraction des mots-clés
            while (xmlr.hasNext()){
                int eventType = xmlr.next();
                if(eventType==XMLEvent.START_ELEMENT){
                    if(xmlr.getLocalName().equals("motcle")){
                        pre=xmlr.getLocalName();
                        while(xmlr.hasNext()){
                            int event = xmlr.next();
                            if(event==XMLEvent.CHARACTERS){
                                list.add(xmlr.getText());
                            }
                        break;  
                        }
                    }
                }
            }
         }
          catch (Exception e) {
              System.out.println("Document pas valide");
              return 0;
                }
       listFiles.add(list);
       return iden;
    }
    
    public static List<String> rechercheDocument(String[] keysWord, String param) throws XMLStreamException{
        List<String> docs=new ArrayList<String>();
        //Stockage index fichiers trouver
        String i=null;
        String file=null;
        //Recherche dans l'index
        for(List<String> f:listFiles){ 
            for(String o:f){
                if(o.equals(keysWord[0])){
                    i=f.get(0);
                    file=f.get(1);
                }
            }
                
        }
               
        //Parcour du fichier pour récupérer le titre
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        Source source = new StreamSource("depotDoc/"+file);
        XMLStreamReader xmlr = xmlif.createXMLStreamReader(source);
        
        
        String titre="";
        //Extraction du titre
        while (xmlr.hasNext()){
        int eventType = xmlr.next();
        if(eventType==XMLEvent.START_ELEMENT){
            if(xmlr.getLocalName().equals("titre")){
                titre=xmlr.getLocalName();
                while(xmlr.hasNext()){
                    int event = xmlr.next();
                    if(event==XMLEvent.CHARACTERS){
                        titre="Titre: "+xmlr.getText()+" Identifiant: "+i;
                        docs.add(titre);
                    }
                    break;
                    }
                }
            }  
        }
        
        return docs;
    }
    
    public File retourneDocument(int id){
        
        return null;
    }
    
    public void generePDF(File xsltFile, int id){
        
    }
    public static void main(String[] args) throws XMLStreamException {
         try {
             //Convertion file en byte
             Path path = Paths.get("Projet.xml");
             byte[] data = Files.readAllBytes(path);
             int i=depotDocument("name",data);
             //System.out.println(i);
         } catch (IOException ex) {
             Logger.getLogger(ProjetAlosSimple.class.getName()).log(Level.SEVERE, null, ex);
         }
        String[] keyword={"mot1","mot2"};
        String par="et";
        System.out.println("***************");
        System.out.println(rechercheDocument(keyword,par).get(0));
    }
    
}
