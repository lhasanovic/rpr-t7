package ba.unsa.rpr.tutorijal7;

import ba.unsa.rpr.tutorijal7.Drzava;
import ba.unsa.rpr.tutorijal7.Grad;
import ba.unsa.rpr.tutorijal7.UN;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLEncoder;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;


public class Tutorijal {

    @SuppressWarnings("ALL")
    public static UN ucitajXml(ArrayList<Grad> gradovi) throws FileNotFoundException, TransformerException, TransformerConfigurationException {
        ArrayList<Drzava> drzave = new ArrayList<Drzava>();

        Document xmldoc = null;
        try {
            DocumentBuilder docReader
                    = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmldoc = docReader.parse(new File("drzave.xml"));
        }
        catch (Exception e) {
            System.out.println("drzave.xml nije validan XML dokument");
        }


        Element root = xmldoc.getDocumentElement();
        Element mjerenja = xmldoc.createElement("temperature");
        root.normalize();
        NodeList children = root.getChildNodes();

        for(int i=0;i<children.getLength();i++){
            Node drzavaNode = children.item(i);
            if (drzavaNode.getNodeType() == Node.ELEMENT_NODE) {
                Drzava novaDrzava = new Drzava();
                Grad noviGrad = new Grad();
                Element drzavaElement = (Element) drzavaNode;

                NodeList glavniGradElementi = ((Element) drzavaNode).getElementsByTagName("glavnigrad");
                if(glavniGradElementi.getLength()>=1 ){
                    Element gradElement = ((Element)glavniGradElementi.item(0));
                    noviGrad.setNaziv(gradElement.getElementsByTagName("naziv").item(0).getTextContent());
                    noviGrad.setBrojStanovnika(Integer.valueOf(gradElement.getElementsByTagName("brojStanovnika").item(0).getTextContent()));

                    String tmp = "";
                    for(int k=0;k<gradovi.size();k++)
                        if(gradovi.get(k).getNaziv().equals(gradElement.getElementsByTagName("naziv").item(0).getTextContent())){
                            for(int p=0;p<gradovi.get(k).getTemperature().length;p++)
                                tmp += String.valueOf(p) + ", ";

                            mjerenja.appendChild(xmldoc.createTextNode(tmp));
                        }

                    ((Element) glavniGradElementi.item(0)).setAttribute("temperature", tmp);
                    Source s = new DOMSource(xmldoc);
                    StreamResult r = new StreamResult("drzave.xml");
                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer t = tf.newTransformer();
                    t.transform(s, r);
                }

                novaDrzava.setGlavniGrad(noviGrad);
                novaDrzava.setBrojStanovnika(Integer.valueOf(drzavaElement.getElementsByTagName("brojStanovnika").item(0).getTextContent()));
                novaDrzava.setNaziv(drzavaElement.getElementsByTagName("naziv").item(0).getTextContent());
                novaDrzava.setJedinicaZaPovrsinu(drzavaElement.getElementsByTagName("jedinicazapovrsinu").item(0).getTextContent());
                novaDrzava.setPovrsina(Double.valueOf(drzavaElement.getElementsByTagName("povrsina").item(0).getTextContent()));
                drzave.add(novaDrzava);
            }
        }

        return new UN(drzave);
    }

    public static void zapisiXML(UN u) {
        try {
            java.beans.XMLEncoder izlaz = new XMLEncoder(new FileOutputStream("un.xml"));
            izlaz.writeObject(u);
            izlaz.close();
        } catch(Exception e) {
            System.out.println("Greška: " + e.getMessage());
        }
    }

    public static ArrayList<Grad> ucitajGradove() throws FileNotFoundException {
        // Naziv grada, mjerenja (niz)
        ArrayList<Grad> gradoviData = new ArrayList<>();
        try{
            Scanner fr = new Scanner(new FileReader("mjerenja.txt")).useDelimiter("[\\r\\n,]");
            while(fr.hasNext()){
                String naziv = fr.next();
                int brojac=0;
                ArrayList<Double> mjerenja = new ArrayList<>();
                while(fr.hasNextDouble()){
                    double vrijednost=fr.nextDouble();
                    if(brojac<1000)
                        mjerenja.add(vrijednost);
                    brojac++;
                }
                if(fr.hasNext()) fr.next();
                Grad grad = new Grad(naziv,0,mjerenja);
                gradoviData.add(grad);
            }

            fr.close();
        }
        catch(FileNotFoundException fnfe){
            System.out.println(fnfe.getMessage()+ " " + fnfe.getCause() + " " + fnfe.getStackTrace() + " " + fnfe.getClass());
        }
        catch(Exception e){
            System.out.println(e.getMessage() + " " + e.getCause() + " " +e.getStackTrace() + " " +e.getClass());
        }

        return  gradoviData;
    }

    public static void main(String[] args) throws FileNotFoundException, TransformerConfigurationException, TransformerException {
        UN un = ucitajXml(ucitajGradove());

        ArrayList<Drzava> hehe = un.getDrzave();
        try{
            for(Drzava d: hehe)
                System.out.println(d);

            zapisiXML(un);
        }catch(Exception e){
            System.out.println("Hehe" + " " + e.getMessage());
        }
    }
}

