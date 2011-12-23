package sample.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ComplexId implements Serializable {

    @Column(name = "id1")
    private Long id1;

    @Column(name = "id2")
    private Long id2;

    public ComplexId() {
    }

    public ComplexId(Long id1, Long id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

}
