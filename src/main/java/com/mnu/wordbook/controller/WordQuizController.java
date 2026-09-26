package com.mnu.wordbook.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.service.Action;
import com.mnu.wordbook.service.wordquiz.WordQuizMainService;
import com.mnu.wordbook.service.wordquiz.WordQuizStartService;

/**
 * Servlet implementation class WordbookController
 */
@WebServlet("/WordQuiz")
public class WordQuizController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WordQuizController() {
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
		if (cmd.equals("wordquiz")) {
		    action = new WordQuizMainService();
		} else if(cmd.equals("wordquizstart")) {
		    action = new WordQuizStartService();  
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
