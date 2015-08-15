package com.joedoe.bullshitbingo.recorder.model;

import java.util.ArrayList;
import java.util.List;

import com.sun.istack.internal.NotNull;

public class Recording {

	private String id;
	private List<String> words = new ArrayList<String>();
	
	public Recording(@NotNull String id) {
		if (null == id) {
			throw new IllegalArgumentException("id is not allowed to be null");
		}
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public void addWord(@NotNull String word) {
		if (null == word) {
			throw new IllegalArgumentException("word is not allowed to be null");
		}
		words.add(word);
	}
	
	public @NotNull List<String> getWords() {
		return words;
	}	
	
	@Override
	public String toString() {
		final int maxLen = 10;
		return "Recording [id="
				+ id
				+ ", words="
				+ (words != null ? words.subList(0,
						Math.min(words.size(), maxLen)) : null) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Recording other = (Recording) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}		

}
