package baitapdautien.service.impl;

import java.util.List;

import baitapdautien.dao.VideoDao;
import baitapdautien.dao.impl.VideoDaoImpl;
import baitapdautien.models.Video;
import baitapdautien.service.VideoService;

public class VideoServiceImpl implements VideoService {

	private final VideoDao videoDao = new VideoDaoImpl();

	@Override
	public Video findById(int id) {
		return videoDao.findById(id);
	}

	@Override
	public List<Video> findAll() {
		return videoDao.findAll();
	}

	@Override
	public List<Video> findTop10Latest() {
		return videoDao.findTopLatest(10);
	}

	@Override
	public List<Video> findByUserId(int userId) {
		return videoDao.findByUserId(userId);
	}

	@Override
	public List<Video> findByUserIdWithPaging(int userId, int page, int pageSize) {
		return videoDao.findByUserIdWithPaging(userId, page, pageSize);
	}

	@Override
	public int countByUserId(int userId) {
		return videoDao.countByUserId(userId);
	}

	@Override
	public List<Video> findAllWithPaging(int page, int pageSize) {
		return videoDao.findAllWithPaging(page, pageSize);
	}

	@Override
	public int countAll() {
		return videoDao.countAll();
	}

	@Override
	public void insert(Video video) {
		videoDao.insert(video);
	}

	@Override
	public void update(Video video) {
		videoDao.update(video);
	}

	@Override
	public void delete(int id) {
		videoDao.delete(id);
	}

	@Override
	public void increaseViews(int id) {
		videoDao.increaseViews(id);
	}

	@Override
	public List<Video> search(String keyword) {
		return videoDao.search(keyword);
	}
}
