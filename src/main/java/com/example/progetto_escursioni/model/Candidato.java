package com.example.progetto_escursioni.model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Entity
@Table(name = "candidati")
public class Candidato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String foto;
    @Column
    private String descrizione;
    @Column
    private String telefono;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_utente", referencedColumnName = "id")
    private Utente utente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(MultipartFile foto) {

        try {
            String formato = foto.getContentType();

            String fotoCodificata = "data:" + formato + ";base64," + Base64.getEncoder().encodeToString(foto.getBytes());

            this.foto = fotoCodificata;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
