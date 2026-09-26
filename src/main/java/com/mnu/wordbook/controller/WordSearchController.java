package com.mnu.wordbook.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.service.Action;
import com.mnu.wordbook.service.wordsearch.WordSearchMainService;
import com.mnu.wordbook.service.wordsearch.WordSearchResultService;

/**
 * Servlet implementation class WordbookController
 */
@WebServlet("/WordSearch")
public class WordSearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WordSearchController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String cmd = request.getParameter("cmd");
		System.out.println("관리자 요청 :" + cmd);
		
		Action action = null;
		if(cmd.equals("wordsearch")) {//로그인
			action = new WordSearchMainService();
		}else if(cmd.equals("wordsearchresult")) {
			action = new WordSearchResultService();
		}
			
		action.process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		doGet(request, response);
	}

}
