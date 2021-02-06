package contabilità;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class ArchivioMov { //01
    private static Connection co;
    private static Statement st;
    //private static ObservableList<Movimento> mov;
    
    public ArchivioMov(String ip, int porta) { //02
        try{
            co = DriverManager.getConnection("jdbc:mysql://"
                    + ip
                    + ":"
                    + porta
                    + "/contabilita", "root","");
            if (co == null) {
                System.err.println("in ArchivioMov.java.ArchivioMov co is null");
                return;
            }
            
            st = co.createStatement();
        }catch(SQLException e){
            System.err.println(
                "connessione non riuscita" +
                e.getMessage()
            );
        }
    } 
    
    public void loadMovimento(ObservableList<Movimento> voci, String nomeUtente, 
        LocalDate dataInizio, LocalDate dataFine, ObservableList<PieChart.Data> ol) { //03
        if (co == null) {
            System.err.println("in ArchivioMov.java.loadMovimento co is null");
            return;
        }
        
        String queryStr = "SELECT * FROM movimento WHERE nomeUtente = ? "
                + " AND data BETWEEN ? AND ? ;";
               
        try(PreparedStatement prepStatm = co.prepareStatement(queryStr);) {  //04
            System.out.println(nomeUtente);
            prepStatm.setString(1, nomeUtente); //05
            prepStatm.setDate(2, java.sql.Date.valueOf(dataInizio)); 
            prepStatm.setDate(3, java.sql.Date.valueOf(dataFine)); 
            
            ResultSet rs = prepStatm.executeQuery(); //06
            while(rs.next()) {
                voci.add(
                    new Movimento(rs.getString("data"), rs.getDouble("importo"),
                        rs.getString("voce"), rs.getString("nomeutente"), 
                        rs.getString("tipo")
                    )
                );
            }
            System.out.println("loadMovimento succesful");
            
            loadGrafico(nomeUtente, dataInizio, dataFine, ol);
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }   
    
     public String calcolaSaldo(String mail){ //07
        if (co == null) {
            System.err.println("in ArchivioMov.java.calcolaSaldo co is null");
            return "";
        }
         
        try(PreparedStatement ps = co.prepareStatement( //08
                "SELECT tipo, SUM(importo) as partial" 
                    + " from movimento " 
                    + " where nomeutente = ? "
                    + " GROUP BY tipo ORDER BY tipo"
            );        
        ){
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery(); //09
            System.out.println("calcola saldo:");// + rs.getString("saldo"));
            Double sld = 0.0;
            
            while (rs.next()) {
                if (rs.getString("tipo").equals("e")) {
                    sld += rs.getDouble("partial");
                } else {
                    sld -= rs.getDouble("partial");
                }              
            }
            
            String sldStr = sld.toString();
            System.out.println(sldStr);
            return sldStr;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            return "50/D1";             ///////////////////////////////////////////////////////////
        }
    } 
    
    public void deleteMovimento(Movimento mv,ObservableList<Movimento> voci,  ///////////////////////////////////////////////////
             ObservableList<PieChart.Data> ol) { //10                      // qua non mi serve passare voici e ol mhua
        if (co == null) {
            System.err.println("in ArchivioMov.java.deleteMovimento co is null");
            return;
        }
        
        try(PreparedStatement ps = co.prepareStatement(
                "DELETE FROM movimento WHERE nomeutente = ? AND importo = ? "
                + "AND data = ? AND voce = ? AND tipo = ? LIMIT 1;"); //11
        ){
           ps.setString(1, mv.getEmail());
           ps.setDouble(2, mv.getImporto());
           ps.setDate(3, java.sql.Date.valueOf(mv.getData())); 
           ps.setString(4, mv.getVoce());
           ps.setString(5, mv.getTipo());
          
           System.out.println("rows affected: " + ps.executeUpdate());
           
           //loadMovimento(voci, mv.getEmail(),inizio, fine, ol);// pieChart); 
           //loadGrafico(mv.getEmail(),inizio , fine, ol);//pieChart);
           //calcolaSaldo(mv.getEmail());
        } catch (SQLException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setMovimento(Movimento mv, ObservableList<Movimento> voci, 
            ObservableList<PieChart.Data> ol) { //12
        if (co == null) {
            System.err.println("in ArchivioMov.java.setMovimento co is null");
            return;
        }
        
        try(PreparedStatement ps = co.prepareStatement( //13
                "INSERT INTO movimento(nomeutente, importo, data, voce, tipo)"
                + " VAlUES(?,?,?,?,?);");     
        ){
            ps.setString(1, mv.getEmail()); 
            ps.setDouble(2,mv.getImporto() );
            ps.setDate(3, java.sql.Date.valueOf(mv.getData())); 
            ps.setString(4,mv.getVoce());
            ps.setString(5, mv.getTipo());
            
            System.out.println("rows affected" + ps.executeUpdate());
        } catch (SQLException e){
            System.err.println( e.getMessage());
            e.printStackTrace();
        }
    }  
    
     public void loadGrafico(String nomeUtente,LocalDate dataInizio, 
            LocalDate dataFine, ObservableList<PieChart.Data> ol){ //14
        if (co == null) {
            System.err.println("in ArchivioMov.java.loadGrafico co is null");
            return;
        }
         
        ObservableList<PieChart.Data> olNew = 
                FXCollections.observableArrayList();
        String queryString = "SELECT voce, SUM(importo) as totale " //15
                + " FROM movimento "
                + " WHERE nomeutente = ? " 
                + " AND data BETWEEN ? AND ? " 
                + " GROUP BY voce;";
       
        try(PreparedStatement ps = co.prepareStatement(queryString); ){
           ps.setString(1, nomeUtente);
           ps.setDate(2, java.sql.Date.valueOf(dataInizio)); 
           ps.setDate(3, java.sql.Date.valueOf(dataFine)); 
            
           ResultSet rs = ps.executeQuery();
           while(rs.next()) { //16
              olNew.add(new PieChart.Data(rs.getString("voce"), rs.getDouble("totale")));
           }
           
           ol.setAll(olNew);
        }catch (SQLException e) {
            System.err.println("aggiornamento grafico non riuscito " + e.getMessage() + " ");
            e.printStackTrace();
        }
    }

}

/*
Note:
01) Classe ArchivioMov che gestisce i dati del database, atteverso l`oggetto Movimento (classe bean).
02) Costruttore di ArchivioMov che crea la connessione con il database, e istanzia la classe 
    statement che produce ResultSet.
03) Metodo loadMovimento, si occupa di caricare un nuovo movimento del databse.
04) Creata la query parametrica per l`interrogazione al database.
05) Compilata la PreparedStatament con i parametri che condizionano la query.
06) ResultSet della query. Si scorre poi il ResultSet e aggiunte le voci nella lista passata per argomento.

07) Metodo calcolaSaldo, si occupa di calcolare il saldo.
08) Creata la query parametrica.
09) Il result set della query è il saldo.

10) Metodo deleteMovimento, si occupa di eliminare dal database un movimento.
11) Creata la query parametrica che elimina il movimento con gli stessi parametri di quello passato per argomento.

12) Metodo che carica nel database un nuovo movimento.
13) Creata la query per inserire il nuovo movimento.

14) Metodo che trova le percetuali nel diagramma a torta.
15) Creata la query che somma per ogni categoria l`ammontare delle spese o degli incassi.
16) Si scorre il Result Set e si aggiunge il risultato alla lista delle categorie del diagramma a torta.
*/