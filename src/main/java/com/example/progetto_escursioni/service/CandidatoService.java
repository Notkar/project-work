package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.model.Candidato;
import com.example.progetto_escursioni.model.Utente;
import org.springframework.web.multipart.MultipartFile;

public interface CandidatoService {

    void salvaCandidato(Candidato candidato);
    boolean controlloCandidato(int idUtente);
    
}
