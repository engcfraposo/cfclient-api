package com.cloudfox.cfcclient.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.cloudfox.cfcclient.dto.ClientDTO;
import com.cloudfox.cfclient.repositories.ClientRepository;
import com.cloudfox.cfclient.services.ClientService;
import com.cloudfox.cfclient.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ClientServiceIT {
	
	@Autowired
	private ClientService service;
	
	@Autowired
	private ClientRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalClients;
	
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 3L;
		nonExistingId = 1000L;
		countTotalClients = 10L;		
	}
	
	@Test
	public void deleteShouldCreateIndependentClientDTOAndDeleteResourceWhenIdExists() {
		service.delete(4L);
		Assertions.assertEquals(existingId - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10() {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ClientDTO> result = service.findAllPaged(pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalClients, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExists() {
		
		PageRequest pageRequest = PageRequest.of(50, 10);
		Page<ClientDTO> result = service.findAllPaged(pageRequest);
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnOrderedPageWhenSortByName() {
		
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		Page<ClientDTO> result = service.findAllPaged(pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalClients, result.getTotalElements());
		Assertions.assertEquals("Claudio Raposo", result.getContent().get(0).getName());
		Assertions.assertEquals("João Farias", result.getContent().get(1).getName());
		Assertions.assertEquals("Laurindo Farias", result.getContent().get(2).getName());

	}
}
