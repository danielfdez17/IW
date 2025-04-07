Feature: publicar una reseña
    Scenario: entrar en la reseña de un producto, editarla y publicarla correctamente

        #Hacemos login usuario 1
        Given call read('login.feature@login_a')

        # Crear subasta (para asegurar que hay alguna subasta a la que dejar reseña)
        # Nos vamos a la vista de subastas
        And driver baseUrl + '/'
        # Hacemos clic en el botón de crear subasta
        And click("button.btn-outline-danger")
        # Rellenamos el formulario
        And input('#nuevaFechaInicio', '2026-01-01')
        And input('#nuevaFechaFin', '2026-01-02')
        And input('#nombreProducto', 'nombre')
        And input('#descripcionProducto', 'desc')
        And input('#precioInicial', '10')
        And click('#btnCrearSubasta')

        #Hacemos login usuario 2
        Given call read('login.feature@logout')
        Given call read('login.feature@login_b')

        #Nos vamos a la vista del historico
        And driver baseUrl + '/historical/' 
        #Nos vamos a la vista de reseña de uno de los productos
        And driver baseUrl + '/historical/reviews/1'

        #Rellenamos la informacion de la reseña
        And script("document.querySelectorAll('#star-widget .fa-star')[2].click()") 
        And clear("#comment")
        And input("#comment", "Muy buen producto, el vendedor es un crack!")     
        # Hacemos clic en el botón de enviar reseña
        And click("button.btn-primary")


        # Comprobamos que se muestra el comentario enviado
        Then match value("#comment") contains "Muy buen producto, el vendedor es un crack!"
        # Comprobamos que se muestran las estrellas
        Then match value("#ratingInput") == "3"

        # Tomamos una captura de pantalla
        And driver.screenshot()