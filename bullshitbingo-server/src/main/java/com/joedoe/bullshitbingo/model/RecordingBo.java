package com.joedoe.bullshitbingo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

public class RecordingBo {

	@NotNull
	private String _id;
	
	@NotNull
	private String _rev;
	
	@NotNull	
	private List<String> words;
	
	@NotNull
	private long timestampOfRecording;

	public RecordingBo(@NotNull String... words) {
		this(new ArrayList<>(Arrays.asList(words)), new Date());
	}

	public RecordingBo(List<String> words) {
		this(words, new Date());
	}

	public RecordingBo(@NotNull List<String> words,
			@NotNull Date timestampOfRecording) {
		Preconditions.checkNotNull(words);
		Preconditions.checkNotNull(timestampOfRecording);
		setWords(words);
		this.timestampOfRecording = timestampOfRecording.getTime();
	}
		
	public RecordingBo(@NotNull List<String> words,
			@NotNull long timestampOfRecording) {
		Preconditions.checkNotNull(words);
		Preconditions.checkNotNull(timestampOfRecording);
		setWords(words);
		this.timestampOfRecording = timestampOfRecording;
	}

	public @NotNull List<String> getWords() {
		return words;
	}

	public @NotNull long getTimestampOfRecording() {
		return timestampOfRecording;
	}

	public @NotNull Date getTimestampOfRecordingAsDate() {
		return new Date(getTimestampOfRecording());
	}
	
	public void setWords(@NotNull List<String> words) {
		Preconditions.checkNotNull(words);
		this.words = words;
	}

	public void setTimestampOfRecording(@NotNull long timestampOfRecording) {
		Preconditions.checkNotNull(timestampOfRecording);
		this.timestampOfRecording = timestampOfRecording;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "RecordingBo [_id="
				+ _id
				+ ", _rev="
				+ _rev
				+ ", words="
				+ (words != null ? words.subList(0,
						Math.min(words.size(), maxLen)) : null)
				+ ", timestampOfRecording=" + timestampOfRecording + "]";
	}

	public static Comparator<RecordingBo> COMPARATOR_BY_TIMESTAMP_ASC = new Comparator<RecordingBo>() {

		@Override
		public int compare(RecordingBo o1, RecordingBo o2) {
			if (o1.timestampOfRecording < o2.timestampOfRecording) return -1;
			else if (o1.timestampOfRecording == o2.timestampOfRecording) return 0;
			else return 1;
		}

	};
}