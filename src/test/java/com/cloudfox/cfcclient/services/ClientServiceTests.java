package com.cloudfox.cfcclient.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cloudfox.cfcclient.dto.ClientDTO;
import com.cloudfox.cfclient.entities.Client;
import com.cloudfox.cfclient.repositories.ClientRepository;
import com.cloudfox.cfclient.services.ClientService;
import com.cloudfox.cfclient.services.exceptions.DatabaseException;
import com.cloudfox.cfclient.services.exceptions.ResourceNotFoundException;
import com.cloudfox.cfclient.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Client> page;
	private Client client;
	ClientDTO clientDTO;

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		client = Factory.createClient();
		clientDTO = Factory.createClientDTO();
		page = new PageImpl<Client>(List.of(client));
		
		//findAll
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		//findById
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(client));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		//insert and update
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(client);
		
		Mockito.when(repository.getOne(existingId)).thenReturn(client);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		//delete
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@InjectMocks
	private ClientService service;
	
	@Mock
	private ClientRepository repository;
	@Mock
	private ClientRepository clientRepository;
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ClientDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnClientsWhenIdExists() {
		ClientDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.findById(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}
	
	@Test
	public void insertShouldReturnClientsDTOWhenIdExists() {
		ClientDTO result = service.insert(clientDTO);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldReturnClientsDTOWhenIdExists() {
		ClientDTO result = service.update(existingId, clientDTO);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.update(nonExistingId, clientDTO);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.delete(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class,() -> {
			service.delete(dependentId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
}
