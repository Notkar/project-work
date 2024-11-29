// SEZIONE PRENOTAZIONI
    // mostra/nascondi sezione prenotazioni
    document.getElementById('btn-sezione-prenotazioni').addEventListener('click', function() {
        const sezionePrenotazioni = document.getElementById('sezione-prenotazioni');
        if (sezionePrenotazioni.style.display === 'none' || sezionePrenotazioni.style.display === '') {
            // mostra la sezione delle prenotazioni e nasconde quella di candidatura
            sezionePrenotazioni.style.display = 'block';
            document.getElementById('sezione-guida').style.display = 'none';
        } else {
            sezionePrenotazioni.style.display = 'none';
        }
    });


// SEZIONE CANDIDATURA
    // mostra/nascondi sezione guida
    document.getElementById('btn-sezione-guida').addEventListener('click', function() {
        const sezioneGuida = document.getElementById('sezione-guida');
        if (sezioneGuida.style.display === 'none' || sezioneGuida.style.display === '') {
            // mostra la sezione di candidatura e nasconde quella delle prenotazioni
            sezioneGuida.style.display = 'block';
            document.getElementById('sezione-prenotazioni').style.display = 'none';
        } else {
            sezioneGuida.style.display = 'none';
        }
    });

    // controllo grandezza file (foto candidato)
    document.getElementById("fotoPersonale").addEventListener("change", function() {
        const file = this.files[0];
        if (file) {
            const grandezzaFileInMB = file.size / (1024 * 1024);
            if (grandezzaFileInMB > 1) {
                alert("Il file selezionato supera la dimensione massima di 1 MB. Si prega di scegliere un file più piccolo.");
                this.value = "";
            }
        }
    });


// EVENTI LEGATI AI FORM: formConfermaPassword & formModificaRegistrazione
    const tastoProcedi = document.getElementById("procedi"); // tasto di submit per formConfermaPassword (e successivamente per formModificaRegistrazione)

    // controllo password per accedere a form di modifica
    const campoInputConfermaPassword = document.getElementById("richiesta-password"); // campo di conferma password

    document.getElementById("formConfermaPassword").addEventListener("submit", function() { // quando il form "formConfermaPassword" è inviato...
        event.preventDefault(); // blocca il submit
        campoInputConfermaPassword.setCustomValidity(''); // resetto la custom validity

        const inputPassword = campoInputConfermaPassword.value; // acquisisco l'input dell'utente dal campo di conferma password

        $.post (
            // indirizzo di /areariservata a cui inviare la richiesta
            "/areariservata/controlloPassword",
            {
                password:inputPassword // invio come parametro l'input dell'utente nel campo password
            },
            // restituisco dal controller un boolean true o false a seconda che la password sia corretta o meno
            function(risposta) {
                if(risposta){ // se la password inserita è corretta...
                    // Nascondi il div controllo-password -> dove si trova il form appena compilato per confermare la password
                    document.getElementById("controllo-password").style.display = "none";
                    // Mostra il div modifica-dati -> contiene il form di modifica dati utente
                    document.getElementById("modifica-dati").style.display = "block";
                    // trasforma campo password del form modifica dati in input type="password" -> di default è type text per poterci stampare la password corrente tramite thymeleaf
                    campoInputNuovaPassword.type = "password";

                    // modifico il tasto procedi -> così diventa il tasto di submit per il form di modifica dati utente
                    tastoProcedi.innerText = "Salva";
                    tastoProcedi.setAttribute("form","formModificaRegistrazione");

                    // mostro il tasto reset (prima era inutile come funzionalità)
                    document.getElementById("resetButton").style.display = "block";

                } else { // altrimenti se la password è errata...
                    campoInputConfermaPassword.setCustomValidity('Password non corretta'); // imposto custom validity
                    campoInputConfermaPassword.reportValidity(); // mostro messaggio di errore custom
                }
            }
        )
    });
    campoInputConfermaPassword.addEventListener('input', function() { // resetto la custom validity del form di conferma password all'input dell'utente per far sparire il messaggio di errore e non bloccare il form
        this.setCustomValidity('');
    });

    // controllo modifica dati utente
    const campoInputNuovaPassword = document.getElementById("registrazione-password"); // campo di modifica password
    campoInputModificaUsername = document.getElementById("username"); // campo di modifica username
    campoInputModificaEmail = document.getElementById("email"); // campo di modifica email

    document.getElementById("formModificaRegistrazione").addEventListener("submit", function() { // quando il form "formModificaRegistrazione" è inviato...
        event.preventDefault(); // blocca il submit

        inputModificaUsername = campoInputModificaUsername.value; // acquisisco l'input dell'utente dal campo di modifica username
        inputModificaEmail = campoInputModificaEmail.value; // acquisisco l'input dell'utente dal campo di modifica email

        let messaggioErrore = ""; // diventerà il messaggio di errore per le custom validity dei campi da controllare (o resterà una stringa vuota in caso non ci siano problemi)

        // resetto le custom validity per i campi di username e password
        campoInputModificaEmail.setCustomValidity("");
        campoInputModificaUsername.setCustomValidity("");

        $.post (
            // invio al controller username e email inseriti dall'utente
            "/areariservata/checkUsernameEmail",
            {
                username:inputModificaUsername,
                email:inputModificaEmail
            },
            // lato back-end faccio i controlli (controlli: username e/o email non devono essere già in uso da altri utenti) e ritorno una risposta che indica quale campo non va bene
            function(risposta) {
                switch(risposta){
                    case "email":
                        messaggioErrore = "Email già in uso."
                        campoInputModificaEmail.setCustomValidity(messaggioErrore);
                        campoInputModificaEmail.reportValidity(); // mostro messaggio di errore custom
                        break;
                    case "username":
                        messaggioErrore = "Nome utente già in uso."
                        campoInputModificaUsername.setCustomValidity(messaggioErrore);
                        campoInputModificaUsername.reportValidity(); // mostro messaggio di errore custom
                        break;
                    default: // l'altra possibile stringa di risposta è "ok"
                        break;
                }

                if (messaggioErrore === "") { // se il messaggio di errore è ancora una stringa vuota, allora i controlli su username e email sono passati con successo
                    if (!validaPassword(campoInputPassword)) { // controllo su password; vedi file validazioneNascitaPasswordUtente.js
                        campoInputPassword.reportValidity(); // mostro messaggio di errore custom
                        return;
                    }

                    document.getElementById("formModificaRegistrazione").submit(); // superati i controlli posso procedere con il submit

                } else { // altrimenti non procedo con il submit -> in questo modo vengono mostrati i messaggi custom di errore sul form grazie a reportValidity()
                    return;
                }
            }
        )
    });
    campoInputModificaUsername.addEventListener('input', function() { // resetto la custom validity del form di modifica username all'input dell'utente
        this.setCustomValidity('');
    });
    campoInputModificaEmail.addEventListener('input', function() { // resetto la custom validity del form di modifica email all'input dell'utente
        this.setCustomValidity('');
    });


