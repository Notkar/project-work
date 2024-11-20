// script usato in checkout.html per effettuare controlli di validazione sul form

document.getElementById('checkoutForm').addEventListener('submit', function(event) {
  event.preventDefault(); // blocca il submit

  const campoNumeroCarta = document.getElementById('numeroCarta'); // input del numero della carta
  const campoDataScadenza = document.getElementById('dataScadenza'); // input della scadenza della carta
  const campoCVV = document.getElementById('cvv'); // input del cvv della carta

  // resetta eventuali customValidity al submit del form
  campoNumeroCarta.setCustomValidity('');
  campoDataScadenza.setCustomValidity('');
  campoCVV.setCustomValidity('');

  // validazione numero carta
  if (!/^\d{16}$/.test(campoNumeroCarta.value.replace(/\s/g, ''))) {
    campoNumeroCarta.setCustomValidity('Il numero di carta deve avere 16 caratteri numerici.');
    campoNumeroCarta.reportValidity();
    return;
  }

  // validazione scadenza carta
  const expiryRegex = /^(0[1-9]|1[0-2])\/([0-9]{2})$/; // formato MM/YY
  const match = campoDataScadenza.value.match(expiryRegex);

  if (!match) {
    campoDataScadenza.setCustomValidity('Il formato della data di scadenza deve essere MM/YY.');
    campoDataScadenza.reportValidity();
    return;
  }

  // recupero mese e anno dal formato MM/YY
  const mese = parseInt(match[1], 10);
  const anno = parseInt(match[2], 10) + 2000; // recupero anno in formato YY -> YYYY

  // recupero data corrente
  const oggi = new Date();
  const annoCorrente = today.getFullYear();
  const meseCorrente = today.getMonth() + 1; // reminder: i mesi sono contati partendo da 0

  // validazione data
  if (anno < annoCorrente || (anno === annoCorrente && mese < meseCorrente)) {
    campoDataScadenza.setCustomValidity('La data di scadenza deve essere successiva alla data odierna.');
    campoDataScadenza.reportValidity();
    return;
  }

  // validazione cvv
  if (!/^\d{3}$/.test(campoCVV.value)) {
    campoCVV.setCustomValidity('Il CVV deve avere 3 caratteri numerici.');
    campoCVV.reportValidity();
    return;
  }

  this.submit(); // se passano tutti i check, submit
});


// reset delle validity sull'input dell'utente
document.getElementById('cardNumber').addEventListener('input', function() {
  this.setCustomValidity('');
});
document.getElementById('expiryDate').addEventListener('input', function() {
  this.setCustomValidity('');
});
document.getElementById('cvv').addEventListener('input', function() {
  this.setCustomValidity('');
});