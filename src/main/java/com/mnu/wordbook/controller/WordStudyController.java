package com.mnu.wordbook.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.service.Action;
import com.mnu.wordbook.service.wordstudy.WordStudyListService;
import com.mnu.wordbook.service.wordstudy.WordStudyMainService;

/**
 * Servlet implementation class WordbookController
 */
@WebServlet("/WordStudy")
public class WordStudyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WordStudyController() {
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
		if(cmd.equals("wordstudy")) {
			action = new WordStudyMainService();
		}else if(cmd.equals("wordstudylist")) {
			action = new WordStudyListService();
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
