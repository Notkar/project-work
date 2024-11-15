package com.example.progetto_escursioni.controller;

import com.example.progetto_escursioni.model.DataDisponibile;
import com.example.progetto_escursioni.model.Foto;
import com.example.progetto_escursioni.model.Itinerario;
import com.example.progetto_escursioni.service.ItinerarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/dettaglio")
public class DettaglioController {

    @Autowired
    private ItinerarioService itinerarioService;

    @GetMapping
    public String getPage(
            HttpSession session,
            Model model,
            @RequestParam("id") int idItinerario
    ){
        // creo un oggetto itinerario in base all'id fornito e lo registro nel model
        Itinerario itinerario = itinerarioService.dettaglioItinerario(idItinerario);
        model.addAttribute("itinerario", itinerario);

        // recupero la lista delle foto associate all'itinerario e le registro nel model
        List<Foto> fotoItinerario = itinerario.getFotoItinerario();
        model.addAttribute("fotoItinerario", fotoItinerario);

        // recupero la lista delle date disponibili associate all'itinerario e le registro nel model
        List<DataDisponibile> dateDisponibili = itinerario.getDateDisponibiliItinerario();
        model.addAttribute("dateDisponibili", dateDisponibili);

        // se almeno una data nella lista è valida, allora abilito il pulsante prenotazione (che di base è disabled)
        model.addAttribute("prenotazioneBloccata", true);
        for(DataDisponibile data : dateDisponibili) {
            if (data.getData().isAfter(LocalDate.now())) {
                model.addAttribute("prenotazioneBloccata", false);
            }
        }

        // registro in sessione la pagina corrente, per eventuali tasti "indietro" o per quando fai il login
        session.setAttribute("paginaPrecedente", "dettaglio?id=" + itinerario.getId());

        // recupero utente in sessione se presente e registro sul model questa cosa per poter cambiare scritta di tasto area riservata
        model.addAttribute("utenteLogged", session.getAttribute("utente") != null); // (session.getAttribute("utente") != null ? true : false)

        return "dettaglio";
    }

    // gestione tasto area riservata (x funzionalità di ritorno indietro con login)
    @GetMapping("/toareariservata")
    public String toAreaRiservata(HttpSession session,
                                  @RequestParam("id") int idItinerario){
        // registro in sessione la pagina corrente, per eventuali tasti "indietro" o per quando fai il login
        session.setAttribute("paginaPrecedente", "dettaglio?id=" + itinerarioService.dettaglioItinerario(idItinerario).getId());
        return "redirect:/areariservata";
    }

}
