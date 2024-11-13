package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.dao.UtenteDao;
import com.example.progetto_escursioni.model.Utente;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private UtenteDao utenteDao;

    @Override
    public void salvaUtente(Utente utente) {
        utenteDao.save(utente);
    }

    @Override
    public Utente dettaglioUtente(int idUtente) {
        Optional<Utente> utenteOptional = utenteDao.findById(idUtente);
        if (utenteOptional.isPresent()) {
            return  utenteOptional.get();
        }
        return null;
    }

    @Override
    public boolean loginUtente(HttpSession session, String username, String password) {
        Utente utente = utenteDao.findByUsernameAndPassword(username, password);

        if (utente != null) {
            session.setAttribute("utente", utente);
            return true;
        }

        return false;
    }

    @Override
    public boolean controlloUsernameEmail(String username, String email) {

        List<Utente> utenti = utenteDao.findByUsernameOrEmail(username, email);

        if (!utenti.isEmpty()) {
            return false;
        }

        return true;
    }
}
