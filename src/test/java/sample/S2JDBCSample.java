package sample;

import static org.seasar.extension.jdbc.operation.Operations.*;
import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.OptimisticLockException;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.SingletonS2Container;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sample.s2jdbc.entity.Book;
import sample.s2jdbc.entity.Complex;
import sample.s2jdbc.entity.Publisher;
import sample.s2jdbc.entity.User;

public class S2JDBCSample extends AbstractSample {

    private JdbcManager jdbcManager;

    @Test
    public void selectById() throws Exception {
        User user = jdbcManager.from(User.class).id(1L).getSingleResult();
        assertEquals(user.name, "Alice");
    }

    @Test(dependsOnMethods = "selectById")
    public void insert() throws Exception {
        User newUser = new User();
        newUser.name = "Uragami";
        int result = jdbcManager.insert(newUser).execute();
        assertEquals(result, 1);
        User inserted = jdbcManager
            .from(User.class)
            .id(newUser.id)
            .getSingleResult();
        assertEquals(inserted.name, "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void update() throws Exception {
        User user = jdbcManager.from(User.class).id(1L).getSingleResult();
        user.name = "Uragami";
        int result = jdbcManager.update(user).execute();
        assertEquals(result, 1);
        User updatedUser = jdbcManager
            .from(User.class)
            .id(1L)
            .getSingleResult();
        assertEquals(updatedUser.name, "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void delete() throws Exception {
        User user = jdbcManager.from(User.class).id(1L).getSingleResult();
        int result = jdbcManager.delete(user).execute();
        assertEquals(result, 1);
        User deleted = jdbcManager.from(User.class).id(1L).getSingleResult();
        assertNull(deleted);
    }

    @Test(dependsOnMethods = "selectById")
    public void batchInsert() throws Exception {
        User[] users = { new User(), new User() };
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        int[] result = jdbcManager.insertBatch(Arrays.asList(users)).execute();
        assertEquals(result.length, 2);
        assertEquals(result[0], 1);
        assertEquals(result[1], 1);
        assertEquals(jdbcManager
            .from(User.class)
            .id(users[0].id)
            .getSingleResult().name, "Uragami");
        assertEquals(jdbcManager
            .from(User.class)
            .id(users[1].id)
            .getSingleResult().name, "Taichi");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchUpdate() throws Exception {
        User[] users = { jdbcManager.from(User.class).id(1L).getSingleResult(),
                jdbcManager.from(User.class).id(2L).getSingleResult() };
        assertEquals(users[0].name, "Alice");
        assertEquals(users[1].name, "Bob");
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        int[] result = jdbcManager.updateBatch(Arrays.asList(users)).execute();
        assertEquals(result.length, 2);
        assertEquals(result[0], 1);
        assertEquals(result[1], 1);
        assertEquals(jdbcManager
            .from(User.class)
            .id(users[0].id)
            .getSingleResult().name, "Uragami");
        assertEquals(jdbcManager
            .from(User.class)
            .id(users[1].id)
            .getSingleResult().name, "Taichi");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchDelete() throws Exception {
        User[] users = { jdbcManager.from(User.class).id(1L).getSingleResult(),
                jdbcManager.from(User.class).id(2L).getSingleResult() };
        int[] result = jdbcManager.deleteBatch(Arrays.asList(users)).execute();
        assertEquals(result.length, 2);
        assertEquals(result[0], 1);
        assertEquals(result[1], 1);
        assertNull(jdbcManager
            .from(User.class)
            .id(users[0].id)
            .getSingleResult());
        assertNull(jdbcManager
            .from(User.class)
            .id(users[1].id)
            .getSingleResult());
    }

    @Test(dependsOnMethods = "selectById")
    public void optimisticLock() throws Exception {
        User user1 = jdbcManager.from(User.class).id(1L).getSingleResult();
        User user2 = jdbcManager.from(User.class).id(1L).getSingleResult();
        assertEquals(user1.version, Long.valueOf(1L));
        assertEquals(user2.version, Long.valueOf(1L));
        user1.name = "Uragami";
        jdbcManager.update(user1).execute();
        assertEquals(user1.version, Long.valueOf(2L));
        user2.name = "Taichi";
        try {
            jdbcManager.update(user2).execute();
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    @Test(dependsOnMethods = "selectById")
    public void pessimisticLock() throws Exception {
        jdbcManager.from(User.class).id(1L).forUpdate().getSingleResult();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            Future<Void> future = exec.submit(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    jdbcManager.from(User.class).id(1L).getSingleResult();
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
        List<User> allUsers = jdbcManager
            .from(User.class)
            .orderBy(asc("id"))
            .getResultList();
        List<User> users = jdbcManager
            .from(User.class)
            .orderBy(asc("id"))
            .offset(2)
            .limit(3)
            .getResultList();
        assertEquals(users.size(), 3);
        assertEquals(users.get(0).id, allUsers.get(2).id);
        assertEquals(users.get(1).id, allUsers.get(3).id);
        assertEquals(users.get(2).id, allUsers.get(4).id);
    }

    @Test(dependsOnMethods = "selectById")
    public void iterationCallback() throws Exception {
        final Iterator<User> users = jdbcManager
            .from(User.class)
            .orderBy(asc("id"))
            .getResultList()
            .iterator();

        Integer result = jdbcManager
            .from(User.class)
            .orderBy(asc("id"))
            .iterate(new IterationCallback<User, Integer>() {

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
        Complex complex = jdbcManager
            .from(Complex.class)
            .id(1L, 3L)
            .getSingleResult();
        assertNotNull(complex);
        assertEquals(complex.val, "c");
    }

    @Test(dependsOnMethods = "selectById")
    public void oneToOne() throws Exception {
        List<Publisher> publishers = jdbcManager
            .from(Publisher.class)
            .innerJoin("address")
            .getResultList();
        for (Publisher publisher : publishers) {
            assertNotNull(publisher);
            assertNotNull(publisher.address);
        }
    }

    @Test(dependsOnMethods = "selectById")
    public void oneToMany() throws Exception {
        List<Publisher> publishers = jdbcManager
            .from(Publisher.class)
            .innerJoin("books")
            .getResultList();
        for (Publisher publisher : publishers) {
            assertNotNull(publisher);
            assertNotNull(publisher.books);
        }
    }

    @Test(dependsOnMethods = "selectById")
    public void manyToOne() throws Exception {
        List<Book> books = jdbcManager
            .from(Book.class)
            .innerJoin("publisher")
            .getResultList();
        for (Book book : books) {
            assertNotNull(book);
            assertNotNull(book.publisher);
        }
    }

    @Test(dependsOnMethods = "selectById")
    public void manyToMany() throws Exception {
        throw new UnsupportedOperationException("manyToMany");
    }

    @BeforeMethod(dependsOnMethods = "beforeMethod")
    public void setUpJdbcManager() throws Exception {
        jdbcManager = SingletonS2Container.getComponent(JdbcManager.class);
    }
}
