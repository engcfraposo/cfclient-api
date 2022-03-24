package com.cloudfox.cfcclient.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cloudfox.cfcclient.dto.ClientDTO;
import com.cloudfox.cfclient.resources.ClientResource;
import com.cloudfox.cfclient.services.ClientService;
import com.cloudfox.cfclient.services.exceptions.DatabaseException;
import com.cloudfox.cfclient.services.exceptions.ResourceNotFoundException;
import com.cloudfox.cfclient.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClientResource.class)
public class ClientResourceTests {
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<ClientDTO> page;
	private ClientDTO clientDTO;
	private String jsonBody;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ClientService service;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		clientDTO = Factory.createClientDTO();
		page = new PageImpl<>(List.of(clientDTO));
		jsonBody = objectMapper.writeValueAsString(clientDTO);
		
		//GET /clients
		Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
		
		//GET /clients/id
		Mockito.when(service.findById(existingId)).thenReturn(clientDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		//PUT /clients/id
		Mockito.when(service.update(existingId, clientDTO)).thenReturn(clientDTO);
		Mockito.when(service.update(nonExistingId, clientDTO)).thenThrow(ResourceNotFoundException.class);
		
		//POST /clients
		Mockito.when(service.insert(clientDTO)).thenReturn(clientDTO);
		
		//DELETE /clients
		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);

	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/clients")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isArray());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.pageable").exists());
		result.andExpect(jsonPath("$.last").exists());
		result.andExpect(jsonPath("$.totalPages").exists());
		result.andExpect(jsonPath("$.totalElements").exists());
		result.andExpect(jsonPath("$.first").exists());
		result.andExpect(jsonPath("$.numberOfElements").exists());
		result.andExpect(jsonPath("$.number").exists());
		result.andExpect(jsonPath("$.sort").exists());
		result.andExpect(jsonPath("$.size").exists());
		result.andExpect(jsonPath("$.empty").exists());
	}
	
	@Test
	public void findByIdShouldClientWhenIdExist() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());

	}
	
	@Test
	public void findByIdShouldThrowNotFoundExcepctionWhenIdDoesNotExist() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/clients/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.timestamp").exists());
		result.andExpect(jsonPath("$.status").exists());
		result.andExpect(jsonPath("$.error").exists());
		result.andExpect(jsonPath("$.path").exists());
	}
	
	
	@Test
	public void updateShouldClientWhenIdExist() throws Exception {
		
		ResultActions result = mockMvc.perform(put("/clients/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
	}
	
	@Test
	public void updateShouldThrowNotFoundExcepctionWhenIdDoesNotExistt() throws Exception {
		
		ResultActions result = mockMvc.perform(put("/clients/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.timestamp").exists());
		result.andExpect(jsonPath("$.status").exists());
		result.andExpect(jsonPath("$.error").exists());
		result.andExpect(jsonPath("$.path").exists());
	}
	
	@Test
	public void insertShouldReturnClientDTOCreated() throws Exception {
		
		ResultActions result = mockMvc.perform(post("/clients")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		
	}
	
	@Test
	public void deleteShouldClientWhenExist() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deleteShouldThrowNotFoundExcepctionWhenIdDoesNotExist() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", nonExistingId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.timestamp").exists());
		result.andExpect(jsonPath("$.status").exists());
		result.andExpect(jsonPath("$.error").exists());
		result.andExpect(jsonPath("$.path").exists());
	}
	
	@Test
	public void deleteShouldThrowDatabaseExcepctionWhenIdDoesNotExist() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", dependentId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("$.timestamp").exists());
		result.andExpect(jsonPath("$.status").exists());
		result.andExpect(jsonPath("$.error").exists());
		result.andExpect(jsonPath("$.path").exists());
	}
}
