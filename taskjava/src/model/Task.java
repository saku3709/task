package model;

import java.time.LocalDate;

public class Task {

	public static final class Entity {
		public static final String TBL_TASK = "TASK";
		public static final String COL_ID = "id";
		public static final String COL_TITLE = "title";
		public static final String COL_CONTENT = "content";
		public static final String COL_STATE = "state";
		public static final String COL_START_DATE = "start_date";
		public static final String COL_LIMIT_DATE = "limit_date";
		public static final String COL_CATEGORY = "category";
		public static final String COL_USER_ID = "user_id";
	}

	private int id;
	private String title;
	private String content;
	private int state;
	private LocalDate startDate;
	private LocalDate limitDate;
	private String category;
	private String userId;

	public Task() {
	}

	public Task(int id, String title, String content, int state, LocalDate startDate, LocalDate limitDate,
			String category, String userId) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.state = state;
		this.startDate = startDate;
		this.limitDate = limitDate;
		this.category = category;
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(LocalDate limitDate) {
		this.limitDate = limitDate;

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", content=" + content + ", state=" + state + ", startDate="
				+ startDate + ", limitDate=" + limitDate + ", category=" + category + ", userId=" + userId + "]";
	}

}
