package com.mnu.wordbook.service.wordbook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.model.WordBookDAO;
import com.mnu.wordbook.model.WordDAO;
import com.mnu.wordbook.model.WordDTO;
import com.mnu.wordbook.service.Action;

public class CustomWordInsertService implements Action {

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        WordDTO wDTO = new WordDTO();
        wDTO.setWord(request.getParameter("word"));
        wDTO.setHuri(request.getParameter("huri"));
        wDTO.setMean(request.getParameter("mean"));
        wDTO.setKanji(request.getParameter("kanji"));
        wDTO.setKormean(request.getParameter("kormean"));
        wDTO.setKorsound(request.getParameter("korsound"));

        int wbId = Integer.parseInt(request.getParameter("wbId"));

        WordDAO wordDao = WordDAO.getInstnace();
        int newWordId = wordDao.insertCustomWord(wDTO);

        WordBookDAO wbDao = WordBookDAO.getInstnace();
        wbDao.wordInsert(newWordId, wbId, "C");

        response.sendRedirect(request.getContextPath() + "/WordBook?cmd=wordbooklist&wbId=" + wbId);
    }
}