
Feature: hacer una puja
    Scenario: entrar en una subasta y pujar correctamente

        #Hacemos login para poder pujar
        #Given call read('login.feature@login_a')
        #Nos vamos a la vista de uno de los productos

        And driver baseUrl + '/products/1' 
        
        #Pujamos con un precio superior al actual
        And def puja = "2000"
        

        # Escribimos la puja en el input correcto
        And value('#nuevaPuja', puja)
        
        # Hacemos clic en el botón de puja
        And submit().click("button.btn-success")

        # Paramos el test
        # * karate.stop(9000)
    

        # Verificamos que el precio actual ha cambiado
        Then match text("#precio-actual") contains puja 

        # Tomamos una captura de pantalla
        And driver.screenshot()
