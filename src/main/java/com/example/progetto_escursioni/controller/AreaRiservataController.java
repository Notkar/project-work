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
            // recupero la data di nascita dell'utente e la salvo opportunamente formattata nel model per il form di modifica dati (ci sono problemi a stamparla in automatico con il binding in un <input type="date">)
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
            // quando non c'è un utente in session si viene reindirizzati alla pagina di login/registrazione
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

    // per la conferma della password, prima di poter modificare i propri dati utente
    @PostMapping("/controlloPassword")
    @ResponseBody
    public boolean controllaPassword(@RequestParam("password") String inputPassword,
                                     HttpSession session) {
        // recupero utente da sessione (c'è sempre perché siamo in area riservata)
        Utente utente = (Utente)session.getAttribute("utente");
        // ritorno true o false a seconda che l'input dell'utente sia uguale o meno alla password recuperata con il metodo getter getPassword()
        return inputPassword.equals(utente.getPassword());
    }

    // controlli per evitare che si stia cercando di impostare uno username o una email già in uso quando si modificano i propri dati utente
    // nel javascript si ha questa sequenza: submit -> blocco submit -> invio richiesta a questo PostMapping tramite jquery per controllo username & email -> se ritorna "ok" procedo con il submit (PostMapping /modificaDati), altrimenti mostro un errore sul form con reportValidity() per il campo colpevole
    @PostMapping("/checkUsernameEmail")
    @ResponseBody
    public String checkUsernameEmail(@RequestParam("username") String username,
                                     @RequestParam("email") String email,
                                     HttpSession session) {

        Utente utenteDaSession = (Utente)session.getAttribute("utente"); // recuperiamo l'utente dalla session (è l'utente pre-modifiche)

        boolean usernameIdentico = username.equals(utenteDaSession.getUsername());
        boolean emailIdentica = email.equals(utenteDaSession.getEmail());

        // abbiamo 3 casi diversi:
        // 1. quando si modifica solo l'email
        // 2. quando si modifica solo l'username
        // 3. quando si modificano entrambi

        if(!(usernameIdentico && emailIdentica) && usernameIdentico) {
            if(!utenteService.controlloUsernameEmail("", email)){ // riciclo la funzione che controlla username e email al momento della registrazione: basta che imposti "" (stringa vuota) per il parametro che non voglio controllare. sul database verrà lanciata una query con sintassi SELECT * FROM `utenti` WHERE username='' or email ='mario@rossi.it';
                // caso username non modificato: controllo email fallito
                return "email"; // il return in caso di controllo fallito comunica quale dei due campi non va bene
            }
        } else if(!(usernameIdentico && emailIdentica) && emailIdentica) {
            if(!utenteService.controlloUsernameEmail(username, "")){
                // caso email non modificata: controllo username fallito
                return "username";
            }
        } else if(!usernameIdentico && !emailIdentica) { // quando sia email che username sono stati modificati, controlliamo prima uno poi l'altro (dobbiamo controllare entrambi)
            if(!utenteService.controlloUsernameEmail("", email)){
                // caso username modificato: controllo email fallito
                return "email";
            }
            if(!utenteService.controlloUsernameEmail(username, "")){
                // caso email modificata: controllo username fallito
                return "username";
            }
        }

        return "ok"; // return per comunicare che tutti i controlli sono passati con successo

    }

    @PostMapping("/modificaDati")
    public String modificaDati(@RequestParam("dataNascita") String dataNascitaString,
                               Model model,
                               @ModelAttribute("utente") Utente utente,
                               HttpSession session) {

        // uso il metodo setter per assegnare manualmente l'ultima variabile che non è stata ri-assegnata all'oggetto utente recuperato dal model
        // questo perché l'<input type="date"> creava problemi con l'autoriempimento del form grazie al binding, quindi è stato escluso dal binding
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataNascita = LocalDate.parse(dataNascitaString, formatter);
        utente.setDataNascita(dataNascita);

        utenteService.salvaUtente(utente); // salvo l'utente sul database -> dato che l'utente ha lo stesso id di un utente già presente sul database, il metodo sovrascrive l'utente invece di inserirne uno nuovo
        session.setAttribute("utente", utente); // sovrascriviamo anche l'utente in session con l'oggetto utente appena modificato

        return "redirect:/areariservata?successoModifica=true";
    }

}
