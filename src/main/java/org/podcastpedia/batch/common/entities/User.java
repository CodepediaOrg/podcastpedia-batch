package org.podcastpedia.batch.common.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email")
	private String email;

	@Column(name="name")
	private String name;
	
//	@Column(name="first_name")
//	private String firstName;
//
//	@Column(name="last_name")
//	private String lastName;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name="podcasts_email_subscribers",
			joinColumns={@JoinColumn(name="email")},
			inverseJoinColumns={@JoinColumn(name="podcast_id")}
	)		
    private List<Podcast> podcasts;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Podcast> getPodcasts() {
		return podcasts;
	}

	public void setPodcasts(List<Podcast> podcasts) {
		this.podcasts = podcasts;
	}	
		
}
