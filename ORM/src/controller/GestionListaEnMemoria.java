/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Armak;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author DM3-2-16
 */
public class GestionListaEnMemoria {
    
    private static Configuration config = new Configuration();
    private static SessionFactory faktorial = config.configure().buildSessionFactory();
    private static Session sesioa;

    public GestionListaEnMemoria() {
       
    }

    public static ObservableList<Armak> cargarArmas() {

        ObservableList<Armak> arm = FXCollections.observableArrayList();

        try {
            sesioa = faktorial.openSession();
            for (Iterator<Armak> iter = sesioa.createQuery("FROM Armak").iterate(); iter.hasNext();) {
                Armak arma = iter.next();
                //Hibernate.initialize(arma.getId());
                arm.add(arma);
                System.out.println(arma.getName()+ "\t" + arma.getJatorria()+ "\t" + arma.getDeskribapena()+ "\t" + arma.getUrtea());
            }
        } 
        catch (HibernateException e) {
            System.out.println(e.getMessage());
        } 
        catch (Throwable ex) {
            System.err.println("Error." + ex);
            throw new ExceptionInInitializerError(ex);
        } finally {
            if (sesioa != null) {
                sesioa.close();
            }
            return arm;
        }
    }

    public static boolean listagorde(Armak arma) {

        boolean emaitza = false;
        Transaction txt = null;
        int armazkia;
        
        try {
            sesioa = faktorial.openSession();
            txt = sesioa.beginTransaction();
            armazkia = (int)sesioa.save(arma);
            txt.commit();
            System.out.println(armazkia + ". arma ondo gorde da datubasean.");
            emaitza = true;

        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (txt != null) {
                txt.rollback();
            }
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);

        } finally {
            if (sesioa != null) {
                sesioa.close();
            }
            return emaitza;
        }

    }
    
   
    public static boolean armaEzabatu(int id) {
        Session saioa = null;
        Transaction tr = null;

        try {
            saioa = faktorial.openSession();
            tr = saioa.beginTransaction(); // transakzioa hasi
            Armak arma = (Armak) saioa.load(Armak.class, id);
            saioa.delete(arma);
            tr.commit(); // ondo ezabatu bada, transakzioa bukatu
            System.out.println("\n"+ id + "arma ondo ezabatu da.\n");
            return true;
        }
        catch (HibernateException ex) {
            if (tr != null) { 
                tr.rollback(); // transakzioa atzera bota
                System.err.println("Ezin izan da arma ezabatu!\n\n ARRAZOIAK:\n" + ex.getCause());
            }
        }
        finally {
            if (saioa != null) {
                saioa.close(); // saioa itxi
            }
        }
        return false; // ezabatu den edo ez jakiteko, booleano bat bidali
    }
    
    public static void armaDatuakAldatu(Armak arma) {
        Transaction tr = null;
        Session saioa = null;
        
        try {
            saioa = faktorial.openSession();
            tr = saioa.beginTransaction(); // transakzioa hasi
            
            Armak armaObj = (Armak) saioa.get(Armak.class, arma.getId());
            armaObj.setName(arma.getName());
            armaObj.setJatorria(arma.getJatorria());
            armaObj.setDeskribapena(arma.getDeskribapena());
            armaObj.setUrtea(arma.getUrtea());
            
            tr.commit();            
        }
        catch (HibernateException e) {
            System.out.println("Ezin izan da ikaslearen datuak aldatu => " + e.getCause());
            if (tr != null) {
                tr.rollback();
            }
        } finally {
            if (saioa != null) {
                saioa.close();
            }
        }
    }

    
    public static void cerrer(){
        faktorial.close();
    }
    
}
