package sample.jpa.entity;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class ListenerSample2Listener {

	@PrePersist
	public void prePersist(ListenerSample2 entity) {
		entity.getMessages().add("PrePersist: " + entity);
	}

	@PostPersist
	public void postPersist(ListenerSample2 entity) {
		entity.getMessages().add("PostPersist: " + entity);
	}

	@PreUpdate
	public void preUpdate(ListenerSample2 entity) {
		entity.getMessages().add("PreUpdate: " + entity);
	}

	@PostUpdate
	public void postUpdate(ListenerSample2 entity) {
		entity.getMessages().add("PostUpdate: " + entity);
	}

	@PreRemove
	public void preRemove(ListenerSample2 entity) {
		entity.getMessages().add("PreRemove: " + entity);
	}

	@PostRemove
	public void postRemove(ListenerSample2 entity) {
		entity.getMessages().add("PostRemove: " + entity);
	}

	@PostLoad
	public void postLoad(ListenerSample2 entity) {
		entity.getMessages().add("PostLoad: " + entity);
	}

}