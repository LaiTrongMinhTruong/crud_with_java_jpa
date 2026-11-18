package baitapdautien.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import baitapdautien.configs.JPAConfig;
import baitapdautien.dao.VideoDao;
import baitapdautien.models.Video;

public class VideoDaoImpl implements VideoDao {

	@Override
	public Video findById(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.find(Video.class, id);
		} finally {
			em.close();
		}
	}

	@Override
	public List<Video> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Video> query = em.createNamedQuery("Video.findAll", Video.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Video> findTopLatest(int count) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT v FROM Video v ORDER BY v.videoId DESC";
			TypedQuery<Video> query = em.createQuery(jpql, Video.class);
			query.setMaxResults(count);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Video> findByUserId(int userId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT v FROM Video v WHERE v.user.id = :userId ORDER BY v.videoId DESC";
			TypedQuery<Video> query = em.createQuery(jpql, Video.class);
			query.setParameter("userId", userId);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Video> findByUserIdWithPaging(int userId, int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT v FROM Video v WHERE v.user.id = :userId ORDER BY v.videoId DESC";
			TypedQuery<Video> query = em.createQuery(jpql, Video.class);
			query.setParameter("userId", userId);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public int countByUserId(int userId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT COUNT(v) FROM Video v WHERE v.user.id = :userId";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter("userId", userId);
			Long count = query.getSingleResult();
			return count != null ? count.intValue() : 0;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Video> findAllWithPaging(int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT v FROM Video v ORDER BY v.videoId DESC";
			TypedQuery<Video> query = em.createQuery(jpql, Video.class);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public int countAll() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT COUNT(v) FROM Video v";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			Long count = query.getSingleResult();
			return count != null ? count.intValue() : 0;
		} finally {
			em.close();
		}
	}

	@Override
	public void insert(Video video) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(video);
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
	public void update(Video video) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(video);
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
			Video video = em.find(Video.class, id);
			if (video != null) {
				em.remove(video);
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
	public void increaseViews(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			Video video = em.find(Video.class, id);
			if (video != null) {
				video.setViews(video.getViews() + 1);
				em.merge(video);
			}
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (trans.isActive()) {
				trans.rollback();
			}
		} finally {
			em.close();
		}
	}

	@Override
	public List<Video> search(String keyword) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT v FROM Video v WHERE v.title LIKE :kw OR v.description LIKE :kw ORDER BY v.videoId DESC";
			TypedQuery<Video> query = em.createQuery(jpql, Video.class);
			query.setParameter("kw", "%" + keyword + "%");
			return query.getResultList();
		} finally {
			em.close();
		}
	}
}
