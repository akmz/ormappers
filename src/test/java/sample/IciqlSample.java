package sample;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sample.iciql.entity.User;

import com.iciql.Db;

public class IciqlSample extends AbstractSample {

    private Db db;

    @Test
    public void selectById() throws Exception {
        User alias = new User();
        User user = db
            .from(alias)
            .where(alias.id)
            .is(1L)
            .or(alias.name)
            .is("Uragami")
            .selectFirst();
        assertEquals(user.name, "Alice");
    }

    @Test(dependsOnMethods = "selectById")
    public void insert() throws Exception {
        User newUser = new User();
        long key = db.insertAndGetKey(newUser);
        User alias = new User();
        User inserted = db.from(alias).where(alias.id).is(key).selectFirst();
        assertEquals(inserted.name, "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void update() throws Exception {
        User alias = new User();
        User user = db.from(alias).where(alias.id).is(1L).selectFirst();
        user.name = "Uragami";
        int result = db.update(user);
        assertEquals(result, 1);
        User updatedUser = db.from(alias).where(alias.id).is(1L).selectFirst();
        assertEquals(updatedUser.name, "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void delete() throws Exception {
        User alias = new User();
        User user = db.from(alias).where(alias.id).is(1L).selectFirst();
        int result = db.delete(user);
        assertEquals(result, 1);
        User deleted = db.from(alias).where(alias.id).is(1L).selectFirst();
        assertNull(deleted);
    }

    @Test(dependsOnMethods = "selectById")
    public void batchInsert() throws Exception {
        User[] users = { new User(), new User() };
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        List<Long> keys = db.insertAllAndGetKeys(Arrays.asList(users));
        assertEquals(keys.size(), 2);
        User alias = new User();
        assertEquals(db
            .from(alias)
            .where(alias.id)
            .is(keys.get(0))
            .selectFirst().name, "Uragami");
        assertEquals(db
            .from(alias)
            .where(alias.id)
            .is(keys.get(1))
            .selectFirst().name, "Taichi");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchUpdate() throws Exception {
        User alias = new User();
        User[] users = { db.from(alias).where(alias.id).is(1L).selectFirst(),
                db.from(alias).where(alias.id).is(2L).selectFirst() };
        assertEquals(users[0].name, "Alice");
        assertEquals(users[1].name, "Bob");
        users[0].name = "Uragami";
        users[1].name = "Taichi";
        db.updateAll(Arrays.asList(users));
        assertEquals(db
            .from(alias)
            .where(alias.id)
            .is(users[0].id)
            .selectFirst().name, "Uragami");
        assertEquals(db
            .from(alias)
            .where(alias.id)
            .is(users[1].id)
            .selectFirst().name, "Taichi");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchDelete() throws Exception {
        User alias = new User();
        User[] users = { db.from(alias).where(alias.id).is(1L).selectFirst(),
                db.from(alias).where(alias.id).is(2L).selectFirst() };
        db.deleteAll(Arrays.asList(users));
        assertNull(db.from(alias).where(alias.id).is(users[0].id).selectFirst());
        assertNull(db.from(alias).where(alias.id).is(users[1].id).selectFirst());
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
        throw new UnsupportedOperationException("iterationCallback");
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

    @BeforeMethod
    public void setUp() throws Exception {
        db = Db.open(dataSource);
    }
}
