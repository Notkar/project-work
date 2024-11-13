package com.example.progetto_escursioni.dao;

import com.example.progetto_escursioni.model.Candidato;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CandidatoDao extends CrudRepository<Candidato, Integer> {

    @Query(
            value = "SELECT * FROM candidati WHERE id_utente=:i",
            nativeQuery = true
    )
    Candidato selectCandidatiWhereIdUtente(@Param("i") int idUtente);
}
