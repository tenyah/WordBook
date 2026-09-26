package com.mnu.wordbook.service.wordbook;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.model.WordBookDAO;
import com.mnu.wordbook.service.Action;

public class WordInsertService implements Action {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		int wordId = Integer.parseInt(request.getParameter("wordId"));
        String[] bookIds = request.getParameterValues("bookId[]");
        String keyword = request.getParameter("keyword");

        WordBookDAO dao = WordBookDAO.getInstnace();
        int result = 0;

        if (bookIds != null) {
            for (String bookIdStr : bookIds) {
                int bookId = Integer.parseInt(bookIdStr);
                result = result + dao.wordInsert(wordId, bookId);
            }
        }

        response.getWriter().append(String.valueOf(result));
    }

}
