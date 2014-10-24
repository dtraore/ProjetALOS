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
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

import org.xml.sax.SAXException;


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
       return Id;
    }
    
    public static String[] rechercheDocument(String[] keysWord, String param) throws XMLStreamException{
        
        HashMap<String, String> files = new HashMap<String, String>();
        int doc=0;
        for(List<String> f:listFiles){
            //Les mots-clés sont reliés par et
                if(param.equals("et") && f.size()!=3){
                    for(int i=2; i<f.size();i++){
                        if(f.get(i).equals(keysWord[0])){
                            for(int j=2;j<f.size();j++){
                                if(f.get(j).equals(keysWord[1])){
                                   files.put(f.get(0),f.get(1));
                                }
                            }
                        }
                    }
                }
            //Les mots-clés sont reliés par ou
                if(param.equals("ou") && f.size()==3){
                    if(f.get(2).equals(keysWord[0]) || f.get(2).equals(keysWord[1] ))
                        files.put(f.get(0),f.get(1));
                }
                if(param.equals("ou") && f.size()!=3){
                    for(int i=2; i<f.size();i++){
                        if(f.get(i).equals(keysWord[0])||f.get(i).equals(keysWord[1])){
                            files.put(f.get(0),f.get(1));
                        }
                    }
                }
        }
        String[] docs=new String[files.size()];    
        for (String mapKey : files.keySet()) {
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            Source source = new StreamSource("depotDoc/"+files.get(mapKey));
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
                                    docs[doc]="Titre: "+xmlr.getText()+" Identifiant: "+mapKey;
                                    //=titre;
                                    doc++;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }  
            }
        return docs;
    }
    public static String retournNomDocument(int id){
        //cette methode permet de retourner le nom du document xml associé à l'id 
        
        String nomDoc="";
        for(int i=0; i<listFiles.size();i++)
        {
            
                if(listFiles.get(i).get(0).equals(id))
                {
                    nomDoc=listFiles.get(i).get(1);
                    break;
                }
                
        }   
        return nomDoc;
    }
    
    public static File retourneDocument(int id){
        String nameFile=retournNomDocument(id);
        //on appelle la methode retournNomDocument, quià partir de l'identifiant 
        //il retourne le nom du document xml associé ou la chainde vide pour dire le contraire
        if(nameFile.equals(""))
            {
               return null; 
            }
        else
        {
            File xmlfile = new File("depotDoc/"+nameFile);
            return xmlfile;
        }
        
    }
    
    public static void generePDF(int id){
        
            // on recupère le nom document associé à l'id  
            String nameDoc=retournNomDocument(id);
            
            //on verifie si il y a un document pour l'id fourni
            if(nameDoc.equals(""))
            {
                System.out.println("Aucune correspondance entre l'id fourni et les documents xml !!!");
            }
            else
            {
            try
            {

                String docXmlName=nameDoc.substring(0,nameDoc.length()-4);
                File xmlfile = new File("depotDoc/"+nameDoc);
                File xsltfile = new File("schema-Xsl/Projet.xsl");
                File pdffile = new File("generatedPdf/"+docXmlName+".pdf");

                final FopFactory fopFactory = FopFactory.newInstance();
                FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
                // configure foUserAgent as desired
                // Setup output
                OutputStream out = new java.io.FileOutputStream(pdffile);
                out = new java.io.BufferedOutputStream(out);

                  try {
                        // Construct fop with desired output format
                    Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                    // Setup XSLT
                    TransformerFactory factory = TransformerFactory.newInstance();
                    Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                                     // Setup input for XSLT transformation
                    Source src = new StreamSource(xmlfile);

                                     // Resulting SAX events (the generated FO) must be piped through to FOP
                    Result res = new SAXResult(fop.getDefaultHandler());

                                            // Start XSLT transformation and FOP processing
                    transformer.transform(src, res);

                  } finally {
                        out.close();
                     }
            }
            catch (Exception e) {
                                      e.printStackTrace(System.err);
                                      System.exit(-1);
                              }
            }
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
             listFiles=ind.getIndex();
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
