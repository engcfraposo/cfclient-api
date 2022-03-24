package com.cloudfox.cfcclient.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.cloudfox.cfcclient.dto.ClientDTO;
import com.cloudfox.cfclient.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ClientResourceIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalCategories;
	
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalCategories = 10L;		
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() throws Exception{
		
		ResultActions result = mockMvc.perform(get("/clients?page=0&size=12&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalCategories));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Claudio Raposo"));
		result.andExpect(jsonPath("$.content[1].name").value("João Farias"));
		result.andExpect(jsonPath("$.content[2].name").value("Laurindo Farias"));
	}
	
	@Test
	public void updatedShouldReturnClientDTOWhenIdExists() throws Exception{
		
		ClientDTO categoryDTO = Factory.createClientDTO();
		String jsonBody = objectMapper.writeValueAsString(categoryDTO);
		
		String expectedName = categoryDTO.getName();
		
		ResultActions result = mockMvc.perform(put("/clients/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
	}
	
	@Test
	public void insertShouldReturnClientDTOWhenCalled() throws Exception{
		
		ClientDTO categoryDTO = Factory.createClientDTO();
		String jsonBody = objectMapper.writeValueAsString(categoryDTO);
		
		long expectedId = 4L;
		String expectedName = categoryDTO.getName();
		
		ResultActions result = mockMvc.perform(post("/clients")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(expectedId));
		result.andExpect(jsonPath("$.name").value(expectedName));
	}
	
	@Test
	public void updatedShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		
		ClientDTO categoryDTO = Factory.createClientDTO();
		String jsonBody = objectMapper.writeValueAsString(categoryDTO);
		
		ResultActions result = mockMvc.perform(put("/clients/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception{
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", nonExistingId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
}
