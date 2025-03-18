Feature: hacer una puja
    Scenario: entrar en una subasta y pujar correctamente

        #Hacemos login para poder pujar
        Given call read('login.feature@login_a')
        #Nos vamos a la vista de uno de los productos
        And driver baseUrl + '/products/2' 
        #Pujamos con un precio superior al actual
        And def puja =  "20" 
        #al multiplicar por 1 se pasa a numero

        # Escribimos la puja en el input correcto
        And input('#nuevaPuja', puja)
        
        # Hacemos clic en el botón de puja
        And click("button.btn-success")

        # Esperamos a que el mensaje de confirmación aparezca
        # And waitForText("#mensaje-puja", "Puja realizada")

        # Verificamos que el precio actual ha cambiado
        Then match text("#precio-actual") contains puja

        # Tomamos una captura de pantalla
        And driver.screenshot()
