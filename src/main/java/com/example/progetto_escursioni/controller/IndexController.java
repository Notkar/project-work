package com.example.progetto_escursioni.controller;

import com.example.progetto_escursioni.model.Itinerario;
import com.example.progetto_escursioni.service.ItinerarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private ItinerarioService itinerarioService;

    @GetMapping
    public String getPage(
            HttpSession session,
            Model model
    ){
        // creo lista di itinerari per il carosello a fine pagina
        List<Itinerario> caroselloItinerari = itinerarioService.elencoItinerari();
        Collections.shuffle(caroselloItinerari);
        // registro in model per thymeleaf
        model.addAttribute("caroselloItinerari", caroselloItinerari.subList(0,3));

        // recupero utente in sessione se presente e registro sul model questa cosa per poter cambiare scritta di tasto area riservata
        model.addAttribute("utenteLogged", session.getAttribute("utente") != null); // (session.getAttribute("utente") != null ? true : false)

        // DA RICONTROLLARE utenteLogged

        // return pagina
        return "index";
    }

    // gestione tasto area riservata (x funzionalità di ritorno indietro con login)
    @GetMapping("/toareariservata")
    public String toAreaRiservata(HttpSession session){
        // registro in sessione la pagina corrente, per eventuali tasti "indietro" o per quando fai il login
        session.setAttribute("paginaPrecedente", "");
        return "redirect:/areariservata";
    }

    // per gestire alcuni potenziali errori di richiesta
    @GetMapping("/null")
    public String nullRedirectToHome(){
        return "redirect:/";
    }

}