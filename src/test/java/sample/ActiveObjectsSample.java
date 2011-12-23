package sample;

import static org.testng.Assert.*;
import net.java.ao.EntityManager;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sample.activeobjects.entity.User;

public class ActiveObjectsSample extends AbstractSample {

    private EntityManager em;

    @Test
    public void selectById() throws Exception {
        User user = em.get(User.class, 1);
        assertEquals(user.getName(), "Alice");
    }

    @Test(dependsOnMethods = "selectById")
    public void insert() throws Exception {
        User newUser = em.create(User.class);
        newUser.setName("Uragami");
        newUser.save();
        User inserted = em.get(User.class, newUser.getID());
        assertEquals(inserted.getName(), "Uragami");
    }

    @Test(dependsOnMethods = "selectById")
    public void update() throws Exception {
        throw new UnsupportedOperationException("update");
    }

    @Test(dependsOnMethods = "selectById")
    public void delete() throws Exception {
        throw new UnsupportedOperationException("delete");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchInsert() throws Exception {
        throw new UnsupportedOperationException("batchInsert");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchUpdate() throws Exception {
        throw new UnsupportedOperationException("batchUpdate");
    }

    @Test(dependsOnMethods = "selectById")
    public void batchDelete() throws Exception {
        throw new UnsupportedOperationException("batchDelete");
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

    @BeforeMethod(dependsOnMethods = "beforeMethod")
    public void setUpEntityManager() throws Exception {
    }
}
