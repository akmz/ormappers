package sample;

import static org.testng.Assert.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sample.jpa.entity.Author;
import sample.jpa.entity.Book;
import sample.jpa.entity.Complex;
import sample.jpa.entity.ComplexId;
import sample.jpa.entity.ListenerSample;
import sample.jpa.entity.ListenerSample2;
import sample.jpa.entity.Publisher;
import sample.jpa.entity.User;

public class JPASample extends AbstractSample {

	private EntityManagerFactory emf;

	private EntityManager em;

	private EntityManager em2;

	private EntityTransaction tx;

	private EntityTransaction tx2;

	@Test
	public void selectById() throws Exception {
		User user = em.find(User.class, 1L);
		assertEquals(user.getName(), "Alice");
	}

	@Test(dependsOnMethods = "selectById")
	public void insert() throws Exception {
		User newUser = new User();
		newUser.setName("Uragami");
		em.persist(newUser);
		em.flush();
		em.clear();
		User inserted = em.find(User.class, newUser.getId());
		assertEquals(inserted.getName(), "Uragami");
	}

	@Test(dependsOnMethods = "selectById")
	public void update() throws Exception {
		User user = em.find(User.class, 1L);
		user.setName("Uragami");
		em.flush();
		em.clear();
		User updatedUser = em.find(User.class, 1L);
		assertEquals(updatedUser.getName(), "Uragami");
	}

	@Test(dependsOnMethods = "selectById")
	public void delete() throws Exception {
		User user = em.find(User.class, 1L);
		em.remove(user);
		em.flush();
		User deleted = em.find(User.class, 1L);
		assertNull(deleted);
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
		User user1 = em.find(User.class, 1L);
		User user2 = em2.find(User.class, 1L);
		assertEquals(user1.getVersion(), Long.valueOf(1L));
		assertEquals(user2.getVersion(), Long.valueOf(1L));
		user1.setName("Uragami");
		em.flush();
		assertEquals(user1.getVersion(), Long.valueOf(2L));
		user2.setName("Taichi");
		try {
			em2.flush();
			fail();
		} catch (OptimisticLockException expected) {
		}
	}

