package sample;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DomaAbstractConfig;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sample.doma.dao.ComplexDao;
import sample.doma.dao.UserDao;
import sample.doma.dao.impl.ComplexDaoImpl;
import sample.doma.dao.impl.UserDaoImpl;
import sample.doma.entity.Complex;
import sample.doma.entity.User;

public class DomaSample extends AbstractSample {

    private UserDao userDao;

    private ComplexDao complexDao;

    private Config config;

    @Test
    public void selectById() throws Exception {
        User user = userDao.selectById(1L);
        assertEquals(user.name, "Alice");
    }

    @Test(dependsOnMethods = "selectById")
    public void insert() throws Exception {
        User newUser = new User();
        newUser.name = "Uragami";
        int result = userDao.insert(newUser);
        assertEquals(result, 1);
        User inserted = userDao.selectById(newUser.id);
        assertEquals(inserted.name, "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void update() throws Exception {
        User user = userDao.selectById(1L);
        user.name = "Uragami";
        int result = userDao.update(user);
        assertEquals(result, 1);
        User updatedUser = userDao.selectById(1L);
        assertEquals(updatedUser.name, "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void delete() throws Exception {
        User user = userDao.selectById(1L);
        int result = userDao.delete(user);
        assertEquals(result, 1);
        User deleted = userDao.selectById(1L);
        assertNull(deleted);
    }

    @Test(dependsOnMethods = "selectById")
    public void batchInsert() throws Exception {
        User[] users = { new User(), new User() };
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        int[] result = userDao.batchInsert(Arrays.asList(users));
        assertEquals(result.length, 2);
        assertEquals(result[0], 1);
        assertEquals(result[1], 1);
        assertEquals(userDao.selectById(users[0].id).name, "Uragami");
        assertEquals(userDao.selectById(users[1].id).name, "Taichi");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchUpdate() throws Exception {
        User[] users = { userDao.selectById(1L), userDao.selectById(2L) };
        assertEquals(users[0].name, "Alice");
        assertEquals(users[1].name, "Bob");
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        int[] result = userDao.batchUpdate(Arrays.asList(users));
        assertEquals(result.length, 2);
        assertEquals(result[0], 1);
        assertEquals(result[1], 1);
        assertEquals(userDao.selectById(users[0].id).name, "Uragami");
        assertEquals(userDao.selectById(users[1].id).name, "Taichi");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchDelete() throws Exception {
        User[] users = { userDao.selectById(1L), userDao.selectById(2L) };
        assertEquals(users[0].name, "Alice");
        assertEquals(users[1].name, "Bob");
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        int[] result = userDao.batchDelete(Arrays.asList(users));
        assertEquals(result.length, 2);
        assertEquals(result[0], 1);
        assertEquals(result[1], 1);
        assertNull(userDao.selectById(users[0].id));
        assertNull(userDao.selectById(users[1].id));
    }

    @Test(dependsOnMethods = "selectById")
    public void optimisticLock() throws Exception {
        User user1 = userDao.selectById(1L);
        User user2 = userDao.selectById(1L);
        assertEquals(user1.version, Long.valueOf(1L));
        assertEquals(user2.version, Long.valueOf(1L));
        user1.name = "Uragami";
        userDao.update(user1);
        assertEquals(user1.version, Long.valueOf(2L));
        user2.name = "Taichi";
        try {
            userDao.update(user2);
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    @Test(dependsOnMethods = "selectById")
    public void pessimisticLock() throws Exception {
        SelectOptions selectOptions = SelectOptions.get().forUpdate();
        userDao.selectById(1L, selectOptions);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            Future<Void> future = exec.submit(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    new UserDaoImpl(config).selectById(1L);
                    return null;
                }
            });
            future.get();
            fail();
        } catch (ExecutionException expected) {
        } finally {
            exec.shutdownNow();
        }
    }

    @Test(dependsOnMethods = "selectById")
    public void pagination() throws Exception {
        List<User> allUsers = userDao.selectAll();
        SelectOptions selectOptions = SelectOptions.get().offset(2).limit(3);
        List<User> users = userDao.selectAll(selectOptions);
        assertEquals(users.size(), 3);
        assertEquals(users.get(0).id, allUsers.get(2).id);
        assertEquals(users.get(1).id, allUsers.get(3).id);
        assertEquals(users.get(2).id, allUsers.get(4).id);
    }

    @Test(dependsOnMethods = "selectById")
    public void iterationCallback() throws Exception {
        final Iterator<User> users = userDao.selectAll().iterator();
        Integer result = userDao
            .selectAll(new IterationCallback<Integer, User>() {

                int counter = 0;

                @Override
                public Integer iterate(User target, IterationContext context) {
                    assertEquals(target.id, users.next().id);
                    counter++;
                    return counter;
                }
            });
        assertEquals(result, Integer.valueOf(6));
        assertFalse(users.hasNext());
    }

    @Test(dependsOnMethods = "selectById")
    public void complexPrimaryKey() throws Exception {
        Complex complex = complexDao.selectById(1L, 3L);
        assertNotNull(complex);
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
    public void setUpDao() throws Exception {
        final Dialect dialect = new H2Dialect();
        Config config = new DomaAbstractConfig() {

            @Override
            public Dialect getDialect() {
                return dialect;
            }

            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        };
        userDao = new UserDaoImpl(config);
        complexDao = new ComplexDaoImpl(config);
    }

}