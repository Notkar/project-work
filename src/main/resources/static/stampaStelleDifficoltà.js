// script usato in itinerari.html e dettaglio.html per stampare un dato numero di stelle per una data difficoltà del percorso

// seleziono l'elemento star-rating e ottiengo la difficoltà dal data-attribute
const starRatingElements = document.querySelectorAll(".star-rating"); // seleziono gli <span> dentro cui stampo le stelle

// iterazione su ogni elemento
starRatingElements.forEach(starRatingElement => {
    // ottengo la difficoltà dal data-attribute
    const difficolta = starRatingElement.getAttribute("data-difficolta")?.toLowerCase().trim();

    // assegno le stelle in base alla difficoltà
    let stars = '';

    switch(difficolta){
        case "facile": // 1 stella piena, 2 vuote
            stars = "★☆☆";
            break;
        case "media": // 2 stelle piene, 1 vuota
            stars = "★★☆";
            break;
        case "difficile": // 3 stelle piene, 0 vuote
            stars = "★★★";
            break;
        default: // caso in cui la difficoltà non corrisponda, lascio la stringa vuota
            break;
    }

    // aggiorno il contenuto dell'elemento star-rating con il numero di stelle corretto
    starRatingElement.innerText = stars;
});