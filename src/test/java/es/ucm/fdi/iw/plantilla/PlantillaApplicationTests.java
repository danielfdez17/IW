package es.ucm.fdi.iw.plantilla;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlantillaApplicationTests {

    @LocalServerPort private int port;
	@Autowired private WebApplicationContext wac;	
	private MockMvc mockMvc;	
	
	@BeforeEach
	public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders
			.webAppContextSetup(this.wac).build();
	}

	@Test
	void contextLoads() {
		// Sólo estamos probando que la plantilla carga
	}

	@Test
	public void aSimpleTest() throws Exception {
		/*
		Lo comento porque no tenemos esta ruta '/api/status/test' ya que no tenemos APIs.
	    MvcResult mvcResult = this.mockMvc.perform(get("/api/status/test"))
	      .andDo(print()).andExpect(status().isOk())
	      .andExpect(jsonPath("$.code").value("test"))
	      .andReturn();	     
	    Assertions.assertEquals("application/json;charset=UTF-8", 
	      mvcResult.getResponse().getContentType());
		*/
	}
}
