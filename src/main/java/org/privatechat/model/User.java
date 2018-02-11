package org.privatechat.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = { "email" }))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@NotNull(message = "valid email required")
	@Email(message = "valid email required")
	public String email;

	@NotNull(message = "valid password required")
	public String password;

	@NotNull(message = "valid name required")
	public String fullName;

	public String role;

	public boolean isPresent;

	public User() {
	}

	public User(long id) {
		this.id = id;
	}

	public User(String email, String fullName, String password, String role) {
		this.email = email;
		this.fullName = fullName;
		this.password = password;
		this.role = role;
	}
}