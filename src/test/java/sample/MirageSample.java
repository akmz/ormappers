package sample;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

import jp.sf.amateras.mirage.IterationCallback;
import jp.sf.amateras.mirage.SqlManager;
import jp.sf.amateras.mirage.SqlManagerImpl;
import jp.sf.amateras.mirage.provider.DataSourceConnectionProvider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sample.mirage.entity.User;

public class MirageSample extends AbstractSample {

    private SqlManager sqlManager;

    @Test
    public void selectById() throws Exception {
        User user = sqlManager.findEntity(User.class, 1L);
        assertEquals(user.name, "Alice");
    }

    @Test(dependsOnMethods = "selectById")
    public void insert() throws Exception {
        User newUser = new User();
        newUser.name = "Uragami";
        int result = sqlManager.insertEntity(newUser);
        assertEquals(result, 1);
        User inserted = sqlManager.findEntity(User.class, newUser.id);
        assertEquals(inserted.name, "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void update() throws Exception {
        User user = sqlManager.findEntity(User.class, 1L);
        user.name = "Uragami";
        int result = sqlManager.updateEntity(user);
        assertEquals(result, 1);
        User updatedUser = sqlManager.findEntity(User.class, 1L);
        assertEquals(updatedUser.name, "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void delete() throws Exception {
        User user = sqlManager.findEntity(User.class, 1L);
        int result = sqlManager.deleteEntity(user);
        assertEquals(result, 1);
        User deleted = sqlManager.findEntity(User.class, 1L);
        assertNull(deleted);
    }

    @Test(dependsOnMethods = "selectById")
    public void batchInsert() throws Exception {
        User[] users = { new User(), new User() };
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        int result = sqlManager.insertBatch(Arrays.asList(users));
        assertEquals(result, 2);
        assertEquals(
            sqlManager.findEntity(User.class, users[0].id).name,
            "Uragami");
        assertEquals(
            sqlManager.findEntity(User.class, users[1].id).name,
            "Taichi");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchUpdate() throws Exception {
        User[] users =
            {
                sqlManager.findEntity(User.class, 1L),
                sqlManager.findEntity(User.class, 2L) };
        assertEquals(users[0].name, "Alice");
        assertEquals(users[1].name, "Bob");
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        int result = sqlManager.updateBatch(Arrays.asList(users));
        assertEquals(result, 2);
        assertEquals(
            sqlManager.findEntity(User.class, users[0].id).name,
            "Uragami");
        assertEquals(
            sqlManager.findEntity(User.class, users[1].id).name,
            "Taichi");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchDelete() throws Exception {
        User[] users =
            {
                sqlManager.findEntity(User.class, 1L),
                sqlManager.findEntity(User.class, 2L) };
        assertEquals(users[0].name, "Alice");
        assertEquals(users[1].name, "Bob");
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        int result = sqlManager.deleteBatch(Arrays.asList(users));
        assertEquals(result, 2);
        assertNull(sqlManager.findEntity(User.class, users[0].id));
        assertNull(sqlManager.findEntity(User.class, users[1].id));
    }

    @Test(dependsOnMethods = "selectById")
    public void optimisticLock() throws Exception {
        throw new UnsupportedOperationException("optimisticLock");
    }

    @Test(dependsOnMethods = "selectById")
    public void pessimisticLock() throws Exception {
        throw new UnsupportedOperationException("pessimisticLock");
    }

    @Test(dependsOnMethods = "selectById")
    public void pagination() throws Exception {
        throw new UnsupportedOperationException("pagination");
    }

    @Test(dependsOnMethods = "selectById")
    public void iterationCallback() throws Exception {
        final Iterator<User> users =
            sqlManager.getResultList(
                User.class,
                "META-INF/sample/doma/dao/UserDao/selectAll.sql").iterator();
        Integer result =
            sqlManager.iterate(
                User.class,
                new IterationCallback<User, Integer>() {

                    int counter = 0;

                    @Override
                    public Integer iterate(User entity) {
                        assertEquals(entity.id, users.next().id);
                        counter++;
                        return counter;
                    }
                },
                "META-INF/sample/doma/dao/UserDao/selectAll.sql");
        assertEquals(result, Integer.valueOf(6));
        assertFalse(users.hasNext());
    }

    @Test(dependsOnMethods = "selectById")
    public void complexPrimaryKey() throws Exception {
        throw new UnsupportedOperationException("complexPrimaryKey");
    }

    @Test(dependsOnMethods = "selectById")
    public void oneToOne() throws Exception {
        throw new UnsupportedOperationException("oneToOne");
    }

    @Test(dependsOnMethods = "selectById")
    public void oneToMany() throws Exception {
        throw new UnsupportedOperationException("oneToMany");
    }

    @Test(dependsOnMethods = "selectById")
    public void manyToOne() throws Exception {
        throw new UnsupportedOperationException("manyToOne");
    }

    @Test(dependsOnMethods = "selectById")
    public void manyToMany() throws Exception {
        throw new UnsupportedOperationException("manyToMany");
    }

    @BeforeMethod(dependsOnMethods = "beforeMethod")
    public void setUpSqlManager() throws Exception {
        DataSourceConnectionProvider connectionProvider =
            new DataSourceConnectionProvider();
        connectionProvider.setDataSource(dataSource);

        sqlManager = new SqlManagerImpl();
        sqlManager.setConnectionProvider(connectionProvider);
    }

}
