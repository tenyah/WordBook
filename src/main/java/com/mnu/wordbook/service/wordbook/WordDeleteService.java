package com.mnu.wordbook.service.wordbook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.model.WordBookDAO;
import com.mnu.wordbook.service.Action;

public class WordDeleteService implements Action {

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	int wbId = Integer.parseInt(request.getParameter("wbId"));
        String[] wordItems = request.getParameterValues("wordItem"); 

        WordBookDAO dao = WordBookDAO.getInstnace();

        if (wordItems != null) {
            dao.deleteWords(wbId, wordItems);
        }

        response.sendRedirect(request.getContextPath() + "/WordBook?cmd=wordbooklist&wbId=" + wbId);
    }
}