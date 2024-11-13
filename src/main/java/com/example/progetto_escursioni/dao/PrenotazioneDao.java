package com.example.progetto_escursioni.dao;

import com.example.progetto_escursioni.model.Prenotazione;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrenotazioneDao extends CrudRepository<Prenotazione, Integer> {

    @Query(
            value = "SELECT * FROM prenotazioni WHERE id_utente=:i",
            nativeQuery = true
    )
    List<Prenotazione> selectPrenotazioniWhereIdUtente(@Param("i") int idUtente);
}
