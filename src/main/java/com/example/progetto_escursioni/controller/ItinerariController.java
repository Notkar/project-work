package com.example.progetto_escursioni.controller;

import com.example.progetto_escursioni.model.Itinerario;
import com.example.progetto_escursioni.service.ItinerarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/itinerari")
public class ItinerariController {

    List<Itinerario> listaTuttiItinerari;
    List<Itinerario> itinerariVisualizzati;

    @Autowired
    private ItinerarioService itinerarioService;

    @GetMapping
    public String getPage(
            HttpSession session,
            Model model,
            // questi @RequestParam sono per poi salvare i filtri selezionati tra una ricerca e un'altra
            @RequestParam(name = "regione", required = false) String regioneSelezionata,
            @RequestParam(name = "difficolta", required = false) String difficoltaSelezionata,
            @RequestParam(name = "ordinaPer", required = false) String ordinaPerSelezionato){

        // recupero lista tutti itinerari per stampare opzioni di datalist per suggerimento di ricerca
        listaTuttiItinerari = itinerarioService.elencoItinerari();
        model.addAttribute("listaTuttiItinerari", listaTuttiItinerari);

        // per visualizzare itinerari da filtri / ricerca
        if(session.getAttribute("itinerariVisualizzatiDaRicerca") != null) {
            itinerariVisualizzati = (List<Itinerario>) session.getAttribute("itinerariVisualizzatiDaRicerca");
            session.removeAttribute("itinerariVisualizzatiDaRicerca");
        } else {
            itinerariVisualizzati = itinerarioService.elencoItinerari();
        }
        model.addAttribute("itinerariVisualizzati", itinerariVisualizzati);

        // recupero utente in sessione se presente e registro sul model questa cosa per poter cambiare scritta di tasto area riservata
        model.addAttribute("utenteLogged", session.getAttribute("utente") != null); // (session.getAttribute("utente") != null ? true : false)

        // per salvare i filtri selezionati tra una ricerca e un'altra li aggiungo al model e poi faccio dei controlli lato thymeleaf usando th:selected o th:checked
        // tendenzialmente non occorrono controlli perché il metodo #strings.equals() di thymeleaf si occupa già da solo del caso in cui l'argomento è null
        model.addAttribute("regioneSelezionata", regioneSelezionata);
        // per difficoltaSelezionata occorre fare attenzione perché si usa #strings.contains() che con un argomento null lancia un'eccezione
        if (difficoltaSelezionata == null || difficoltaSelezionata.equals("null")) { // controllo per quando difficoltaSelezionata è null (= quando premiamo su "Itinerari" nella navbar) e per quando è una stringa di valore "null" (= quando abbiamo usato il select in index.html)
            model.addAttribute("difficoltaSelezionata", "facile,media,difficile"); // in quei casi vogliamo fare che sono selezionate tutte le opzioni di difficoltà (stato di default del form)
        } else {
            model.addAttribute("difficoltaSelezionata", difficoltaSelezionata); // altrimenti solo le opzioni che sono state selezionate durante l'ultima volta che si sono usati i filtri
        }
        model.addAttribute("ordinaPerSelezionato", ordinaPerSelezionato);

        return "itinerari";
    }

    @PostMapping
    public String filtraRisultati(
            HttpSession session,
            Model model,
            @RequestParam("regione") String regione,
            @RequestParam(name = "difficolta", required = false) String difficolta,
            @RequestParam(name = "ordinaPer", required = false) String ordinaPer,
            @RequestParam(name = "fromHome", required = false) String fromHome
    ){

        if(regione.equals("tutte-regioni")){
            itinerariVisualizzati = (fromHome == null) ? itinerarioService.filtraPerDifficoltaOrdinaPer(difficolta, ordinaPer) : itinerarioService.elencoItinerari(); // se l'utente viene dalla home vuol dire che ha usato il select lì presente, che presenta solo un filtro per regione
        } else {
            itinerariVisualizzati = (fromHome == null) ? itinerarioService.filtraPerRegioneDifficoltaOrdinaPer(regione, difficolta, ordinaPer) : itinerarioService.elencoItinerariRegione(regione);
        }

        // aggiungo a session per recuperarlo nel GetMapping principale
        session.setAttribute("itinerariVisualizzatiDaRicerca", itinerariVisualizzati);

        return "redirect:/itinerari?regione=" + regione + "&difficolta=" + difficolta + "&ordinaPer=" + ordinaPer;
    }

    @PostMapping("/ricerca")
    public String ricercaRisultati(
            Model model,
            HttpSession session,
            @RequestParam("ricercaItinerario") String ricercaItinerario
    ){
        // recupero lista di itinerari in base alla ricerca e la registro in session per recuperarlo nel GetMapping principale
        itinerariVisualizzati = itinerarioService.cercaItinerarioPerNomeLike(ricercaItinerario);
        session.setAttribute("itinerariVisualizzatiDaRicerca", itinerariVisualizzati);

        return "redirect:/itinerari";
    }

    // gestione tasto area riservata (x funzionalità di ritorno indietro con login)
    @GetMapping("/toareariservata")
    public String toAreaRiservata(HttpSession session){
        // registro in sessione la pagina corrente, per eventuali tasti "indietro" o per quando fai il login
        session.setAttribute("paginaPrecedente", "itinerari");
        return "redirect:/areariservata";
    }
}
