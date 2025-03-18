Feature: crear una subasta
    Scenario: entrar en la app y crear una subasta

    # Hacer login
    Given call read('login.feature@login_a')

    # Nos vamos a la vista de subastas
    And driver baseUrl + '/'

    # Hacemos clic en el botón de crear subasta
    And click("button.btn-primary")

    # Rellenamos el formulario
    # And input('#fechaInicio', '2026-01-01')
    # And input('#fechaFin', '2026-01-02')



    And input('#nombreProducto', 'nombre')
    And input('#descripcionProducto', 'desc')
    And input('#precioInicial', '10')
    And click("button#btnCrearSubasta")


    # Nos vamos a la vista de subastas
    # And driver baseUrl + '/index'

    
    # Paramos el test
    # * karate.stop(9000)

    # Hacer login
    Given call read('test1.feature')

    # * karate.stop(9000)

    # Given call read('login.feature@login_a')

    And driver.screenshot()