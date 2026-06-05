import java.io.*;
import java.util.ArrayList;

public class GuildSystem {

    public static void main(String[] args) {

        // Nama file untuk menyimpan data guild
        String namaFile = "guild_data.dat";

        // =========================
        // PROSES SERIALIZATION (SAVE)
        // =========================

        // Membuat ArrayList untuk menampung data member
        ArrayList<Member> daftarMember = new ArrayList<>();

        // Menambahkan 3 member secara manual
        daftarMember.add(new Member("M001", "Andi"));
        daftarMember.add(new Member("M002", "Budi"));
        daftarMember.add(new Member("M003", "Citra"));

        // Menyimpan ArrayList ke file menggunakan ObjectOutputStream
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(namaFile))) {

            // Menulis objek ArrayList ke file
            oos.writeObject(daftarMember);

            System.out.println("Data guild berhasil disimpan!");

        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menyimpan data.");
            e.printStackTrace();
        }

        // =========================
        // PROSES DESERIALIZATION (LOAD)
        // =========================

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(namaFile))) {

            // Membaca objek dari file
            // Perlu downcasting karena readObject() mengembalikan Object
            @SuppressWarnings("unchecked")
            ArrayList<Member> hasilLoad =
                    (ArrayList<Member>) ois.readObject();

            // Menampilkan data hasil load menggunakan foreach
            System.out.println("\nData Member Hasil Load:");

            for (Member member : hasilLoad) {
                System.out.println(
                        "ID: " + member.getId() +
                        " | Nama: " + member.getNama()
                );
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Terjadi kesalahan saat membaca data.");
            e.printStackTrace();
        }
    }
}
