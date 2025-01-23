"use strict";
$(() => {
  Toastify({
    text: "Bienvenido a Casa Apuestas",
    duration: 3000,
    newWindow: true,
    gravity: "top",
    position: "center",
    stopOnFocus: true,
    style: {
      background: "linear-gradient(to right, #00b09b, #96c93d)",
    },
  }).showToast();
});
