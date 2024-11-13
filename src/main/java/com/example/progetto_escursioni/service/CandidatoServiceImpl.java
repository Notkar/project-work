package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.dao.CandidatoDao;
import com.example.progetto_escursioni.model.Candidato;
import com.example.progetto_escursioni.model.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
public class CandidatoServiceImpl implements CandidatoService {

    @Autowired
    private CandidatoDao candidatoDao;


    @Override
    public void salvaCandidato(Candidato candidato) {

        candidatoDao.save(candidato);
    }

    @Override
    public boolean controlloCandidato(int idUtente) {

        Candidato candidato = candidatoDao.selectCandidatiWhereIdUtente(idUtente);

        if (candidato != null) {
            return false;
        }

        return true;
    }
}
