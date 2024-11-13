package com.example.progetto_escursioni.service;

import com.example.progetto_escursioni.dao.GuidaDao;
import com.example.progetto_escursioni.model.Guida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuidaServiceImpl implements GuidaService {

    @Autowired
    private GuidaDao guidaDao;

    @Override
    public List<Guida> elencoGuide() {
        return (List<Guida>) guidaDao.findAll();
    }

    @Override
    public Guida dettaglioGuida(int idGuida) {

        Optional<Guida> guidaOptional = guidaDao.findById(idGuida);
        if (guidaOptional.isPresent()) {
            return  guidaOptional.get();
        }
        return null;
    }
}
