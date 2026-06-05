import java.io.Serializable;

// Kelas Member harus Serializable agar objek dapat disimpan ke file
public class Member implements Serializable {

    // Atribut member
    private String id;
    private String nama;

    // Constructor untuk mengisi data member
    public Member(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    // Getter ID
    public String getId() {
        return id;
    }

    // Getter Nama
    public String getNama() {
        return nama;
    }
}