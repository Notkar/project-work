package com.example.progetto_escursioni.dao;

import com.example.progetto_escursioni.model.Itinerario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ItinerarioDao extends CrudRepository<Itinerario, Integer> {
    List<Itinerario> findByRegione(String regione);

    // filtro per regione & difficoltà + ordino per x (distanza, durata_media, dislivello, prezzo) tramite sort
    @Query(
            value = "SELECT * FROM itinerari WHERE regione=:r AND difficolta IN(:d1, :d2, :d3)",
            nativeQuery = true
    )
    List<Itinerario> selectItinerariWhereRegioneAndDifficoltaOrderBy(@Param("r") String regioneSelezionata, @Param("d1") String difficoltaUno, @Param("d2") String difficoltaDue, @Param("d3") String difficoltaTre, Sort sort);

    // filtro per ifficoltà + ordino per x (distanza, durata_media, dislivello, prezzo) tramite sort
    @Query(
            value = "SELECT * FROM itinerari WHERE difficolta IN(:d1, :d2, :d3)",
            nativeQuery = true
    )
    List<Itinerario> selectItinerariWhereDifficoltaOrderBy(@Param("d1") String difficoltaUno, @Param("d2") String difficoltaDue, @Param("d3") String difficoltaTre, Sort sort);

    // filtro per ricerca per nome itinerario
    List<Itinerario> findByNomeContaining(String ricercaItinerario);

}
