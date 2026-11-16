package baitapdautien.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import baitapdautien.configs.JPAConfig;
import baitapdautien.dao.UserDao;
import baitapdautien.models.User;

public class UserDaoImpl implements UserDao {

	@Override
	public User get(String username) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.userName = :username";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter("username", username);
			List<User> list = query.getResultList();
			if (!list.isEmpty()) {
				return list.get(0);
			}
			return null;
		} finally {
			em.close();
		}
	}

	@Override
	public User findById(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.find(User.class, id);
		} finally {
			em.close();
		}
	}

	@Override
	public List<User> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public void insert(User user) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(user);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (trans.isActive()) {
				trans.rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public void update(User user) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(user);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (trans.isActive()) {
				trans.rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			User user = em.find(User.class, id);
			if (user != null) {
				em.remove(user);
			}
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (trans.isActive()) {
				trans.rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean checkExistEmail(String email) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter("email", email);
			Long count = query.getSingleResult();
			return count != null && count > 0;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean checkExistUsername(String username) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT COUNT(u) FROM User u WHERE u.userName = :username";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter("username", username);
			Long count = query.getSingleResult();
			return count != null && count > 0;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean checkExistPhone(String phone) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT COUNT(u) FROM User u WHERE u.phone = :phone";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter("phone", phone);
			Long count = query.getSingleResult();
			return count != null && count > 0;
		} finally {
			em.close();
		}
	}

	@Override
	public User findByEmail(String email) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.email = :email";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter("email", email);
			List<User> list = query.getResultList();
			return list.isEmpty() ? null : list.get(0);
		} finally {
			em.close();
		}
	}

	@Override
	public User findByCode(String code) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.code = :code";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter("code", code);
			List<User> list = query.getResultList();
			return list.isEmpty() ? null : list.get(0);
		} finally {
			em.close();
		}
	}

	@Override
	public User findByUsernameOrEmail(String keyword) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.userName = :kw OR u.email = :kw";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter("kw", keyword);
			List<User> list = query.getResultList();
			return list.isEmpty() ? null : list.get(0);
		} finally {
			em.close();
		}
	}
}
