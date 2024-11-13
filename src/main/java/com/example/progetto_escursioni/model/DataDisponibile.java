package com.example.progetto_escursioni.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "date_disponibili")
public class DataDisponibile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private LocalDate data;

    @Column
    private String ora;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_itinerario", referencedColumnName = "id")
    private Itinerario itinerario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
    }
}
