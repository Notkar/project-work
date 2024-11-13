package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.model.Utente;
import jakarta.servlet.http.HttpSession;

public interface UtenteService {

    void salvaUtente(Utente utente);
    Utente dettaglioUtente(int idUtente);
    boolean loginUtente(HttpSession session, String username, String password);
    boolean controlloUsernameEmail(String username, String email);
}
