package sample.s2jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "complex")
public class Complex implements Serializable {

    @Id
    @Column(name = "id1")
    public Long id1;

    @Id
    @Column(name = "id2")
    public Long id2;

    @Column(name = "val")
    public String val;
}
