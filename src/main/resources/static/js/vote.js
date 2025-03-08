document.addEventListener('DOMContentLoaded', function() {
  const stars = document.querySelectorAll("#star-widget .fa-star");
  const ratingInput = document.getElementById("ratingInput");

  // Función para actualizar la visualización de las estrellas según el rating actual
  function updateStars(rating) {
    stars.forEach(function(star) {
      if (parseInt(star.getAttribute("data-rating")) <= rating) {
        star.classList.add("checked");
      } else {
        star.classList.remove("checked");
      }
    });
  }

  // Eventos para cada estrella
  stars.forEach(function(star) {
    // Al hacer clic, se establece el rating en el input hidden y se actualiza la visualización
    star.addEventListener("click", function() {
      const rating = parseInt(this.getAttribute("data-rating"));
      ratingInput.value = rating;
      updateStars(rating);
    });

    // Efecto hover: ilumina las estrellas hasta la actual
    star.addEventListener("mouseover", function() {
      const rating = parseInt(this.getAttribute("data-rating"));
      updateStars(rating);
    });

    // Al salir del hover, se restaura la selección según el valor del input hidden
    star.addEventListener("mouseout", function() {
      const currentRating = parseInt(ratingInput.value);
      updateStars(currentRating);
    });
  });

  // Si el input hidden ya tiene un valor (por ejemplo, al recargar la página tras haber votado)
  if (parseInt(ratingInput.value) > 0) {
    updateStars(parseInt(ratingInput.value));
  }
});