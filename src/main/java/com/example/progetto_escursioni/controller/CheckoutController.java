package com.example.progetto_escursioni.controller;

import com.example.progetto_escursioni.model.*;
import com.example.progetto_escursioni.service.ItinerarioService;
import com.example.progetto_escursioni.service.PrenotazioneService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private ItinerarioService itinerarioService;

    @GetMapping
    public String getPage(HttpSession session,
                          Model model,
                          @RequestParam(name = "id", required = false) Integer idItinerario) {

        // se l'utente modifica l'URL e accede a questa pagina senza che ci sia l'id di un itinerario, viene reindirizzato alla home
        if(idItinerario == null) {
            return "redirect:/";
        }

        // check per vedere se l'utente è loggato o meno, nel caso reindirizza alla pagina di login
        if (session.getAttribute("utente") != null) {
            // recupera l'utente salvato in sessione e lo registra nel model
            Utente utente = (Utente) session.getAttribute("utente");
            model.addAttribute("utente", utente);

            // recupera l'itinerario in base al parametro di id e lo registra nel model
            Itinerario itinerario = itinerarioService.dettaglioItinerario(idItinerario);
            model.addAttribute("itinerario", itinerario);

            // recupera la lista di date disponibili avendo a disposizione l'itinerario recuperato sopra e registra nel model per futura iterazione
            List<DataDisponibile> dateDisponibili = itinerario.getDateDisponibiliItinerario();
            List<DataDisponibile> dateDisponibiliValide = new ArrayList<>();
            for(DataDisponibile data : dateDisponibili) {
                if (data.getData().isAfter(LocalDate.now())) {
                    dateDisponibiliValide.add(data);
                }
            }
            model.addAttribute("dateDisponibili", dateDisponibiliValide);

            return "checkout";
        }
        else {
            // registro in sessione la pagina corrente, per eventuali tasti "indietro" o per quando fai il login
            session.setAttribute("paginaPrecedente", "checkout?id=" + itinerarioService.dettaglioItinerario(idItinerario).getId());

            return "redirect:/loginregistrazione";
        }
    }

    // gestisce la richiesta dal javascript per la modifica del prezzo totale dell'ordine in base al numero dei partecipanti
    @PostMapping("/modificaPrezzo")
    @ResponseBody
    public String modificaPrezzo(
            @RequestParam("id") int idItinerario,
            @RequestParam("partecipanti") int partecipanti){
        // recupero un oggetto itinerario in base all'id fornito, per poter poi acquisire il prezzo base per persona
        Itinerario itinerario = itinerarioService.dettaglioItinerario(idItinerario);

        // per formattare il prezzo
        DecimalFormat format = new DecimalFormat("0.00");
        format.setRoundingMode((RoundingMode.HALF_EVEN));

        // ritorno una stringa con la struttura (€ + prezzo * numero di partecipanti); la parte numerica di prezzo è opportunamente formattata con format()
        return format.format((itinerario.getPrezzo()*partecipanti)) + "€";
    }

    // gestisce l'invio della prenotazione (submit)
    @PostMapping("/invia")
    public String invioPrenotazione(HttpSession session,
                                    @RequestParam("numeroPartecipanti") int numeroPartecipanti,
                                    @RequestParam("totale") String totaleString,
                                    @RequestParam("idItinerario") int idItinerario,
                                    @RequestParam("nomeCompleto") String nomeCompleto,
                                    @RequestParam("email") String email,
                                    @RequestParam("telefono") String telefono,
                                    @RequestParam("dataOraScelta") String dataOraScelta){
        // creo un oggetto prenotazione vuoto
        Prenotazione prenotazione = new Prenotazione();
        // recupero l'oggetto utente dalla sessione (perché per prenotare l'utente dev'essere loggato)
        Utente utente = (Utente) session.getAttribute("utente");
        // recupero l'oggetto itinerario a partire dall'id, che è presente nel form in un campo hidden
        Itinerario itinerario = itinerarioService.dettaglioItinerario(idItinerario);

        LocalDateTime dataPrenotazione = LocalDateTime.now(); // la data in cui viene effettuata la prenotazione è la data attuale
        double prezzoTotale = Double.parseDouble(totaleString.replace("€","").replace(",",".")); // per semplicità nel form il campo del totale è un type text (così possiamo scriverci direttamente "€" accanto); per salvare solo la parte numerica dobbiamo prima togliere € e poi sostituire la virgola con il punto (perché Java usa il punto per i decimali)
        String[] dataOraArray = dataOraScelta.split(" - ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataEscursione = LocalDate.parse(dataOraArray[0], formatter);
        String oraEscursione = dataOraArray[1];

        // inizializzo le variabili dell'oggetto prenotazione con i valori recuperati
        prenotazione.setDataPrenotazione(dataPrenotazione);
        prenotazione.setDataEscursione(dataEscursione);
        prenotazione.setOraEscursione(oraEscursione);
        prenotazione.setNumeroPartecipanti(numeroPartecipanti);
        prenotazione.setPrezzoTotale(prezzoTotale);
        prenotazione.setNomeCompleto(nomeCompleto);
        prenotazione.setEmail(email);
        prenotazione.setTelefono(telefono);
        prenotazione.setItinerario(itinerario);
        prenotazione.setUtente(utente);

        // salvo la prenotazione sul database
        prenotazioneService.salvaPrenotazione(prenotazione);

        return "redirect:/areariservata";
    }

    @GetMapping("/indietro")
    public String tornaIndietro(HttpSession session,
                                @RequestParam("id") int idItinerario) {
        // registro in sessione la pagina corrente, per eventuali tasti "indietro" o per quando fai il login
        session.setAttribute("paginaPrecedente", "dettaglio?id=" + itinerarioService.dettaglioItinerario(idItinerario).getId());

        return "redirect:/" + session.getAttribute("paginaPrecedente");
    }
}
