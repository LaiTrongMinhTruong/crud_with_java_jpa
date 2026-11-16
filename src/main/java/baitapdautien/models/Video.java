package baitapdautien.models;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "videos")
@NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v ORDER BY v.videoId DESC")
public class Video implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "video_id")
	private int videoId;

	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "poster", length = 255)
	private String poster;

	@Column(name = "url", length = 500)
	private String url;

	@Column(name = "views")
	private int views = 0;

	@Column(name = "active")
	private boolean active = true;

	@Column(name = "created_date")
	private Date createdDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	public Video() {
		super();
	}

	public Video(int videoId, String title, String description, String poster, String url, int views, boolean active,
			Date createdDate, User user) {
		super();
		this.videoId = videoId;
		this.title = title;
		this.description = description;
		this.poster = poster;
		this.url = url;
		this.views = views;
		this.active = active;
		this.createdDate = createdDate;
		this.user = user;
	}

	public Video(String title, String description, String poster, String url, int views, boolean active,
			Date createdDate, User user) {
		super();
		this.title = title;
		this.description = description;
		this.poster = poster;
		this.url = url;
		this.views = views;
		this.active = active;
		this.createdDate = createdDate;
		this.user = user;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public int getId() {
		return videoId;
	}

	public void setId(int videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public boolean isActive() {
		return active;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Video [videoId=" + videoId + ", title=" + title + ", poster=" + poster + ", views=" + views
				+ ", active=" + active + ", createdDate=" + createdDate + "]";
	}
}
