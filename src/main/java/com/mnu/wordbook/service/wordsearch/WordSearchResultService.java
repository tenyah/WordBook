package com.mnu.wordbook.service.wordsearch;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.model.WordBookDAO;
import com.mnu.wordbook.model.WordBookListDTO;
import com.mnu.wordbook.model.WordDAO;
import com.mnu.wordbook.model.WordDTO;
import com.mnu.wordbook.service.Action;

public class WordSearchResultService implements Action {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		 String keyword = request.getParameter("keyword");

	        WordDAO dao = WordDAO.getInstnace();
	        List<WordDTO> wordList = dao.wordSearch(keyword);

	        WordBookDAO wbDao = WordBookDAO.getInstnace();
	        List<WordBookListDTO> wordbookList = wbDao.getWordbookList();

	        request.setAttribute("wordList", wordList);
	        request.setAttribute("wordbookList", wordbookList);

	        RequestDispatcher rd = request.getRequestDispatcher("/wordsearch/wordsearchresult.jsp");
	        rd.forward(request, response);
	    }

}
