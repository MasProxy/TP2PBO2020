/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2_1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;


/**
 *
 * @author Fauzan
 */
public class Proses {
    public static Connection con;
    public static Statement stm;
    
    public void koneksi(){//untuk membuka koneksi ke database
        try {
            String url ="jdbc:mysql://localhost/db_tp2";
            String user="root";
            String pass="";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,user,pass);
            stm = con.createStatement();
            System.out.println("koneksi berhasil;");
        } catch (Exception e) {
            System.err.println("koneksi gagal" +e.getMessage());
        }
    }
    
    public void AddDataBaru(String kode, String NamaBuku, String NamaPeminjam, String TanggalMeminjam, String TanggalPengembalian ){//untuk menambah data peminjaman baru
        try{
            koneksi();
            String sql = "Insert Into peminjaman (kode_buku,nama_buku,Peminjam, Tanggal_Pinjam, Tanggal_Pengembalian) Values ('%s','%s', '%s','%s', '%s')";
            sql = String.format(sql, kode, NamaBuku, NamaPeminjam, TanggalMeminjam, TanggalPengembalian);
            stm.execute(sql);
        } catch (Exception e){
            System.err.println("Penambahan gagal" +e.getMessage());
        }   
    }
    
    public void HapusData(String kode){//untuk menghapus data peminjaman karena sudah dikembalikan
        try{
            koneksi();
            String sql = "Delete from peminjaman where kode_buku = '%s'";
            sql = String.format(sql, kode);
            stm.execute(sql);
        } catch (Exception e){
            System.err.println("Penghapusan gagal gagal" +e.getMessage());
        }   
    }
    
    public int cekkode(String kode){//untuk mengecek kode buku, apakah sudah ada atau belum
        int hasil=0;
        try{
            koneksi();
            
            String sql = "Select * from peminjaman";
            ResultSet res = stm.executeQuery(sql);

            while(res.next()){
                String getKode = res.getString("kode_buku");
                if(getKode.equals(kode)){
                    hasil=1;
                    
                }
            }
        }catch(Exception e){
        }
        
        return hasil;
    }
    
    private int cekBulan(int bln){//untuk mengecek bulan tanggal pengembalian
        int hasil=0;
        if(bln==2){
            hasil=28;
        }else if((bln==1)||(bln==3)||(bln==5)||(bln==7)||(bln==8)||(bln==10)||(bln==12)){
            hasil=31;
        }else{
            hasil = 31;
        }
        return hasil;
    }
    
    private String kalkulasi(int tgl, int bln, int thn, int tambahan){//untuk kalkulasi penambahan durasi hari pengembalian
        String hasil="";
        String tglBaru="";
        String blnBaru="";
        String thnBaru="";
        
        int cekbln = cekBulan(bln); 
        if((tambahan+tgl)>cekbln){
            tglBaru=Integer.toString(tambahan+tgl-cekbln);
            if(bln<12){
                if(bln<9){
                    blnBaru = "0"+Integer.toString(bln+1);
                }else{
                    blnBaru = Integer.toString(bln+1);
                }
                hasil=Integer.toString(thn)+"-"+blnBaru+"-"+tglBaru;
            }else{
                blnBaru = Integer.toString(bln+1-12);
                thnBaru=Integer.toString(thn+1);
                hasil=thnBaru+"-"+blnBaru+"-"+tglBaru;
            }
        }else{
            if((tambahan+tgl)<10){
                tglBaru="0"+Integer.toString(tambahan+tgl);
            }else{
                tglBaru=Integer.toString(tambahan+tgl);
            }
            hasil=Integer.toString(thn)+"-"+Integer.toString(bln)+"-"+tglBaru;
        }
        return hasil;
    }
    
    
    public void perpanjangData(int n, String kode, String tanggal){//untuk memperpanjang peminjaman
        try{
            koneksi();
            String sql = "Update peminjaman SET Tanggal_Pengembalian= '%s' where kode_buku = '%s'";
            String[] pecah = tanggal.split("-");
            String tahun = pecah[0];
            String bulan = pecah[1];
            String tgl = pecah[2];
            
            int bln = Integer.parseInt(bulan);
            int tgl1 = Integer.parseInt(tgl);
            int thn = Integer.parseInt(tahun);
            
            String hasil = kalkulasi(tgl1,bln,thn, n);
            sql = String.format(sql, hasil, kode);
            
            stm.execute(sql);
        } catch (Exception e){
            System.err.println("Penambahan Hari gagal" +e.getMessage());
        }   
    }
    
    
    
    
}
