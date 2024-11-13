package com.example.progetto_escursioni.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prenotazioni")
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "data_prenotazione")
    private LocalDateTime dataPrenotazione;

    @Column(name = "data_escursione")
    private LocalDate dataEscursione;

    @Column(name = "ora_escursione")
    private String oraEscursione;

    @Column(name = "numero_partecipanti")
    private int numeroPartecipanti;

    @Column(name = "prezzo_totale")
    private double prezzoTotale;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column
    private String email;

    @Column
    private String telefono;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_itinerario", referencedColumnName = "id")
    private Itinerario itinerario;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_utente", referencedColumnName = "id")
    private Utente utente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(LocalDateTime dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public LocalDate getDataEscursione() {
        return dataEscursione;
    }

    public void setDataEscursione(LocalDate dataEscursione) {
        this.dataEscursione = dataEscursione;
    }

    public String getOraEscursione() {
        return oraEscursione;
    }

    public void setOraEscursione(String oraEscursione) {
        this.oraEscursione = oraEscursione;
    }

    public int getNumeroPartecipanti() {
        return numeroPartecipanti;
    }

    public void setNumeroPartecipanti(int numeroPartecipanti) {
        this.numeroPartecipanti = numeroPartecipanti;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
