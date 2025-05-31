"use strict";
const MAX = 4;
let totalPares = 0;
let totalParesPulsados = 0;
let totalImparesPulsados = 0;

document.addEventListener("DOMContentLoaded", () => {
  const div = document.getElementById("checkboxes");
  const errorMsg = document.getElementById("errorMsg");

  for (let i = 0; i < MAX; ++i) {
    const random = Math.floor(Math.random() * MAX);
    const input = document.createElement("input");
    const label = document.createElement("label");
    input.name = random;
    input.value = random;
    input.id = `random${i}`;
    input.className = "btn-check w-100";
    input.type = "checkbox";
    input.addEventListener("click", () => {
      if (input.value % 2 != 0) {
        if (input.checked) {
          totalImparesPulsados++;
        } else {
          totalImparesPulsados--;
        }
      } else {
        if (input.checked) {
          totalParesPulsados++;
        } else {
          totalParesPulsados--;
        }
      }
      const btn = document.getElementById("submitBtn");
      if (totalImparesPulsados > 0) {
        btn.disabled = true;
        errorMsg.className = "text-danger mt-3 text-center fs-4";
        return;
      }
      errorMsg.className = "d-none text-danger mt-3 text-center fs-4";
      if (totalPares === totalParesPulsados) {
        console.info("deshabilitando btn");
        btn.disabled = false;
      } else {
        console.info("habilitando btn");
        btn.disabled = true;
      }
      console.warn(
        `Pares totales: ${totalPares}; Pares pulsados: ${totalParesPulsados}`
      );
    });
    if (random % 2 == 0) totalPares++;
    label.htmlFor = `random${i}`;
    label.className = "btn btn-outline-primary";
    label.innerText = random;
    div.append(input);
    div.append(label);
  }
});
