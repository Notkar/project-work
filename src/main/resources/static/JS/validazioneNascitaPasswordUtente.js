// script usato per controlli sui form in cui si registrano o modificano i dati dell'utente (form di registrazione in login-registrazione.html e form di modifica dati in areariservata.html)


// impostazione data massima selezionabile per data di nascita
    // data attuale
    const oggi = new Date();
    // data massima selezionabile per data di nascita = data attuale - 18 anni
    const dataMax = new Date(
        oggi.getFullYear() - 18,
        oggi.getMonth(),
        oggi.getDate()
    );
    // formatto opportunamente la data e la setto come max dell'input
    const dataFormattata = dataMax.toISOString().split('T')[0]; // il formato ISO in cui converto la data segue il seguente schema: "YYYY-MM-DDTHH:mm:ss.sssZ"
    document.getElementById("nascita").max = dataFormattata;


// controlli sulla password
    const campoInputPassword = document.getElementById("registrazione-password"); // recupero il campo della password

    // evento di controllo
    document.getElementById("formModificaRegistrazione").addEventListener("submit", function(event) {

        if (!validaPassword(campoInputPassword)) {
            campoInputPassword.reportValidity();
            event.preventDefault();
        }
    });

    // metodo di controllo
    function validaPassword(campoInputPassword) {
        const password = campoInputPassword.value; // recupero l'input dell'utente

        // imposto i pattern a cui la password deve sottostare
        const patternMaiuscole = /[A-Z]/;
        const patternMinuscole = /[a-z]/;
        const patternNumeri = /[0-9]/;
        const patternSimboli = /[!@#$%^&*()_+\-=\[\]{};:\\|,.<>/?]/;

        // inizializzo il messaggio di errore come stringa vuota
        let messaggioErrore = "";

        // controllo la password per i vari pattern
        if (!patternMaiuscole.test(password)) {
            messaggioErrore += "Deve contenere almeno una lettera maiuscola (A-Z).\n"; // per ogni controllo che la password fallisce, aggiungo un pezzo al messaggio di errore
        }
        if (!patternMinuscole.test(password)) {
            messaggioErrore += "Deve contenere almeno una lettera minuscola (a-z).\n";
        }
        if (!patternNumeri.test(password)) {
            messaggioErrore += "Deve contenere almeno un numero (0-9).\n";
        }
        if (!patternSimboli.test(password)) {
            messaggioErrore += "Deve contenere almeno un simbolo consentito (! @ # $ % ^ & * ( ) _ + - = [ ] { } ; : \\ | , . < > / ?).\n";
        }

        if (messaggioErrore === "") { // se il messaggio di errore è ancora una stringa vuota, vuol dire che l'input ha passato tutti i controlli con successo
            return true;
        } else { // altrimenti imposto la custom validity con il messaggio di errore
            campoInputPassword.setCustomValidity(messaggioErrore);
            return false;
        }
    }

    // refresh della validity dell'input password per ogni volta che l'utente scrive nel campo
    campoInputPassword.addEventListener("input", function(event) {
        this.setCustomValidity("");
    });