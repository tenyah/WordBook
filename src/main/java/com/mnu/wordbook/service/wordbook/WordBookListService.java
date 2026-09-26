package com.mnu.wordbook.service.wordbook;

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

public class WordBookListService implements Action {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 int wbId = Integer.parseInt(request.getParameter("wbId"));

	        WordBookDAO wbDao = WordBookDAO.getInstnace();
	        List<WordBookListDTO> wordbookList = wbDao.getWordbookList();
	        WordBookListDTO selectedWordbook = wbDao.wordlist(wbId);

	        WordDAO wordDao = WordDAO.getInstnace();
	        List<WordDTO> wordList = wordDao.wordBookData(wbId);

	        request.setAttribute("wordbookList", wordbookList);
	        request.setAttribute("selectedWordbook", selectedWordbook);
	        request.setAttribute("wordList", wordList);
	        request.setAttribute("selectedWbId", wbId);

	        RequestDispatcher rd = request.getRequestDispatcher("/wordbook/wordbook.jsp");
	        rd.forward(request, response);
	    }
	

}
