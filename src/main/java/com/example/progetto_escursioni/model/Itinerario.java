package com.example.progetto_escursioni.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itinerari")
public class Itinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String nome;

    @Column
    private String immagine;

    @Column
    private String regione;

    @Column
    private String mappa;

    @Column
    private int distanza;

    @Column
    private int dislivello;

    @Column(name = "durata_media")
    private int durataMedia;

    @Column
    private String descrizione;

    @Column
    private String difficolta;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_guida", referencedColumnName = "id")
    private Guida guida;

    @OneToMany
            (
                    mappedBy = "itinerario",
                    cascade = CascadeType.REMOVE,
                    fetch = FetchType.EAGER,
                    orphanRemoval = true
            )
    private List<Foto> fotoItinerario = new ArrayList<>();

    @OneToMany
            (
                    mappedBy = "itinerario",
                    cascade = CascadeType.REMOVE,
                    fetch = FetchType.LAZY,
                    orphanRemoval = true
            )
    private List<DataDisponibile> dateDisponibiliItinerario = new ArrayList<>();

    @Column
    private double prezzo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getMappa() {
        return mappa;
    }

    public void setMappa(String mappa) {
        this.mappa = mappa;
    }

    public int getDistanza() {
        return distanza;
    }

    public void setDistanza(int distanza) {
        this.distanza = distanza;
    }

    public int getDislivello() {
        return dislivello;
    }

    public void setDislivello(int dislivello) {
        this.dislivello = dislivello;
    }

    public int getDurataMedia() {
        return durataMedia;
    }

    public void setDurataMedia(int durataMedia) {
        this.durataMedia = durataMedia;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }

    public Guida getGuida() {
        return guida;
    }

    public void setGuida(Guida guida) {
        this.guida = guida;
    }

    public List<Foto> getFotoItinerario() {
        return fotoItinerario;
    }

    public void setFotoItinerario(List<Foto> fotoItinerario) {
        this.fotoItinerario = fotoItinerario;
    }

    public List<DataDisponibile> getDateDisponibiliItinerario() {
        return dateDisponibiliItinerario;
    }

    public void setDateDisponibiliItinerario(List<DataDisponibile> dateDisponibiliItinerario) {
        this.dateDisponibiliItinerario = dateDisponibiliItinerario;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }
}
