package hua.dit.localDocWebApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpringbootdemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	public void testCreateUser() throws Exception {
		// Arrange
		String userJson = "{\"username\":\"apiuser\",\"email\":\"api@hua.gr\",\"password\":\"pass123\",\"userRole\":\"ROLE_CLIENT\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"phone\":\"1234567890\",\"address\":\"123 Main St\",\"city\":\"Athens\",\"state\":\"Attica\",\"postalCode\":\"12345\"}";


		// Act
		ResultActions result = mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));

		// Assert
		result.andExpect(status().isOk());
	}

	@Test
	public void testSignUser() throws Exception {
		// Arrange
		String userJson = "{\"username\":\"apiuser\",\"password\":\"pass123\"}";

		// Act
		ResultActions result = mockMvc.perform(post("/api/auth/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));

		// Assert
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("apiuser"));

	}

}