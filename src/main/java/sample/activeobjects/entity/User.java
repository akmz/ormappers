package sample.activeobjects.entity;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("user")
public interface User extends Entity {

    String getName();

    void setName(String name);

    Long getVersion();

    void setVersion(Long version);

}