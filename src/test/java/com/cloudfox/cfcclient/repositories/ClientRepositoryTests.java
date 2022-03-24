package com.cloudfox.cfcclient.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.cloudfox.cfclient.entities.Client;
import com.cloudfox.cfclient.repositories.ClientRepository;
import com.cloudfox.cfclient.tests.Factory;

@DataJpaTest
public class ClientRepositoryTests {
	
	@Autowired
	private ClientRepository repository;
	
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
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
		long expectedCountTotalCategories = countTotalCategories + 1;
		Client client = Factory.createClient();
		client.setId(null);
		client = repository.save(client); 
		Assertions.assertNotNull(client.getId());
		Assertions.assertEquals(expectedCountTotalCategories, client.getId());
	}
	
	@Test
	public void findByIdShouldFindOptionalObjectWhenIdExists(){
		Optional<Client> client = repository.findById(existingId);
		Assertions.assertTrue(client.isPresent());
	}
	
	@Test
	public void findByIdShouldNotFindOptionalObjectWhenIdInvalid(){
		Optional<Client> client = repository.findById(nonExistingId);
		Assertions.assertFalse(client.isPresent());
	}
	
	@Test
	public void deleteByIdShouldDeleteObjectWhenIdExists(){
		repository.deleteById(existingId);
		Optional<Client> client = repository.findById(existingId);
		Assertions.assertFalse(client.isPresent());
	}
	
	@Test
	public void deleteByIdShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
}
