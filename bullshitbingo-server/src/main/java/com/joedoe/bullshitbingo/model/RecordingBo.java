package com.joedoe.bullshitbingo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

@Entity
@Table(name = "RECORDINGS")
@NamedQueries(value = {
		@NamedQuery(name = "listRecordings", query = "SELECT r FROM RecordingBo r ORDER BY r.timestampOfRecording"),
		@NamedQuery(name = "listRecordingsAfterRecordingTimestamp", query = "SELECT r FROM RecordingBo r WHERE r.timestampOfRecording > :timestampOfRecording ORDER BY r.timestampOfRecording"),
		@NamedQuery(name = "deleteRecordings", query = "DELETE FROM RecordingBo"),
		@NamedQuery(name = "deleteRecordingById", query = "DELETE FROM RecordingBo r WHERE r.id =:id")})
public class RecordingBo {

	@Id
	@GeneratedValue
	private Long id;

	@ElementCollection
	@CollectionTable(name = "RECORDED_WORDS", joinColumns = @JoinColumn(name = "RECORDING_ID"))
	@Column(name = "WORD")
	@NotNull
	private List<String> words = new ArrayList<String>();

	@Basic(optional = false)
	@NotNull
	private long timestampOfRecording;

	public RecordingBo() {
	}

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "RecordingBo [id="
				+ id
				+ ", words="
				+ (words != null ? words.subList(0,
						Math.min(words.size(), maxLen)) : null)
				+ ", timestampOfRecording=" + timestampOfRecording + "]";
	}

}