package sample;

import org.testng.annotations.Test;

public class MyBatisSample extends AbstractSample {

    @Test
    public void selectById() throws Exception {
        throw new UnsupportedOperationException("selectById");
    }

    @Test(dependsOnMethods = "selectById")
    public void insert() throws Exception {
        throw new UnsupportedOperationException("insert");
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

}
