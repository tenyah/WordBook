package com.mnu.wordbook.service.wordsearch;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.service.Action;

public class WordSearchMainService implements Action {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/wordsearch/wordsearch.jsp");
		rd.forward(request, response);

	}

}
