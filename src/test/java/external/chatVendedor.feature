Feature: mandar mensaje al vendedor

Scenario: entrar en el chat con el vendedor y mandarle un mensaje
  # Hacemos login para poder entrar en el chat
  Given driver baseUrl + '/login'
  And waitFor("#username")
  And input('#username', 'b')
  And input('#password', 'aa')
  When submit().click(".form-signin button")
  Then waitForUrl(baseUrl + '/user/2')
  
  # Nos vamos a la vista del producto
  And driver baseUrl + '/products/1'
  
  # Clic en el enlace "Chat con el vendedor"
  And waitFor("a[href*='/chat?userChatNew=']").click()
  
  # Verificamos que la URL es correcta
  Then match driver.url contains '/chat?userChatNew='
  
 # Esperamos que el chat se cargue correctamente
 And waitFor("#chat")

 # Esperamos que el campo de texto esté presente
 And waitFor("#txtSend")

 # Escribimos un mensaje en el campo de texto
 And input("#txtSend", "Hola, estoy interesado en tu producto.")

 # Clic en el botón de enviar mensaje
 And click("#btnSend")

 # Esperamos que el mensaje aparezca en el chat
 And waitFor("ul#chat li:last-child div.message")
 
 # Verificamos que el último mensaje enviado sea el correcto
 Then match text("ul#chat li:last-child div.message") == "Hola, estoy interesado en tu producto."

