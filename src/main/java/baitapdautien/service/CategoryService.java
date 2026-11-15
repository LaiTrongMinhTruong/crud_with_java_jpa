package baitapdautien.service;

import java.util.List;

import baitapdautien.models.Category;

public interface CategoryService {
	void insert(Category category);

	void edit(Category category);

	void delete(int id);

	Category get(int id);

	Category get(String name);

	List<Category> getAll();

	List<Category> search(String keyword);

	// Các phương thức được gọi trong Controller (Slide 23, 24)
	Category findById(int id);

	void update(Category category);
}
