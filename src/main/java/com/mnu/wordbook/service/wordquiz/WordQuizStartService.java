package com.mnu.wordbook.service.wordquiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.model.QuizQuestionDTO;
import com.mnu.wordbook.model.WordDAO;
import com.mnu.wordbook.model.WordDTO;
import com.mnu.wordbook.service.Action;

public class WordQuizStartService implements Action {

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int wbId = Integer.parseInt(request.getParameter("wbId"));
        String mode = request.getParameter("mode");
        int count = Integer.parseInt(request.getParameter("count"));

        String[] hintArr = request.getParameterValues("hint");
        List<String> hints = (hintArr != null) ? Arrays.asList(hintArr) : new ArrayList<>();

        WordDAO wordDao = WordDAO.getInstnace();
        List<WordDTO> wordList = wordDao.wordBookData(wbId);

        // 한자 맞추기 낱개 한자 단위로 문제 생성 
        if ("kanji".equals(mode)) {

            List<HanjaItem> hanjaItems = buildHanjaItems(wordList);

            if (hanjaItems.isEmpty()) {
                request.setAttribute("errorMessage", "단어장에 한자가 포함된 단어가 없습니다");
                RequestDispatcher rd = request.getRequestDispatcher("/wordquiz/wordquiz_alert.jsp");
                rd.forward(request, response);
                return;
            }

            if (count > hanjaItems.size()) {
                count = hanjaItems.size();
            }

            List<HanjaItem> shuffledHanja = new ArrayList<>(hanjaItems);
            Collections.shuffle(shuffledHanja);

            List<QuizQuestionDTO> questions = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                HanjaItem answerItem = shuffledHanja.get(i);

                // 오답 후보 같은 한자는 제외
                List<HanjaItem> others = new ArrayList<>();
                for (HanjaItem h : hanjaItems) {
                    if (!h.hanja.equals(answerItem.hanja)) {
                        others.add(h);
                    }
                }
                Collections.shuffle(others);

                String answerText = answerItem.meaning + " " + answerItem.sound;

                List<String> choices = new ArrayList<>();
                choices.add(answerText);
                for (int j = 0; j < 3 && j < others.size(); j++) {
                    HanjaItem o = others.get(j);
                    choices.add(o.meaning + " " + o.sound);
                }
                Collections.shuffle(choices);

                QuizQuestionDTO q = new QuizQuestionDTO();
                q.setKanji(answerItem.hanja);        // 화면 중앙에 크게 보여줄 한자 한 글자
                q.setWord(answerItem.sourceWord);
                q.setHuri(answerItem.sourceHuri);
                q.setMean(answerItem.sourceMean);
                q.setAnswer(answerText);
                q.setChoices(choices);
                q.setHints(hints);

                questions.add(q);
            }

            request.setAttribute("questions", questions);
            request.setAttribute("mode", mode);

            RequestDispatcher rd = request.getRequestDispatcher("/wordquiz/wordquizplay.jsp");
            rd.forward(request, response);
            return;
        }

        // 단어 맞추기 + 후리가나 힌트 조합이면, 한자 없는 단어 제외
        if ("word".equals(mode) && hints.contains("huri")) {
            List<WordDTO> filtered = new ArrayList<>();
            for (WordDTO w : wordList) {
                if (w.getWord() != null && !w.getWord().equals(w.getHuri())) {
                    filtered.add(w);
                }
            }
            wordList = filtered;

            if (wordList.isEmpty()) {
                request.setAttribute("errorMessage", "이 조건에 맞는 단어가 없습니다");
                RequestDispatcher rd = request.getRequestDispatcher("/wordquiz/wordquiz_alert.jsp");
                rd.forward(request, response);
                return;
            }
        }

        if (count > wordList.size()) {
            count = wordList.size();
        }

        List<WordDTO> shuffled = new ArrayList<>(wordList);
        Collections.shuffle(shuffled);

        List<QuizQuestionDTO> questions = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            WordDTO answer = shuffled.get(i);

            List<WordDTO> others = new ArrayList<>(wordList);
            others.remove(answer);
            Collections.shuffle(others);

            List<String> choices = new ArrayList<>();
            choices.add(getAnswerText(answer, mode));

            for (int j = 0; j < 3 && j < others.size(); j++) {
                choices.add(getAnswerText(others.get(j), mode));
            }

            Collections.shuffle(choices);

            QuizQuestionDTO q = new QuizQuestionDTO();
            q.setWord(answer.getWord());
            q.setHuri(answer.getHuri());
            q.setKanji(answer.getKanji());
            q.setKormean(answer.getKormean());
            q.setKorsound(answer.getKorsound());
            q.setMean(answer.getMean());
            q.setAnswer(getAnswerText(answer, mode));
            q.setChoices(choices);
            q.setHints(hints);

            questions.add(q);
        }

        request.setAttribute("questions", questions);
        request.setAttribute("mode", mode);

        RequestDispatcher rd = request.getRequestDispatcher("/wordquiz/wordquizplay.jsp");
        rd.forward(request, response);
    }

    private String getAnswerText(WordDTO w, String mode) {
        if ("word".equals(mode)) {
            return w.getWord();
        } else {
            return w.getMean();
        }
    }

    // 단어장의 단어들에서 한자가 있는 것만 골라 낱개 한자 목록으로 쪼갬
    private List<HanjaItem> buildHanjaItems(List<WordDTO> wordList) {
        List<HanjaItem> list = new ArrayList<>();

        for (WordDTO w : wordList) {
            if (w.getKanji() == null || w.getKanji().trim().isEmpty()) {
                continue;
            }

            String[] kanjiArr = w.getKanji().split(",");
            String[] meanArr = (w.getKormean() != null) ? w.getKormean().split(",") : new String[0];
            String[] soundArr = (w.getKorsound() != null) ? w.getKorsound().split(",") : new String[0];

            int len = Math.min(kanjiArr.length, Math.min(meanArr.length, soundArr.length));

            for (int i = 0; i < len; i++) {
                HanjaItem item = new HanjaItem();
                item.hanja = kanjiArr[i].trim();
                item.meaning = meanArr[i].trim();
                item.sound = soundArr[i].trim();
                item.sourceWord = w.getWord();
                item.sourceHuri = w.getHuri();
                item.sourceMean = w.getMean();
                list.add(item);
            }
        }

        return list;
    }

    // 낱개 한자 하나를 표현하는 내부 전용 클래스
    private static class HanjaItem {
        String hanja;
        String meaning;
        String sound;
        String sourceWord;
        String sourceHuri;
        String sourceMean;
    }
}