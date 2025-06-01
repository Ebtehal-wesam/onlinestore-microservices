package com.mystore.productservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mystore.productservice.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
class ProductserviceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	private Product product_test;

	@BeforeEach
	void setUp() {
		product_test = new Product(15,"bag", "green large bag", BigDecimal.valueOf(12.3));
	}

	@Test
	void testCreateProduct() throws Exception {
		mockMvc.perform(post("/add")
						.content(objectMapper.writeValueAsString(product_test))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Sample Product"));
	}


	@Test
	void testDeleteProduct() throws Exception {
		mockMvc.perform(delete("/delete/15"))
				.andExpect(status().isNoContent());
	}

}
