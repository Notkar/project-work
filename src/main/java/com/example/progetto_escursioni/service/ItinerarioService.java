package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.model.Itinerario;

import java.util.List;

public interface ItinerarioService {

    List<Itinerario> elencoItinerari();
    Itinerario dettaglioItinerario(int idItinerario);
    List<Itinerario> elencoItinerariRegione(String regione);

    // metodi per filtri
    List<Itinerario> filtraPerRegioneDifficoltaOrdinaPer(String regioneSelezionata, String difficolta, String ordinaPerSelezionato);
    List<Itinerario> filtraPerDifficoltaOrdinaPer(String difficolta, String ordinaPerSelezionato);

    // metodo per ricerca per nome
    List<Itinerario> cercaItinerarioPerNomeLike(String ricercaItinerario);
}
