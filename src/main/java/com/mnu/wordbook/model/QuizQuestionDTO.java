package com.mnu.wordbook.model;

import java.util.List;

public class QuizQuestionDTO {

    private String word;
    private String huri;
    private String kanji;
    private String kormean;
    private String korsound;
    private String mean;
	private String answer;
    private List<String> choices;
    private List<String> hints;
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
	 public String getMean() {
		return mean;
	}
	public void setMean(String mean) {
		this.mean = mean;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public List<String> getChoices() {
		return choices;
	}
	public void setChoices(List<String> choices) {
		this.choices = choices;
	}
	public List<String> getHints() {
		return hints;
	}
	public void setHints(List<String> hints) {
		this.hints = hints;
	}
	
}