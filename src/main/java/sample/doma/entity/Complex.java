package sample.doma.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "complex")
public class Complex {

    @Id
    @Column(name = "id1")
    public Long id1;

    @Id
    @Column(name = "id2")
    public Long id2;

    @Column(name = "val")
    public String val;
}
