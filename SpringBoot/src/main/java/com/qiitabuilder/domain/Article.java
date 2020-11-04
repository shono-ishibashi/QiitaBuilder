package com.qiitabuilder.domain;



import java.time.LocalDateTime;
import java.util.List;

public class Article {
	private Integer articleId;
	private User postedUser;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String title;

	private String content;

	private Integer stateFlag;

	private String qiitaArticleId;

	private List<Tag> tags;

	private List<Feedback> feedbacks;

	private Integer qiitaRecommendPoint;

	private Integer registeredMyArticleCount;

	private Integer feedbackCount;
}
