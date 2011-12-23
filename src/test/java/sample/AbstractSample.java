package sample;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.TransactionManager;

import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractSample {

    protected DataSource dataSource;

    protected TransactionManager tm;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        SingletonS2ContainerFactory.init();
        tm = SingletonS2Container.getComponent(TransactionManager.class);
        tm.begin();
        dataSource = SingletonS2Container.getComponent(DataSource.class);
        try (Connection con = dataSource.getConnection();
                Statement st = con.createStatement()) {
            st.execute("RUNSCRIPT FROM 'init.sql'");
        }
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        try {
            if (tm.getStatus() == Status.STATUS_ACTIVE) {
                tm.commit();
            } else {
                tm.rollback();
            }
        } finally {
            SingletonS2ContainerFactory.destroy();
        }
    }
}
