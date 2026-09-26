package com.mnu.wordbook.model;

public class WordDTO {
	private int id;
	private String word;
	private String huri;
	private String mean;
	private String kanji;
	private String kormean;
	private String korsound;
	private String wordType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getHuri() {
		return huri;
	}
	public void setHuri(String huri) {
		this.huri = huri;
	}
	public String getMean() {
		return mean;
	}
	public void setMean(String mean) {
		this.mean = mean;
	}
	public String getKanji() {
		return kanji;
	}
	public void setKanji(String kanji) {
		this.kanji = kanji;
	}
	public String getKormean() {
		return kormean;
	}
	public void setKormean(String kormean) {
		this.kormean = kormean;
	}
	public String getKorsound() {
		return korsound;
	}
	public void setKorsound(String korsound) {
		this.korsound = korsound;
	}
	public String getWordType() {
		return wordType;
	}
	public void setWordType(String wordType) {
		this.wordType = wordType;
	}
}
