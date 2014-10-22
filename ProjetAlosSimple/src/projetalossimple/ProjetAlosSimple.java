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
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
<<<<<<< HEAD
=======
import javax.xml.bind.JAXBContext;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;
>>>>>>> 1c3c71ae3792cc74af010830f69973d9fc360aad
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
<<<<<<< HEAD

=======
import metier.ArticleType;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;
>>>>>>> 1c3c71ae3792cc74af010830f69973d9fc360aad


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
    
    public static String retournNomDocument(int id)
    {
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
    
    public File retourneDocument(int id){
        
        return null;
    }
    
    public static void generePDF(int id){
        
        String nameDoc=retournNomDocument(id);
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
<<<<<<< HEAD
        System.out.println("***************");
        System.out.println(rechercheDocument(keyword,par).get(0));
=======
        System.out.println(rechercheDocument(keyword,par));
        System.out.println("****************************");
        System.out.println(retournNomDocument(10));
        
        generePDF(10);
        
        
>>>>>>> 1c3c71ae3792cc74af010830f69973d9fc360aad
    }
    
}
