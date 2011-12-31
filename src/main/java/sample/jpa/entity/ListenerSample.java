package sample.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name = "listener_sample")
public class ListenerSample implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "val")
	private String val;

	@Version
	@Column(name = "version")
	private Long version;

	@Transient
	private List<String> messages = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public List<String> getMessages() {
		return messages;
	}

	@PrePersist
	public void prePersist() {
		messages.add("PrePersist: " + this);
	}

	@PostPersist
	public void postPersist() {
		messages.add("PostPersist: " + this);
	}

	@PreUpdate
	public void preUpdate() {
		messages.add("PreUpdate: " + this);
	}

	@PostUpdate
	public void postUpdate() {
		messages.add("PostUpdate: " + this);
	}

	@PreRemove
	public void preRemove() {
		messages.add("PreRemove: " + this);
	}

	@PostRemove
	public void postRemove() {
		messages.add("PostRemove: " + this);
	}

	@PostLoad
	public void postLoad() {
		messages.add("PostLoad: " + this);
	}

	@Override
	public String toString() {
		return "ListenerSample [id="
			+ id
			+ ", val="
			+ val
			+ ", version="
			+ version
			+ "]";
	}

}
