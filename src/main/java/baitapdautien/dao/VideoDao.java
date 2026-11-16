package baitapdautien.dao;

import java.util.List;

import baitapdautien.models.Video;

public interface VideoDao {

	Video findById(int id);

	List<Video> findAll();

	List<Video> findTopLatest(int count);

	List<Video> findByUserId(int userId);

	List<Video> findByUserIdWithPaging(int userId, int page, int pageSize);

	int countByUserId(int userId);

	List<Video> findAllWithPaging(int page, int pageSize);

	int countAll();

	void insert(Video video);

	void update(Video video);

	void delete(int id);

	void increaseViews(int id);
}
