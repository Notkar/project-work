$(document).ready(function(){
    const idItinerario = document.getElementById("idItinerario").value; // registro l'id dell'itinerario, che nel form è salvato in un type=hidden
    const totale = document.getElementById("totale"); // salvo il percorso del campo del prezzo totale

    modificaPrezzo(idItinerario, 1); // a caricamento pagina imposto il prezzo totale (vedi funzione più sotto)

    // ogni volta che cambia il numero di partecipanti dal select si deve modificare di nuovo il prezzo totale
    document.getElementById("numeroPartecipanti").addEventListener("change", function() {
        const numeroPartecipanti = this.value; // registro il valore corrente del select (ovvero il numero dei partecipanti selezionato)

        modificaPrezzo(idItinerario, numeroPartecipanti);
    });

    // funzione che si occupa della modifica del prezzo totale
    function modificaPrezzo(idItinerario, numeroPartecipanti) {

        $.post (
            // invio id dell'itinerario e numero di partecipanti al controller
            "/checkout/modificaPrezzo",
            {
                id:idItinerario,
                partecipanti:numeroPartecipanti
            },
            // dal lato controller faccio i calcoli e le formattazioni del caso e rendo il prezzo totale
            function(risposta) {
                totale.value = risposta; // imposto il nuovo prezzo totale
            }
        )
    }
});