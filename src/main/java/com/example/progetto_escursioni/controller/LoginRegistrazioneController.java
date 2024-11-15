package com.example.progetto_escursioni.controller;

import com.example.progetto_escursioni.model.Utente;
import com.example.progetto_escursioni.service.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/loginregistrazione")
public class LoginRegistrazioneController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping
    public String getPage(
            HttpSession session,
            Model model,
            @RequestParam(name = "erroreCredenziali", required = false) String erroreCredenziali,
            @RequestParam(name = "successoRegistrazione", required = false) String successoRegistrazione) {

        // se provi a riaccedere alla pagina di login quando sei già registrato (modificando l'URL), vieni reindirizzato all'area riservata
        if (session.getAttribute("utente") != null) {
            return "redirect:/areariservata";
        }

        // crea un nuovo oggetto utente e lo registra nel model perché il form di registrazione ha il binding con esso
        Utente utente = new Utente();
        model.addAttribute("utente", utente);

        // messaggio di errore per login
        if(erroreCredenziali != null) {
            model.addAttribute("messaggio", "Credenziali errate");
        }

        // messaggi di successo o errore per registrazione
        if(successoRegistrazione != null && successoRegistrazione.equals("true")){
            model.addAttribute("messaggio", "Registrazione effettuata"); // questo messaggio deve comparire sul form di login
        } else if(successoRegistrazione != null && successoRegistrazione.equals("false")) {
            model.addAttribute("messaggioDue", "Nome utente o email già in uso"); // questo messaggio deve comparire sul form di registrazione
        }

        return "login-registrazione";
    }

    @PostMapping("/login")
    public String loginManager(HttpSession session,
                               Model model,
                               @ModelAttribute("utente") Utente utente,
                               @RequestParam("login-username") String username,
                               @RequestParam("login-password") String password) {


        if (utenteService.loginUtente(session, username, password)) {
            // se il login ha successo (ovvero se c'è un utente corrispondente sul database) allora reindirizza alla pagina precedente
            return "redirect:/" + session.getAttribute("paginaPrecedente");
        }

        return "redirect:/loginregistrazione?erroreCredenziali";
    }

    @PostMapping("/registrazione")
    public String registrazioneManager(Model model,
                                       @ModelAttribute("utente") Utente utente) {

        // controllo per vedere se username o email sono già in uso
        if (utenteService.controlloUsernameEmail(utente.getUsername(), utente.getEmail())) {
            utenteService.salvaUtente(utente); // se è tutto ok salva l'utente sul database
            return "redirect:/loginregistrazione?successoRegistrazione=true";
        }
        else {
            return "redirect:/loginregistrazione?successoRegistrazione=false";
        }
    }

    // mapping per tasto indietro
    @GetMapping("/indietro")
    public String tornaIndietro(HttpSession session) {
        // i mapping sono strutturati in modo che se vado sulla pagina di checkout di un itinerario ma non sono loggato, allora vengo reindirizzato sulla pagina di login
        // questo vuol dire che se paginaPrecedente è una pagina di checkout di un dato itinerario, quando l'utente preme sul tasto indietro dobbiamo piuttosto far tornare alla pagina di dettaglio di quell'itinerario invece che alla pagina di checkout, perché altrimenti saremmo subito reindirizzati sulla pagina corrente (la pagina di login)
        if(((String)session.getAttribute("paginaPrecedente")).contains("checkout")) {
            return "redirect:/" + ((String)session.getAttribute("paginaPrecedente")).replace("checkout", "dettaglio"); // redirect:/checkout?id=### -> redirect:/dettaglio?id=###
        }
        return "redirect:/" + session.getAttribute("paginaPrecedente");
    }
}
