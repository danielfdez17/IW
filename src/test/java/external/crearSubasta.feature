Feature: crear una subasta
    Scenario: entrar en la app y crear una subasta

    # Hacer login
    Given call read('login.feature@login_a')

    # Nos vamos a la vista de subastas
    And driver baseUrl + '/index'

    # Hacemos clic en el botón de crear subasta
    And click("button.btn-outline-danger")

    # Rellenamos el formulario
    And value('#nuevaFechaInicio', '2026-01-01')
    And value('#nuevaFechaFin', '2026-01-02')

    And value('#nombreProducto', 'nombre')
    And value('#descripcionProducto', 'desc')
    And value('#precioInicial', '10')
    #And click("button#btnCrearSubasta")
    * submit().click("#btnCrearSubasta")


    # Nos vamos a la vista de subastas
    # And driver baseUrl + '/index'


    # Hacer puja correctamente
    Given call read('test1.feature')

    #Hacer puja incorrecta
    Given call read('test2.feature')

    # Contactar con el vendedor
    Given call read('chatVendedor.feature')

    # * karate.stop(9000)

    # Given call read('login.feature@login_a')

    And driver.screenshot()
