import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * HeroDAO (Data Access Object)
 * Bertanggung jawab untuk semua komunikasi antara aplikasi dan database MySQL
 * terkait data Hero: koneksi, insert, select, dan delete.
 */
public class HeroDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/db_game";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Membuka koneksi ke database MySQL
    public Connection getKoneksi() {
        Connection konek = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            konek = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return konek;
    }

    // Menyimpan data Hero baru ke database
    public boolean simpanHero(String nama, String role, int hp, int damage) {
        String sql = "INSERT INTO hero (nama, role, hp, damage) VALUES (?, ?, ?, ?)";
        try (Connection konek = getKoneksi();
             PreparedStatement pst = konek.prepareStatement(sql)) {

            pst.setString(1, nama);
            pst.setString(2, role);
            pst.setInt(3, hp);
            pst.setInt(4, damage);
            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gagal simpan: " + e.getMessage());
            return false;
        }
    }

    // Mengambil semua data Hero dari database, digunakan oleh loadDataTabel()
    public List<Object[]> ambilSemuaHero() {
        List<Object[]> daftarHero = new ArrayList<>();
        String sql = "SELECT id, nama, role, hp, damage FROM hero";

        try (Connection konek = getKoneksi();
             Statement st = konek.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("role"),
                    rs.getInt("hp"),
                    rs.getInt("damage")
                };
                daftarHero.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Gagal ambil data: " + e.getMessage());
        }

        return daftarHero;
    }

    /**
     * Menghapus satu baris data Hero berdasarkan ID.
     * Dipanggil dari AppViewGUI ketika Admin menekan tombol "Hapus Data".
     *
     * @param idTarget id hero yang dipilih dari JTable
     * @return true jika berhasil dihapus, false jika gagal
     */
    public boolean hapusHero(int idTarget) {
        String sql = "DELETE FROM hero WHERE id = ?";
        try (Connection konek = getKoneksi();
             PreparedStatement pst = konek.prepareStatement(sql)) {

            pst.setInt(1, idTarget);
            int hasil = pst.executeUpdate(); // jumlah baris yang terhapus (0 atau 1)
            return hasil > 0;

        } catch (SQLException e) {
            System.out.println("Gagal hapus: " + e.getMessage());
            return false;
        }
    }
}