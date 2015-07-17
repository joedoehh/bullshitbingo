package com.joedoe.bullshitbingo.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

public class MarkedWord {
	
	@NotNull
	private String word;
	
	private boolean marked;

	public MarkedWord(@NotNull String word) {
		this(word, false);
	}	
	
	public MarkedWord(@NotNull String word, boolean marked) {
		Preconditions.checkNotNull(word);
		this.word = word;
		this.marked = marked;
	}

	public static List<MarkedWord> fromWords(List<String> words) {
		List<MarkedWord> markedWords = new ArrayList<MarkedWord>();
		for (String word : words)
			markedWords.add(new MarkedWord(word));
		return markedWords;
	}
	
	
	public String getWord() {
		return word;
	}

	public boolean isMarked() {
		return marked;
	}
	
	public MarkedWord setMarked(boolean marked) {
		this.marked = marked;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (marked ? 1231 : 1237);
		result = prime * result + ((word == null) ? 0 : word.hashCode());
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
		MarkedWord other = (MarkedWord) obj;
		if (marked != other.marked)
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MarkedWord [word=" + word + ", marked=" + marked + "]";
	}
	
}
