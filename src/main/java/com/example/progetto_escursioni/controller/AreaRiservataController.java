package com.example.progetto_escursioni.controller;

import com.example.progetto_escursioni.model.Candidato;
import com.example.progetto_escursioni.model.Prenotazione;
import com.example.progetto_escursioni.model.Utente;
import com.example.progetto_escursioni.service.CandidatoService;
import com.example.progetto_escursioni.service.PrenotazioneService;
import com.example.progetto_escursioni.service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/areariservata")
public class AreaRiservataController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping
    public String getPage(HttpSession session,
                          Model model,
                          @RequestParam(name = "candidatura", required = false) String successoCandidatura,
                          @RequestParam(name = "successoModifica", required = false) String successoModifica) {

        // check per vedere se c'è un utente in sessione, altrimenti fa redirect a loginregistrazione
        if (session.getAttribute("utente") != null) {
            // recupero utente da session e lo salvo nel model per stamparne le variabili tramite thymeleaf
            Utente utente = (Utente) session.getAttribute("utente");
            model.addAttribute("utente", utente);
            // recupero la data di nascita dell'utente e la salvo opportunamente formattata nel model per il form di modifica dati
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dataNascitaFormattataPerCampo = utente.getDataNascita().format(pattern);
            model.addAttribute("dataNascitaFormattata", dataNascitaFormattataPerCampo);

            // creo oggetto candidato per poterci fare il binding con il form per le candidature
            Candidato candidato = new Candidato();
            model.addAttribute("candidato", candidato);

            // recupero la lista di prenotazioni associate all'utente e la salvo nel model per iterare con thymeleaf
            List<Prenotazione> prenotazioni = prenotazioneService.elencoPrenotazioniUtente(utente.getId());
            model.addAttribute("prenotazioni", prenotazioni);

            // messaggi di successo o errore per candidatura
            if(successoCandidatura != null && successoCandidatura.equals("true")){
                model.addAttribute("messaggio", "Candidatura inviata con successo: ti risponderemo al più presto!");
            } else if (successoCandidatura != null && successoCandidatura.equals("false")){
                model.addAttribute("messaggio", "Non puoi inviare un'altra candidatura!"); // caso che non dovrebbe essere possibile, lasciato per sicurezza
            }

            // messaggi di successo o errore per modifica dati utente
            if(successoModifica != null && successoModifica.equals("true")){
                model.addAttribute("messaggio", "Modifica dati utente effettuata con successo!");
            } else if (successoModifica != null && successoModifica.equals("false")){
                model.addAttribute("messaggio", "Modifica dati utente fallita: username o email già in uso.");
            }

            // se c'è una candidatura associata all'utente registro nel model una stringa per far comparire lo script per disabilitare il tasto di candidatura
            if(!candidatoService.controlloCandidato(utente.getId())) {
                // se non è vero che non ha trovato un candidato = se è vero che ha trovato un candidato...
                model.addAttribute("candidaturaBloccata", true);
            } else {
                // altrimenti se non ha trovato un candidato...
                model.addAttribute("candidaturaBloccata", false);
            }

            // recupero utente in sessione se presente e registro sul model questa cosa per poter cambiare scritta di tasto area riservata
            model.addAttribute("utenteLogged", session.getAttribute("utente") != null); // (session.getAttribute("utente") != null ? true : false)

            return "areariservata";
        }
        else {
            return "redirect:/loginregistrazione";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("utente");
        return "redirect:/" + session.getAttribute("paginaPrecedente");
    }

    @PostMapping("/candidati")
    public String candidatiManager(@ModelAttribute("candidato") Candidato candidato,
                                   HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");

        if (candidatoService.controlloCandidato(utente.getId())) {
            candidato.setUtente(utente);
            candidatoService.salvaCandidato(candidato);
            return "redirect:/areariservata?candidatura=true";
        }
        else {
            return "redirect:/areariservata?candidatura=false";
        }
    }

    @PostMapping("/controlloPassword")
    @ResponseBody
    public boolean controllaPassword(@RequestParam("password") String inputPassword,
                                     HttpSession session) {
        // recupero utente da sessione (c'è sempre perché siamo in area riservata)
        Utente utente = (Utente)session.getAttribute("utente");
        // ritorno true o false a seconda che l'input dell'utente sia uguale o meno alla password recuperata con il metodo getter getPassword()
        return inputPassword.equals(utente.getPassword());
    }

    @PostMapping("/modificaDati")
    public String modificaDati(@RequestParam("dataNascita") String dataNascitaString,
                               Model model,
                               @ModelAttribute("utente") Utente utente,
                               HttpSession session) {
        Utente utenteDaSession = (Utente)session.getAttribute("utente"); // recuperiamo l'utente pre-modifiche dalla session

        boolean usernameIdentico = utente.getUsername().equals(utenteDaSession.getUsername());
        boolean emailIdentica = utente.getEmail().equals(utenteDaSession.getEmail());

        if(!(usernameIdentico && emailIdentica) && usernameIdentico) {
            if(!utenteService.controlloUsernameEmail("", utente.getEmail())){
                System.out.println("username uguale: controllo email fallito");
                return "redirect:/areariservata?successoModifica=false";
            }
        } else if(!(usernameIdentico && emailIdentica) && emailIdentica) {
            if(!utenteService.controlloUsernameEmail(utente.getUsername(), "")){
                System.out.println("email uguale: controllo username fallito");
                return "redirect:/areariservata?successoModifica=false";
            }
        } else if(!usernameIdentico && !emailIdentica) {
            if(!utenteService.controlloUsernameEmail(utente.getUsername(), utente.getEmail())){
                System.out.println("username & email uguali: controllo username o email fallito");
                return "redirect:/areariservata?successoModifica=false";
            }
        }

        System.out.println("controlli passati");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataNascita = LocalDate.parse(dataNascitaString, formatter);
        utente.setDataNascita(dataNascita);

        utenteService.salvaUtente(utente); // se è tutto ok salva l'utente sul database
        session.setAttribute("utente", utente); // sovrascriviamo l'utente in session con l'utente appena modificato

        return "redirect:/areariservata?successoModifica=true";
    }

}
