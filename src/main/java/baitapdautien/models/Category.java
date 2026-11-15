package baitapdautien.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "category")
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c")
public class Category implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cate_id")
	private int cateid;

	@Column(name = "cate_name", columnDefinition = "VARCHAR(255)")
	private String catename;

	@Column(name = "icons", columnDefinition = "VARCHAR(255)")
	private String icon;

	public Category() {
		super();
	}

	public Category(int cateid, String catename, String icon) {
		super();
		this.cateid = cateid;
		this.catename = catename;
		this.icon = icon;
	}

	public Category(String catename, String icon) {
		super();
		this.catename = catename;
		this.icon = icon;
	}

	// Getters and Setters cho cateid
	public int getCateid() {
		return cateid;
	}

	public void setCateid(int cateid) {
		this.cateid = cateid;
	}

	public int getCateId() {
		return cateid;
	}

	public void setCateId(int cateid) {
		this.cateid = cateid;
	}

	public int getId() {
		return cateid;
	}

	public void setId(int cateid) {
		this.cateid = cateid;
	}

	// Getters and Setters cho catename
	public String getCatename() {
		return catename;
	}

	public void setCatename(String catename) {
		this.catename = catename;
	}

	public String getCateName() {
		return catename;
	}

	public void setCateName(String catename) {
		this.catename = catename;
	}

	public String getName() {
		return catename;
	}

	public void setName(String catename) {
		this.catename = catename;
	}

	// Getters and Setters cho icon
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcons() {
		return icon;
	}

	public void setIcons(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "Category [cateid=" + cateid + ", catename=" + catename + ", icon=" + icon + "]";
	}
}
