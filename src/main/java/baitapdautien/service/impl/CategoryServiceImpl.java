package baitapdautien.service.impl;

import java.io.File;
import java.util.List;

import baitapdautien.dao.CategoryDao;
import baitapdautien.dao.impl.CategoryDaoImpl;
import baitapdautien.models.Category;
import baitapdautien.service.CategoryService;
import baitapdautien.util.Constant;

public class CategoryServiceImpl implements CategoryService {
	CategoryDao categoryDao = new CategoryDaoImpl();

	@Override
	public void insert(Category category) {
		categoryDao.insert(category);
	}

	@Override
	public void edit(Category newCategory) {
		Category oldCategory = categoryDao.get(newCategory.getId());
		if (oldCategory != null) {
			oldCategory.setName(newCategory.getName());
			// Chỉ xóa ảnh cũ và cập nhật ảnh mới KHI có ảnh mới upload và KHÁC ảnh cũ
			if (newCategory.getIcon() != null && !newCategory.getIcon().isEmpty()
					&& !newCategory.getIcon().equals(oldCategory.getIcon())) {
				String fileName = oldCategory.getIcon();
				if (fileName != null && !fileName.equals("avata.png")) {
					File file = new File(Constant.DIR + File.separator + fileName);
					if (file.exists()) {
						file.delete();
					}
				}
				oldCategory.setIcon(newCategory.getIcon());
			}
			categoryDao.edit(oldCategory);
		} else {
			categoryDao.edit(newCategory);
		}
	}

	@Override
	public void delete(int id) {
		Category oldCategory = categoryDao.get(id);
		if (oldCategory != null && oldCategory.getIcon() != null && !oldCategory.getIcon().equals("avata.png")) {
			File file = new File(Constant.DIR + File.separator + oldCategory.getIcon());
			if (file.exists()) {
				file.delete();
			}
		}
		categoryDao.delete(id);
	}

	@Override
	public Category get(int id) {
		return categoryDao.get(id);
	}

	@Override
	public Category get(String name) {
		return categoryDao.get(name);
	}

	@Override
	public List<Category> getAll() {
		return categoryDao.getAll();
	}

	@Override
	public List<Category> search(String keyword) {
		return categoryDao.search(keyword);
	}

	@Override
	public Category findById(int id) {
		return get(id);
	}

	@Override
	public void update(Category category) {
		edit(category);
	}
}
