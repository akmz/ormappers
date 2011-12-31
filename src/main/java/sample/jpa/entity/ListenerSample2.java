package sample.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@EntityListeners({ ListenerSample2Listener.class })
@Table(name = "listener_sample")
public class ListenerSample2 implements Serializable {

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

	@Override
	public String toString() {
		return "ListenerSample2 [id="
			+ id
			+ ", val="
			+ val
			+ ", version="
			+ version
			+ "]";
	}

}
