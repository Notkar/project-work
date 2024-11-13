package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.model.Prenotazione;

import java.util.List;

public interface PrenotazioneService {

    void salvaPrenotazione(Prenotazione prenotazione);
    List<Prenotazione> elencoPrenotazioniUtente(int idUtente);
}