	@Test(dependsOnMethods = "selectById")
	public void pessimisticLock() throws Throwable {
		em.find(User.class, 1L, LockModeType.PESSIMISTIC_READ);
		ExecutorService exec = Executors.newSingleThreadExecutor();
		try {
			Future<Void> future = exec.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					em2.find(User.class, 1L);
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
		List<User> allUsers =
			em
				.createQuery(
					"select u from User u order by u.id asc",
					User.class)
				.getResultList();
		List<User> users =
			em
				.createQuery(
					"select u from User u order by u.id asc",
					User.class)
				.setFirstResult(2)
				.setMaxResults(3)
				.getResultList();
		assertEquals(users.size(), 3);
		assertEquals(users.get(0).getId(), allUsers.get(2).getId());
		assertEquals(users.get(1).getId(), allUsers.get(3).getId());
		assertEquals(users.get(2).getId(), allUsers.get(4).getId());
	}

	@Test(dependsOnMethods = "selectById")
	public void iterationCallback() throws Exception {
		throw new UnsupportedOperationException("iterationCallback");
	}

	@Test(dependsOnMethods = "selectById")
	public void complexPrimaryKey() throws Exception {
		Object primaryKey = new ComplexId(1L, 3L);
		Complex complex = em.find(Complex.class, primaryKey);
		assertNotNull(complex);
		assertEquals(complex.getVal(), "c");
	}

	@Test(dependsOnMethods = "selectById")
	public void oneToOne() throws Exception {
		List<Publisher> publishers =
			em
				.createQuery("select p from Publisher p", Publisher.class)
				.getResultList();
		for (Publisher publisher : publishers) {
			assertNotNull(publisher);
			assertNotNull(publisher.getAddress());
		}
	}

	@Test(dependsOnMethods = "selectById")
	public void oneToMany() throws Exception {
		List<Publisher> publishers =
			em
				.createQuery("select p from Publisher p", Publisher.class)
				.getResultList();
		for (Publisher publisher : publishers) {
			assertNotNull(publisher);
			assertNotNull(publisher.getBooks());
		}
	}

	@Test(dependsOnMethods = "selectById")
	public void manyToOne() throws Exception {
		List<Book> books =
			em.createQuery("select b from Book b", Book.class).getResultList();
		for (Book book : books) {
			assertNotNull(book);
			assertNotNull(book.getPublisher());
		}
	}

	@Test(dependsOnMethods = "selectById")
	public void manyToMany() throws Exception {
		Book book =
			em
				.createQuery(
					"select b from Book b where b.id = :bookId",
					Book.class)
				.setParameter("bookId", 1L)
				.getSingleResult();
		assertNotNull(book);
		assertEquals(book.getAuthors().size(), 2);
		Author author =
			em
				.createQuery(
					"select a from Author a where a.id = :authorId",
					Author.class)
				.setParameter("authorId", 1L)
				.getSingleResult();
		assertNotNull(author);
		assertEquals(author.getBooks().size(), 2);
	}

	@Test
	public void lifecycleCallbackMethods() throws Exception {
		ListenerSample entity = new ListenerSample();
		assertTrue(entity.getMessages().isEmpty());
		entity.setVal("foo");

		em.persist(entity);
		em.flush();

		assertEquals(entity.getMessages().size(), 2);
		assertEquals(
			entity.getMessages().get(0),
			"PrePersist: ListenerSample [id=null, val=foo, version=null]");
		assertEquals(
			entity.getMessages().get(1),
			"PostPersist: ListenerSample [id=1, val=foo, version=1]");

		entity.setVal("bar");
		em.flush();

		assertEquals(entity.getMessages().size(), 4);
		assertEquals(
			entity.getMessages().get(2),
			"PreUpdate: ListenerSample [id=1, val=bar, version=1]");
		assertEquals(
			entity.getMessages().get(3),
			"PostUpdate: ListenerSample [id=1, val=bar, version=2]");

		em.clear();
		entity = em.find(ListenerSample.class, 1L);
		assertEquals(entity.getMessages().size(), 1);
		assertEquals(
			entity.getMessages().get(0),
			"PostLoad: ListenerSample [id=1, val=bar, version=2]");

		em.remove(entity);
		em.flush();
		assertEquals(entity.getMessages().size(), 3);
		assertEquals(
			entity.getMessages().get(1),
			"PreRemove: ListenerSample [id=1, val=bar, version=2]");
		assertEquals(
			entity.getMessages().get(2),
			"PostRemove: ListenerSample [id=1, val=bar, version=2]");
	}

	@Test
	public void lifecycleCallbackMethodsWithEntityListenerClass()
			throws Exception {
		ListenerSample2 entity = new ListenerSample2();
		assertTrue(entity.getMessages().isEmpty());
		entity.setVal("foo");

		em.persist(entity);
		em.flush();

		assertEquals(entity.getMessages().size(), 2);
		assertEquals(
			entity.getMessages().get(0),
			"PrePersist: ListenerSample2 [id=null, val=foo, version=null]");
		assertEquals(
			entity.getMessages().get(1),
			"PostPersist: ListenerSample2 [id=1, val=foo, version=1]");

		entity.setVal("bar");
		em.flush();

		assertEquals(entity.getMessages().size(), 4);
		assertEquals(
			entity.getMessages().get(2),
			"PreUpdate: ListenerSample2 [id=1, val=bar, version=1]");
		assertEquals(
			entity.getMessages().get(3),
			"PostUpdate: ListenerSample2 [id=1, val=bar, version=2]");

		em.clear();
		entity = em.find(ListenerSample2.class, 1L);
		assertEquals(entity.getMessages().size(), 1);
		assertEquals(
			entity.getMessages().get(0),
			"PostLoad: ListenerSample2 [id=1, val=bar, version=2]");

		em.remove(entity);
		em.flush();
		assertEquals(entity.getMessages().size(), 3);
		assertEquals(
			entity.getMessages().get(1),
			"PreRemove: ListenerSample2 [id=1, val=bar, version=2]");
		assertEquals(
			entity.getMessages().get(2),
			"PostRemove: ListenerSample2 [id=1, val=bar, version=2]");
	}

	@BeforeMethod(dependsOnMethods = "beforeMethod")
	public void setUpEntityManager() throws Exception {
		emf = Persistence.createEntityManagerFactory("sampleUnit");
		em = emf.createEntityManager();
		tx = em.getTransaction();
		tx.begin();
		em2 = emf.createEntityManager();
		tx2 = em2.getTransaction();
		tx2.begin();
	}

	@AfterMethod
	public void tearDownEntityManager() throws Exception {
		if (tx.getRollbackOnly()) {
			tx.rollback();
		} else {
			tx.commit();
		}
		em.close();
		if (tx2.getRollbackOnly()) {
			tx2.rollback();
		} else {
			tx2.commit();
		}
		em2.close();
		emf.close();
	}
}
