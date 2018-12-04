package ba.unsa.rpr.tutorijal7;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.NullPointerException;

import static java.lang.Integer.min;

public class Tutorijal implements Serializable {

    public static void main(String[] args) {

        ArrayList<Grad> gradovi = ucitajGradove();
        gradovi.forEach(grad -> {
            System.out.println(grad.getNaziv());
        });

        UN un = new UN();

        Drzava bosna = new Drzava();
        bosna.setGlavniGrad(gradovi.get(0));
        bosna.setBrojStanovnika(4000000);
        bosna.setNaziv("Bosna i Hercegovina");
        bosna.setPovrsina(423423);
        bosna.setJedinicaZaPovrsinu("km2");

        ArrayList<Drzava> drzave = new ArrayList<Drzava>();

        drzave.add(bosna);

        un.setDrzave(drzave);

        zapisiXml(un);

    }


    public static void zapisiXml(UN un) {


        XMLEncoder encoder = null;

        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(unDatoteka)));
        } catch (FileNotFoundException e) {
            System.out.println("Problem pri otvaranju izlazne datoteke.");
        } catch (Exception e) {
            System.out.println("Desilo se nesto neocekivano.");
        }

        encoder.writeObject(un);
        encoder.close();
    }

    private static String ulaznaDatoteka = "mjerenja.txt";
    private static String unDatoteka = "un.xml";
    private static String drzaveDatoteka="un.xml";

    public static ArrayList<Grad> ucitajGradove() {

        Scanner citac = null;

        try {
            citac = new Scanner(new File(ulaznaDatoteka));
        } catch (FileNotFoundException e) {
            System.out.println("Ulazna datoteka ne postoji.");
        } catch (Exception e) {
            System.out.println("Desilo se nesto neocekivano.");
        }


        ArrayList<Grad> gradovi = new ArrayList<Grad>();

        while (citac.hasNextLine()) {
            String red = citac.nextLine();

            String[] vrijednosti = red.split(",");

            Grad noviGrad = new Grad();
            noviGrad.setNaziv(vrijednosti[0]);

            double [] temperature = noviGrad.getTemperature();


            for (int i = 1; i < min(vrijednosti.length, 1001); i++) {
                temperature[i - 1] = Double.parseDouble(vrijednosti[i]);
            }

            gradovi.add(noviGrad);

        }

        return gradovi;


    }
  public static UN ucitajXml(ArrayList<Grad> gradovi){
      UN un = new UN();
      return un;
    }
}