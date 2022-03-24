package com.cloudfox.cfclient.tests;

import java.time.Instant;

import com.cloudfox.cfcclient.dto.ClientDTO;
import com.cloudfox.cfclient.entities.Client;

public class Factory {

	public static Client createClient() {
		return new Client(1L,"Claudio Raposo", "12345678900", 230.0,  1, Instant.parse("2020-07-13T20:50:07.12345Z"));
	}

	public static ClientDTO createClientDTO() {
		Client category = createClient();
		return new ClientDTO(category);
	}
}
