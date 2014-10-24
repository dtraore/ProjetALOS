/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetalossimple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
     public static int iden=6;
     //structure pour l'indexation
     public static List<List<String>> listFiles=new ArrayList<List<String>>();
     
    //Depose un document dans le serveur
    public static int depotDocument(String nameFile, byte[] contenu) throws FileNotFoundException, IOException{
        List<String> list=new ArrayList<String>();
        int Id;
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
            //Génération de l'dentifiant
            Random r = new Random();
            Id = iden + r.nextInt(1000 - iden);
            //System.out.println(Id);
            
            String pre="";
            list.add(String.valueOf(Id));
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
      /*for(List<String> a:listFiles){
            System.out.println(a.get(0));
            System.out.println(a.get(1));
            System.out.println(a.get(2));
       }*/
       return Id;
    }
    
    public static List<List<String>> rechercheDocument(String[] keysWord, String param) throws XMLStreamException{
        List<List<String>> docs=new ArrayList<List<String>>();
        List<String>doc=new ArrayList<String>();
        //Stockage index fichiers trouver
        List<String> idFile=new ArrayList<String>();
        List<String> file=new ArrayList<String>();
        
        //Recherche dans l'index
        for(List<String> f:listFiles){
            //Les mots-clés sont reliés par et
                if(param.equals("et") && f.size()==3){
                    if(f.get(2).equals(keysWord[0]) || f.get(2).equals(keysWord[1] ))
                        idFile.add(f.get(0));
                        file.add(f.get(1));
                }
                if(param.equals("et") && f.size()!=3){
                    if(f.get(2).equals(keysWord[0]) && f.get(3).equals(keysWord[1]))
                        idFile.add(f.get(0));
                        file.add(f.get(1));
                }
            //Les mots-clés sont reliés par ou
                if(param.equals("ou") && f.size()==3){
                    if(f.get(2).equals(keysWord[0]) || f.get(2).equals(keysWord[1] ))
                        idFile.add(f.get(0));
                        file.add(f.get(1));
                }
                if(param.equals("ou") && f.size()!=3){
                    if(f.get(2).equals(keysWord[0]) || f.get(3).equals(keysWord[1]))
                        idFile.add(f.get(0));
                        file.add(f.get(1));
                }
            //}
        }
            
        /*for(List<String> f:listFiles){ 
            for(String o:f){
                if(param.equals("et")){
                    if(o.equals(keysWord[0])){
                        i.add(f.get(1));
                        file.add(f.get(1));
                        System.out.println(o);
                    }
                }
            }
                
        }*/
        //Parcour des fichiers pour récupérer le titre
        for(String s: file){  
            //System.out.println(s);
            
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            Source source = new StreamSource("depotDoc/"+s);
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
                                titre="Titre: "+xmlr.getText()+" Identifiant: "+idFile.get(0);
                                doc.add(titre);
                            }
                            break;
                        }
                    }
                }  
            }
        }
        docs.add(doc);
        return docs;
    }
    
    public File retourneDocument(int id){
        
        return null;
    }
    
    public void generePDF(File xsltFile, int id){
        
    }
    
    public static void arret(){
        ObjectOutputStream oos =  null ;
         try {
             File fichier =  new File("index") ;
             // ouverture d'un flux sur un fichier
             oos = new ObjectOutputStream(new FileOutputStream(fichier));
             // création d'un objet à sérializer
             Index ind =  new Index() ;
             ind.setIndex(listFiles);
             // sérialization de l'objet
             oos.writeObject(ind) ;
         } catch (IOException ex) {
             Logger.getLogger(ProjetAlosSimple.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
             try {
                 oos.close();
             } catch (IOException ex) {
                 Logger.getLogger(ProjetAlosSimple.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
    }
    public static void demarrage(){
        ObjectInputStream ois =  null ;
         try {
             File fichier =  new File("index") ;
             // ouverture d'un flux sur un fichier
             ois = new ObjectInputStream(new FileInputStream(fichier));
             // désérialization de l'objet
             Index ind = (Index)ois.readObject() ;
             System.out.println(ind.getIndex().get(0).get(0)) ;
         } catch (IOException ex) {
             Logger.getLogger(ProjetAlosSimple.class.getName()).log(Level.SEVERE, null, ex);
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(ProjetAlosSimple.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
             try {
                 ois.close();
             } catch (IOException ex) {
                 Logger.getLogger(ProjetAlosSimple.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
    }
    
    
}
