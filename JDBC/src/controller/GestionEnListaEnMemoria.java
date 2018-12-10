/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Armak;

/**
 *
 * @author DM3-2-16
 */
  
    public class GestionEnListaEnMemoria {
    
    String dbMota = "mysql";
    String bd = "Armakdb";
    String server = "localhost";
    //String taula = "";
    private String url = "jdbc:mysql://"+server+":3306/"+bd;

    String erabiltzailea = "root";
    String pasahitza = "admin";

    

    public GestionEnListaEnMemoria(String dbMota, String bd,
            String server, String taula,
            String erabiltzailea, String pasahitza) {
        this.dbMota = dbMota;
        this.bd = bd;


        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
    }
    
    public GestionEnListaEnMemoria() {
            
    }
    
    //Mysql datu base motan onektatzeko
    public Connection konexioa() {
        Connection konexioa = null;
        
            try {
            
                konexioa = DriverManager.getConnection(url, erabiltzailea, pasahitza);
            
                
            
            } catch (SQLException ex) {
            
                System.err.println(ex.getMessage());
        } finally {
            return konexioa;
        }
    }
    
    //Pelikulak datubasera gehitzeko
     public boolean listagorde(Armak arma) {
        String sql = "INSERT INTO armak(Name, Jatorria, Deskribapena, Urtea) VALUES(?,?,?,?)";

        try (Connection conn = konexioa();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, arma.getName());
            pstmt.setString(2, arma.getJatorria());
            pstmt.setString(3, arma.getDesk());
            pstmt.setInt(4, arma.getUrtea());
            pstmt.executeUpdate();

            System.out.println("Erregistroa ondo txertatu da datu-basean.");

            return true;
        } catch (SQLException e) {
            return false;
        }

    }
     
     //Pelikulak taulan ikustekopublic ObservableList<Armak> cargaArmas() {

    
     public ObservableList<Armak> cargaArmas() {

        ObservableList<Armak> ObPel = FXCollections.observableArrayList();

        Statement sententzia = null;
        ResultSet kar = null;
        Connection konekzioa = konexioa();
        try {
            sententzia = konekzioa.createStatement();
            kar = sententzia.executeQuery("SELECT * FROM armak");

            while (kar.next()) {
                String izena = kar.getString("Name");
                String zuzendaria = kar.getString("Jatorria");
                String durazioa = kar.getString("Deskribapena");
                int adina = kar.getInt("Urtea");
                Armak pelikula = new Armak(izena, zuzendaria, durazioa, adina);
                ObPel.add(pelikula);
            }
          
            } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        } finally {
            try {
                if (sententzia != null) {
                    sententzia.close();
                }
            } catch (SQLException sqle) {
            }
            return ObPel;
        
        
        }
     }
     
     //Pelikulak datubasetik ezabatzeko
     public boolean armakEzabatu(Armak pel) {
             
             String sql = "DELETE FROM armak WHERE Name=?";
             
              try (Connection conn = konexioa();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pel.getName());
            pstmt.executeUpdate();

            System.out.println("Erregistroa ondo ezabatu da datu-basean.");

            return true;
        } catch (SQLException e) {
            return false;
        }
            
  
         }
     //Pelikulen balioak aldatzeko
     public boolean armakAldatu(Armak pel) {
       
        String sql = "UPDATE armak SET Name=?, Jatorria=?, Deskribapena=?, Urtea=? WHERE Name=?";
        //Statement estatuto = conex.getConnection().createStatement();

        try (Connection conn = konexioa();
                   PreparedStatement pstmt = conn.prepareStatement(sql)) {
                   pstmt.setString(1, pel.getName());
                   pstmt.setString(2, pel.getJatorria());
                   pstmt.setString(3, pel.getDesk());
                   pstmt.setInt(4, pel.getUrtea());
                   pstmt.setString(5, pel.getName());
                   pstmt.executeUpdate();

                   System.out.println("Erregistroa ondo txertatu da datu-basean.");

                   return true;
               } catch (SQLException e) {
                   return false;
               }
     } 
     public String getDbMota() {
        return dbMota;
    }

    public void setDbMota(String dbMota) {
        this.dbMota = dbMota;
    }
    
    public String getBd() {
        return bd;
    }

    public void setBd(String bd) {
        this.bd = bd;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErabiltzailea() {
        return erabiltzailea;
    }

    public void setErabiltzailea(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }
 
}
