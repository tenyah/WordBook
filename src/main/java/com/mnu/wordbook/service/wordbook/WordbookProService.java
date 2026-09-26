package com.mnu.wordbook.service.wordbook;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.model.WordBookDAO;
import com.mnu.wordbook.service.Action;

public class WordbookProService implements Action {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("name");

	    WordBookDAO dao = WordBookDAO.getInstnace();
	    int result = dao.newWordbook(name);

	    request.setAttribute("result", result);

	    RequestDispatcher rd = request.getRequestDispatcher("/wordbook/wordbook_alert.jsp");
	    rd.forward(request, response);
 
    }
		

}