// GESTIONE MODALE x MODIFICA DATI UTENTE (abbiamo una singola modale che contiene formConfermaPassword & formModificaRegistrazione)
    const modaleModificaDati = document.getElementById('modificaDati'); // modale sopra menzionata

    // Quando la modale viene chiusa...
    modaleModificaDati.addEventListener('hidden.bs.modal', function () {
        // ripristina la visibilità dei div (nasconde l'area di modifica dati e ripristina quella di conferma password)
        document.getElementById("controllo-password").style.display = "block";
        document.getElementById("modifica-dati").style.display = "none";
        // resetto il campo di conferma password (svuoto il campo, rimetto type password nel caso sia stata mostrata la password, resetto l'icona di toggle per visualizzare la password)
        campoInputConfermaPassword.value = "";
        campoInputConfermaPassword.type = "password";
        iconaTogglePassword.className = "bi bi-eye-slash";
        // Ripristino il tasto procedi (torna ad essere il submit di formConfermaPassword)
        tastoProcedi.innerText = "Procedi";
        tastoProcedi.setAttribute("form","formConfermaPassword");
        // nascondo il tasto reset
        document.getElementById("resetButton").style.display = "none";
        // resetto il form di modifica dati (i valori default tornano ad essere quelli dell'utente, inseriti con thymeleaf)
        document.getElementById('formModificaRegistrazione').reset();
        // resetto custom validity dei campi di modifica username ed email
        campoInputModificaEmail.setCustomValidity("");
        campoInputModificaUsername.setCustomValidity("");
    });


// GESTIONE MOSTRA/NASCONDI PASSWORD
    // Funzione mostra/nascondi password
    function togglePassword(campo, icona) {
        // Cambia il type del campo ("text" -> "password" e viceversa)
        campo.type = campo.type === "password" ? "text" : "password";
        // Cambia l'icona
        icona.className = icona.className === "bi bi-eye-slash" ? "bi bi-eye" : "bi bi-eye-slash";
    };

    // toggle visualizzazione password (campo conferma password)
    const iconaTogglePassword = document.getElementById("icona-toggle-password"); // icona toggler per mostrare/nascondere password in formConfermaPassword
    document.getElementById("toggle-password").addEventListener("click", function() {
        togglePassword(campoInputConfermaPassword, iconaTogglePassword);
    });

    // toggle visualizzazione password (campo modifica password)
    const iconaToggleNuovaPassword = document.getElementById("icona-toggle-cambio-password"); // icona toggler per mostrare/nascondere password in formModificaRegistrazione
    document.getElementById("toggle-cambio-password").addEventListener("click", function() {
        togglePassword(campoInputNuovaPassword, iconaToggleNuovaPassword);
    });


// INIZIALIZZAZIONE MODALE MESSAGGIO DI CONFERMA
    const modaleMessaggio = new bootstrap.Modal(document.getElementById('successoModifica')); // inizializzo la modale specifica per il messaggio di successo candidatura / successo modificat dati utente (viene mostrata in base a un th:if; vedi ultime righe di areariservata.html)