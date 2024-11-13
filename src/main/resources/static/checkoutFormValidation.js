document.getElementById('checkoutForm').addEventListener('submit', function(event) {
  event.preventDefault(); // blocca il submit

  const cardNumberInput = document.getElementById('cardNumber'); // input del numero della carta
  const expiryDateInput = document.getElementById('expiryDate'); // input della scadenza della carta
  const cvvInput = document.getElementById('cvv'); // input del cvv della carta

  // resette eventuali customValidity
  cardNumberInput.setCustomValidity('');
  expiryDateInput.setCustomValidity('');
  cvvInput.setCustomValidity('');

  // validazione numero carta
  if (!/^\d{16}$/.test(cardNumberInput.value.replace(/\s/g, ''))) {
    cardNumberInput.setCustomValidity('Il numero di carta deve avere 16 caratteri numerici.');
    cardNumberInput.reportValidity();
    return;
  }

  // validazione scadenza carta
  const expiryRegex = /^(0[1-9]|1[0-2])\/([0-9]{2})$/; // formato MM/YY
  const match = expiryDateInput.value.match(expiryRegex);

  if (!match) {
    expiryDateInput.setCustomValidity('Il formato della data di scadenza deve essere MM/YY.');
    expiryDateInput.reportValidity();
    return;
  }

  // recupero mese e anno dal formato MM/YY
  const month = parseInt(match[1], 10);
  const year = parseInt(match[2], 10) + 2000; // recupero anno in formato YY -> YYYY

  // recupero data corrente
  const today = new Date();
  const currentYear = today.getFullYear();
  const currentMonth = today.getMonth() + 1; // reminder: i mesi sono contati partendo da 0

  // validazione data
  if (year < currentYear || (year === currentYear && month < currentMonth)) {
    expiryDateInput.setCustomValidity('La data di scadenza deve essere successiva alla data odierna.');
    expiryDateInput.reportValidity();
    return;
  }

  // validazione cvv
  if (!/^\d{3}$/.test(cvvInput.value)) {
    cvvInput.setCustomValidity('Il CVV deve avere 3 caratteri numerici.');
    cvvInput.reportValidity();
    return;
  }

  this.submit(); // se passano tutti i check, submit
});


// reset della validity sull'input dell'utente
document.getElementById('cardNumber').addEventListener('input', function() {
  this.setCustomValidity('');
});

document.getElementById('expiryDate').addEventListener('input', function() {
  this.setCustomValidity('');
});

document.getElementById('cvv').addEventListener('input', function() {
  this.setCustomValidity('');
});