// js per controllo per le checkbox del filtro di difficoltà in itinerari.html

    const checkBoxes = document.getElementsByClassName("checkboxDifficolta"); // array che contiene le checkbox

    // ci dev'essere un controllo al momento del submit del form
    document.getElementById("formFiltri").addEventListener("submit", function(event) {
        if (!validaCheckbox()) { // se non passa il controllo...
            event.preventDefault(); // blocco il submit
            // per ogni checkbox triggero la validity per mostrare il messaggio di errore
            for (let checkbox of checkBoxes) {
                checkbox.reportValidity();
            }
        }
    });

    // refresh della validity al click su una checkbox
    for (let checkbox of checkBoxes) { // per ogni checkbox dell'array checkBoxes...
        checkbox.addEventListener("click", function(event) { // aggiungo un evento click
            for (let checkbox of checkBoxes) { // al click su una delle checkbox, si resetta la customValidity di ogni checkbox dell'array checkBoxes
                checkbox.setCustomValidity("");
            }
        });
    }

    // funzione di validazione
    function validaCheckbox() {
        let isChecked = false; // true quando almeno una checkbox è checked

        for (let checkbox of checkBoxes) { // per ogni checkbox controlla se è checked
            if (checkbox.checked) { // se almeno una è checked, allora isChecked è true e interrompo il ciclo
                isChecked = true;
                break;
            }
        }

        if (isChecked) { // almeno una checkbox è checked: restituisco true, il form può procedere
            return true;
        } else { // altrimenti...
            const errorMessage = "Seleziona almeno una difficoltà"; // creo un messaggio di errore
            // lo imposto per tutte le checkbox con setCustomValidity()
            for (let checkbox of checkBoxes) {
                checkbox.setCustomValidity(errorMessage);
            }
            return false; // restituisco false: il submit resta bloccato
        }
    }