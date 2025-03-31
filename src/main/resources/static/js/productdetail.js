"use strict";
function pujar() {
  let inputPuja = document.getElementById("nuevaPuja");
  let nuevaPuja = parseFloat(inputPuja.value);
  let productoId = window.location.pathname.split("/").pop();

  if (isNaN(nuevaPuja) || nuevaPuja <= 0) {
    mostrarMensaje("Introduce un valor válido.", "danger");
    return;
  }
  // fetch(`/products/${productoId}/pujar`, {
  //     method: "POST",
  //     headers: { "Content-Type": "application/x-www-form-urlencoded" },
  //     body: new URLSearchParams({ puja: nuevaPuja })
  // })
  //     .then(response => response.text())
  //     .then(data => {
  //         if (data.includes("Puja realizada")) {
  //             mostrarMensaje(data, "success");
  //             document.getElementById("precio-actual").innerText = `€${nuevaPuja.toFixed(2)}`;
  //             inputPuja.value = "";
  //         } else {
  //             mostrarMensaje(data, "danger");
  //         }
  //     })
  //     .catch(error => {
  //         console.error("Error:", error);
  //         mostrarMensaje("Error en la puja, inténtalo de nuevo.", "danger");
  //     });
}

function mostrarMensaje(mensaje, tipo) {
  let mensajeDiv = document.getElementById("mensaje-puja");
  mensajeDiv.innerHTML = `<div class="alert alert-${tipo}" role="alert">${mensaje}</div>`;
}

function subirPuja(precio) {
  const nuevaPuja = document.getElementById("nuevaPuja");
  nuevaPuja.value = Number(nuevaPuja.value) + Number(precio);
}
