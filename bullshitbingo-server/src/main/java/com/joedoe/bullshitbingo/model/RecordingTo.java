package com.joedoe.bullshitbingo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

public class RecordingTo {
	
	private long id;
	private List<String> words;
	private String wordsAsString;
	private Date timestampOfRecording;

	public RecordingTo(@NotNull RecordingBo recordingBo) {
		Preconditions.checkNotNull(recordingBo);
		this.id = recordingBo.getId();
		this.words = recordingBo.getWords();
		this.timestampOfRecording = new Date(recordingBo.getTimestampOfRecording());
		this.wordsAsString = convertToStringOfWords(this.words);
	}

	public static List<RecordingTo> transferToTo(List<RecordingBo> recordings) {
		List<RecordingTo> returnValue = new ArrayList<RecordingTo>(
				recordings.size());
		for (RecordingBo bo : recordings)
			returnValue.add(new RecordingTo(bo));
		return returnValue;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public String getWordsAsString() {
		return wordsAsString;
	}

	public void setWordsAsString(String wordsAsString) {
		this.wordsAsString = wordsAsString;
	}

	public Date getTimestampOfRecording() {
		return timestampOfRecording;
	}

	public void setTimestampOfRecording(Date timestampOfRecording) {
		this.timestampOfRecording = timestampOfRecording;
	}

	private String convertToStringOfWords(List<String> words) {
		String returnValue = "";
		for (String word : words)
			returnValue += word + " ";
		return returnValue.trim();
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "RecordingTo [id="
				+ id
				+ ", words="
				+ (words != null ? words.subList(0,
						Math.min(words.size(), maxLen)) : null)
				+ ", wordsAsString=" + wordsAsString
				+ ", timestampOfRecording=" + timestampOfRecording + "]";
	}

}