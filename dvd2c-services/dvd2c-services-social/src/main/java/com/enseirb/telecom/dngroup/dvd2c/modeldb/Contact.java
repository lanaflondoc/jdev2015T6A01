package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;

import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The persistent class for the contacts database table.
 * 
 */
@Entity
@Table(name = "contacts")
@NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c")
public class Contact extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	// @Column(name = "inverse_id")
	// private int inverseId;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private ReceiverActor receiverActor;

	@Column(name = "owner_id", length = 36)
	@Type(type = "uuid-char")
	private UUID ownerId;

	private int status;

	// @Column(name = "ties_count")
	// private int tiesCount;

//	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "contact_role", joinColumns = @JoinColumn(name = "contact_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	public Contact() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	// public int getInverseId() {
	// return this.inverseId;
	// }
	//
	// public void setInverseId(int inverseId) {
	// this.inverseId = inverseId;
	// }
	//
	// public int getTiesCount() {
	// return this.tiesCount;
	// }
	//
	// public void setTiesCount(int tiesCount) {
	// this.tiesCount = tiesCount;
	// }

	public List<Role> getRole() {
		return this.roles;
	}

	public void setRole(List<Role> role) {
		this.roles = role;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ReceiverActor getReceiverActor() {
		return receiverActor;
	}

	public void setReceiverActor(ReceiverActor receiverActor) {
		this.receiverActor = receiverActor;
	}

	public UUID getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(UUID ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @param contact
	 * @return
	 */
	public ContactXSD toContactXSD() {
		ReceiverActor receiverActor = getReceiverActor();
		ContactXSD contactXSD = new ContactXSD();
		contactXSD.setActorID(receiverActor.getEmail());
		contactXSD.setAprouve(getStatus());
		contactXSD.setFirstname(receiverActor.getFirstname());
		contactXSD.setSurname(receiverActor.getSurname());
		contactXSD.setUuid(receiverActor.getId().toString());

		contactXSD.getRole().addAll(getRolesNames(getRole()));

		return contactXSD;
	}

	/**
	 * @param roles
	 * @return
	 */
	public static List<String> getRolesNames(List<Role> roles) {
		List<String> roleName = new ArrayList<String>();
		for (Role role : roles) {
			roleName.add(role.getName());
		}
		return roleName;
	}

}