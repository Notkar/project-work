package com.example.progetto_escursioni.controller;

import com.example.progetto_escursioni.model.Guida;
import com.example.progetto_escursioni.service.GuidaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/guide")
public class GuideController {

    @Autowired
    private GuidaService guidaService;

    @GetMapping
    public String getPage(HttpSession session,
                          Model model) {
        // recupera la lista delle guide
        List<Guida> listaGuide = guidaService.elencoGuide();
        model.addAttribute("listaGuide", listaGuide);

        // recupero utente in sessione se presente e registro sul model questa cosa per poter cambiare scritta di tasto area riservata
        model.addAttribute("utenteLogged", session.getAttribute("utente") != null); // (session.getAttribute("utente") != null ? true : false)

        return "guide";
    }

    // gestione tasto area riservata (x funzionalità di ritorno indietro con login)
    @GetMapping("/toareariservata")
    public String toAreaRiservata(HttpSession session){
        // registro in sessione la pagina corrente, per eventuali tasti "indietro" o per quando fai il login
        session.setAttribute("paginaPrecedente", "guide");
        return "redirect:/areariservata";
    }
}
