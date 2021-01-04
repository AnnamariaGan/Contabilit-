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
    private static ObservableList<Movimento> mov;
    
    public ArchivioMov(String ip, int porta) { //02
        try{
             co = DriverManager.getConnection("jdbc:mysql://"
                     + ip
                     + ":"
                     + porta
                     + "/contabilita_personale", "root","");
            st = co.createStatement();
        }catch(SQLException e){System.err.println("connessione non riuscita" +
                 e.getMessage() );}
    } 
    
    public void loadMovimento(ObservableList<Movimento> voci,String nomeUtente, LocalDate dataInizio, LocalDate dataFine){ //03
        String queryString = "SELECT *"
                + "FROM movimento"
                + " WHERE nomeutente = ?"
                + "AND data BETWEEN ? AND ?"; 
        try(  
            PreparedStatement prepStat = co.prepareStatement(queryString); //04
        ){
            prepStat.setString(1, nomeUtente); //05
            prepStat.setDate(2, java.sql.Date.valueOf(dataInizio)); 
            prepStat.setDate(3, java.sql.Date.valueOf(dataFine)); 
            
            ResultSet rs = prepStat.executeQuery(); //06
            st.executeQuery(queryString); 
            while(rs.next()) {
                voci.add( new Movimento(rs.getString("data"), rs.getDouble("importo"),
                                        rs.getString("voce"), rs.getString("email"),
                                        rs.getString("tipo"))
                    );
            }
           loadGrafico(nomeUtente,dataInizio , dataFine); 
          }catch (SQLException e) {
              System.err.println(e.getMessage());
          }
    }  
    
     public String calcolaSaldo(String mail){ //07
        try(
          PreparedStatement ps = co.prepareStatement( //08
                "set @entrate = (select sum(importo) as entrate\n" +
                                    "from movimento\n" +
                                    " where nomeutente = ?"
                                           + " and tipo = 'e');\n" +
                         "\n" +
                "set @uscite = (select sum(importo) as uscite\n" +
                                " from movimento \n" +
                                " where nomeutente = ?"
                                        + " and tipo = 'u');\n" +
                        "\n" +
                        "\n" +
                "set @saldo = @entrate - @uscite;\n" +
                        "\n" +
                "select @saldo");  
        ){
            ps.setString(1,mail );
            ps.setString(2,mail );
            
            ResultSet rs = ps.executeQuery(); //09
            System.out.println("rows affected" + rs);
            String sld = Double.toString(rs.getDouble("saldo"));
            return sld;
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        
        return "0.00";
    } 
    
    public void deleteMovimento(Movimento mv,ObservableList<Movimento> voci){ //10
        try( //questa query cancella tutte le righe con quelle proprieta
            PreparedStatement ps = co.prepareStatement("DELETE FROM movimento"  //11
                        + " WHERE nomeutente = ? AND importo = ? AND data = ?"
                        + "AND voce = ? AND tipo = ?"); 
        ){
           ps.setString(1, mv.getEmail());
           ps.setDouble(2, mv.getImporto());
           ps.setDate(3, java.sql.Date.valueOf(mv.getData())); 
           ps.setString(4, mv.getVoce());
           ps.setString(5, mv.getTipo());
          
           System.out.println("rows affected: " + ps.executeUpdate());
           
           LocalDate inizio = LocalDate.now();
           LocalDate fine = LocalDate.now().plusDays(-30);
        //  voci.remove(); //dalla observable list devo pero togliere la voce che ho eliminato
           loadMovimento(voci, mv.getEmail(),inizio, fine ); 
           loadGrafico(mv.getEmail(),inizio , fine);
           calcolaSaldo(mv.getEmail());
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    
    public void setMovimento(Movimento mv,ObservableList<Movimento> voci){ //12
        try( 
          PreparedStatement ps = co.prepareStatement( //13
                  "INSERT INTO movimento(nomeutente, importo, data, voce, tipo)"
                          + " VAlUES(?,?,?,?,?)");  
               
        ){
            ps.setString(1, mv.getEmail()); 
            ps.setDouble(2,mv.getImporto() );
            ps.setDate(3, java.sql.Date.valueOf(mv.getData())); 
            ps.setString(4,mv.getVoce());
            ps.setString(5, mv.getTipo());
            
            System.out.println("rows affected" + ps.executeUpdate());
            
            LocalDate inizio = LocalDate.now();
            LocalDate fine = LocalDate.now().plusDays(-30);
            loadMovimento(voci, mv.getEmail(),inizio, fine );  
            loadGrafico(mv.getEmail(),inizio , fine);
        }catch (SQLException e){System.err.println( e.getMessage());}
    }  
    
     public void loadGrafico(String nomeUtente,   //14
            LocalDate dataInizio, LocalDate dataFine){
        ObservableList<PieChart.Data> ol = FXCollections.observableArrayList();
        String queryString = "SELECT voce, SUM(importo) as totale" //15
                + "FROM movimento"
                + "WHERE nomeutente = ?" 
                + "AND data BETWEEN ? AND ?" 
                + "GROUP BY voce";
        try(
            PreparedStatement ps = co.prepareStatement(queryString);
        ){
           ps.setString(1, nomeUtente);
           ps.setDate(2, java.sql.Date.valueOf(dataInizio)); 
           ps.setDate(3, java.sql.Date.valueOf(dataFine)); 
            
           ResultSet rs = ps.executeQuery();
            while(rs.next()) { //16
                ol.add(new PieChart.Data(rs.getString("voce"), rs.getDouble("totale")));
            }
          }catch (SQLException e) {System.err.println("aggiornamento "
                  + "grafico non riuscito" + e.getMessage());}
    }

}

/*
Note:
01) Classe ArchivioMov che gestisce i dati del database, atteverso l`oggetto Movimento (classe bean).
02) Costruttore di ArchivioMov che crea la connessione con il database, e istanzia la classe 
    statement che produce ResultSet.
03) Metodo loadMovimento, si occupa di caricare un nuovo movimento del databse.
04) Creata la quesry parametrica per l`interrogazione al database.
05) Compilata la PreparedStatament con i parametri che condizionano la query.
06) ResultSet della query. Si scorre poi il ResultSet e aggiunte le voci nella lista passata per argomento.

07) Metodo calcolaSaldo, si occupa di calcolare il saldo.
08) Creata la query parametrica che calcola le entrate e le uscite e ne fa la differenza.
09) Il result set della query è il saldo.

10) Metodo deleteMovimento, si occupa di eliminare dal database un movimento.
11) Creata la query parametrica che elimina il movimento con gli stessi parametri di quello passato per argomento.

12) Metodo che carica nel database un nuovo movimento.
13) Creata la query per inserire il nuovo movimento.

14) Metodo che trova le percetuali nel diagramma a torta.
15) Creata la query che somma per ogni categoria l`ammontare delle spese o degli incassi.
16) Si scorre il Result Set e si aggiunge il risultato alla lista delle categorie del diagramma a torta.
*/