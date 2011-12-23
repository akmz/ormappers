package sample.doma.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import sample.doma.entity.Complex;

@Dao
public interface ComplexDao {

    @Select
    Complex selectById(Long id1, Long id2);

}
