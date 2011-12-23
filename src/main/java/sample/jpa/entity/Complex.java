package sample.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "complex")
public class Complex implements Serializable {

    @EmbeddedId
    private ComplexId complexId;

    @Column(name = "val")
    private String val;

    public void setComplexId(ComplexId complexId) {
        this.complexId = complexId;
    }

    public ComplexId getComplexId() {
        return complexId;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

}
