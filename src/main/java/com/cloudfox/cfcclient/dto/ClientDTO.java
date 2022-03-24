package com.cloudfox.cfcclient.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.cloudfox.cfclient.entities.Client;

public class ClientDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Instant birthDate;
	private Integer children;
	private String cpf;
	private Long id;
	private Double income;
	private String name;

	public ClientDTO() {
	}

	public ClientDTO(Client entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.cpf = entity.getCpf();
		this.income = entity.getIncome();
		this.children = entity.getChildren();
		this.birthDate = entity.getBirthDate();
	}

	public ClientDTO(Long id, String name, String cpf, Double income, Integer children, Instant bithDate) {
		this.id = id;
		this.name = name;
		this.cpf = cpf;
		this.income = income;
		this.children = children;
		this.birthDate = bithDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		ClientDTO other = (ClientDTO) obj;
		return Objects.equals(id, other.id);
	}

	public Instant getBirthDate() {
		return birthDate;
	}

	public Integer getChildren() {
		return children;
	}

	public String getCpf() {
		return cpf;
	}

	public Long getId() {
		return id;
	}

	public Double getIncome() {
		return income;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public void setBirthDate(Instant birthDate) {
		this.birthDate = birthDate;
	}

	public void setChildren(Integer children) {
		this.children = children;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	public void setName(String name) {
		this.name = name;
	}

}