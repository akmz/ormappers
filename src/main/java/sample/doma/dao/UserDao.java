package sample.doma.dao;

import java.util.List;

import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

import sample.doma.entity.User;

@Dao
public interface UserDao {

    @Select
    User selectById(Long id);

    @Select
    User selectById(Long id, SelectOptions selectOptions);

    @Select
    List<User> selectAll();

    @Select
    List<User> selectAll(SelectOptions selectOptions);

    @Select(iterate = true)
    Integer selectAll(IterationCallback<Integer, User> iterationCallback);

    @Insert
    int insert(User entity);

    @Update
    int update(User entity);

    @Delete
    int delete(User entity);

    @BatchInsert
    int[] batchInsert(List<User> entities);

    @BatchUpdate
    int[] batchUpdate(List<User> entities);

    @BatchDelete
    int[] batchDelete(List<User> entities);

}
