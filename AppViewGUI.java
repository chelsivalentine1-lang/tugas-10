import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * AppViewGUI
 * Tampilan utama aplikasi manajemen data Hero.
 * Berisi form input, tombol Simpan, tombol Hapus, dan JTable untuk menampilkan data.
 */
public class AppViewGUI extends JFrame {

    private JTextField txtNama, txtRole, txtHp, txtDamage;
    private JButton btnSimpan;
    private JButton btnHapus = new JButton("Hapus Data");
    private JTable tblHero;
    private DefaultTableModel model;

    private final HeroDAO dao = new HeroDAO();

    public AppViewGUI() {
        setTitle("Aplikasi Manajemen Hero");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- Panel Input ----------
        JPanel panelInput = new JPanel();
        panelInput.setLayout(new FlowLayout());

        txtNama = new JTextField(10);
        txtRole = new JTextField(8);
        txtHp = new JTextField(5);
        txtDamage = new JTextField(5);
        btnSimpan = new JButton("Simpan");

        panelInput.add(new JLabel("Nama:"));
        panelInput.add(txtNama);
        panelInput.add(new JLabel("Role:"));
        panelInput.add(txtRole);
        panelInput.add(new JLabel("HP:"));
        panelInput.add(txtHp);
        panelInput.add(new JLabel("Damage:"));
        panelInput.add(txtDamage);
        panelInput.add(btnSimpan);
        panelInput.add(btnHapus); // Tombol Hapus Data dijejerkan dengan tombol Simpan

        add(panelInput, BorderLayout.NORTH);

        // ---------- Tabel ----------
        model = new DefaultTableModel(new Object[]{"ID", "Nama", "Role", "HP", "Damage"}, 0);
        tblHero = new JTable(model);
        add(new JScrollPane(tblHero), BorderLayout.CENTER);

        // ---------- Listener Tombol Simpan ----------
        btnSimpan.addActionListener(e -> {
            String nama = txtNama.getText();
            String role = txtRole.getText();
            int hp = Integer.parseInt(txtHp.getText());
            int damage = Integer.parseInt(txtDamage.getText());

            boolean sukses = dao.simpanHero(nama, role, hp, damage);
            if (sukses) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                loadDataTabel();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data.");
            }
        });

        // ---------- Listener Tombol Hapus (Lambda Expression) ----------
        btnHapus.addActionListener(e -> {
            // 1. Ambil index baris yang sedang dipilih user di JTable
            int selectedRow = tblHero.getSelectedRow();

            // 2. Validasi: jika tidak ada baris yang diklik, getSelectedRow() akan bernilai -1
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris yang mau dihapus dulu!");
                return;
            }

            // 3. Tarik nilai kolom ID dari baris yang dipilih.
            //    getValueAt() mengembalikan tipe data Object (karena TableModel bersifat generik
            //    dan tidak tahu tipe asli tiap kolom saat runtime).
            //    Oleh karena itu, hasilnya HARUS di-casting secara eksplisit ke (int)
            //    agar bisa dipakai sebagai parameter method hapusHero(int idTarget).
            //    Casting ini aman karena kolom "ID" pada model selalu diisi dengan tipe Integer
            //    (lihat ambilSemuaHero() di HeroDAO yang mengisi rs.getInt("id")).
            int idTarget = (int) model.getValueAt(selectedRow, 0);

            // 4. Kirim ID ke HeroDAO untuk dieksekusi query DELETE ke database
            boolean sukses = dao.hapusHero(idTarget);

            if (sukses) {
                // 5. Refresh tabel supaya baris yang terhapus langsung lenyap dari layar
                loadDataTabel();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus dari Database!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data.");
            }
        });

        loadDataTabel();
    }

    // Mengosongkan lalu mengisi ulang JTable dengan data terbaru dari database
    private void loadDataTabel() {
        model.setRowCount(0); // kosongkan baris lama agar tidak duplikat/stale
        List<Object[]> daftarHero = dao.ambilSemuaHero();
        for (Object[] row : daftarHero) {
            model.addRow(row);
        }
    }

    private void bersihkanForm() {
        txtNama.setText("");
        txtRole.setText("");
        txtHp.setText("");
        txtDamage.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppViewGUI().setVisible(true));
    }
}
