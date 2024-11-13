package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.dao.PrenotazioneDao;
import com.example.progetto_escursioni.model.Prenotazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrenotazioneServiceImpl implements PrenotazioneService {

    @Autowired
    private PrenotazioneDao prenotazioneDao;

    @Override
    public void salvaPrenotazione(Prenotazione prenotazione) {
        prenotazioneDao.save(prenotazione);
    }

    @Override
    public List<Prenotazione> elencoPrenotazioniUtente(int idUtente) {
        return prenotazioneDao.selectPrenotazioniWhereIdUtente(idUtente);
    }
}
