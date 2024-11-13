package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.model.Guida;

import java.util.List;

public interface GuidaService {

    List<Guida> elencoGuide();
    Guida dettaglioGuida(int idGuida);
}
