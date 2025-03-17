Feature: hacer una puja
    Scenario: entrar en una subasta y pujar incorrectamente

        #Hacemos login para poder pujar
        Given call read('login.feature@login_a')
        #Nos vamos a la vista de uno de los productos
        And driver baseUrl + '/products/3' 
        #Pujamos con un precio inferior al actual
        And def puja = "1"

        # Escribimos la puja en el input correcto
        And input('#nuevaPuja', puja)
        
        # Hacemos clic en el botón de puja
        And click("button.btn-success")

        # Esperamos a que el mensaje aparezca
        And waitForText("#mensaje-puja", "La puja debe ser mayor al precio actual.")

        # Tomamos una captura de pantalla
        And driver.screenshot()
