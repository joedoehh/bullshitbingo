package com.joedoe.bullshitbingo.model;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.base.Preconditions;

public class Game {

	@NotNull
	private String player;

	@NotNull
	@Min(0)
	@Max(5)
	private int rating;

	@NotNull
	@Size(min = 5, max = 5)
	private List<MarkedWord> words;

	public Game(@NotNull String player,			
			@NotNull @Size(min = 5, max = 5) List<String> words) {
		this(player, 0, MarkedWord.fromWords(words));
	}	
	
	public Game(@NotNull String player,
			@NotNull @Min(0) @Max(5) Integer rating,
			@NotNull @Size(min = 5, max = 5) List<MarkedWord> words) {
		Preconditions.checkNotNull(player);
		Preconditions.checkNotNull(rating);
		Preconditions.checkArgument(rating <= 5);
		Preconditions.checkArgument(rating >= 0);
		Preconditions.checkNotNull(player);
		Preconditions.checkArgument(words.size() == 5);
		this.player = player;
		this.rating = rating;
		this.words = words;
	}

	public String getPlayer() {
		return player;
	}

	public int getRating() {
		return rating;
	}

	public List<MarkedWord> getWords() {
		return words;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + ((words == null) ? 0 : words.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (words == null) {
			if (other.words != null)
				return false;
		} else if (!words.equals(other.words))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "Game [player="
				+ player
				+ ", rating="
				+ rating
				+ ", words="
				+ (words != null ? words.subList(0,
						Math.min(words.size(), maxLen)) : null) + "]";
	}

}
