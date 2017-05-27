package vn.khanhpdt.luceneplayground.moviesearch;

import java.util.Date;

class Movie {

	private String title;
	private String summary;
	private String storyline;
	private String[] genres;
	private Rating rating;
	private Date releaseDate;
	private String[] directors;
	private String[] stars;

	String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}

	String getSummary() {
		return summary;
	}

	void setSummary(String summary) {
		this.summary = summary;
	}

	String getStoryline() {
		return storyline;
	}

	void setStoryline(String storyline) {
		this.storyline = storyline;
	}

	String[] getGenres() {
		return genres;
	}

	void setGenres(String[] genres) {
		this.genres = genres;
	}

	Rating getRating() {
		return rating;
	}

	void setRating(Rating rating) {
		this.rating = rating;
	}

	Date getReleaseDate() {
		return releaseDate;
	}

	void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	String[] getDirectors() {
		return directors;
	}

	void setDirectors(String[] directors) {
		this.directors = directors;
	}

	String[] getStars() {
		return stars;
	}

	void setStars(String[] stars) {
		this.stars = stars;
	}

	static class Rating {
		private double score;
		private long count;

		double getScore() {
			return score;
		}

		void setScore(double score) {
			this.score = score;
		}

		long getCount() {
			return count;
		}

		void setCount(long count) {
			this.count = count;
		}
	}
}
