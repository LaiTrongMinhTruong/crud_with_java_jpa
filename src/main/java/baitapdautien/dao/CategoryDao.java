package baitapdautien.dao;

import java.util.List;

import baitapdautien.models.Category;

public interface CategoryDao {
	void insert(Category category);

	void edit(Category category);

	void delete(int id);

	Category get(int id);

	Category get(String name);

	List<Category> getAll();

	List<Category> search(String keyword);
}
