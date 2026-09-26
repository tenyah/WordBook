package com.mnu.wordbook.service.wordstudy;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.model.WordBookDAO;
import com.mnu.wordbook.model.WordBookListDTO;
import com.mnu.wordbook.service.Action;

public class WordStudyMainService implements Action {

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        WordBookDAO dao = WordBookDAO.getInstnace();
        List<WordBookListDTO> wordbookList = dao.getWordbookList();

        request.setAttribute("wordbookList", wordbookList);

        RequestDispatcher rd = request.getRequestDispatcher("/wordstudy/wordstudy.jsp");
        rd.forward(request, response);
    }
}