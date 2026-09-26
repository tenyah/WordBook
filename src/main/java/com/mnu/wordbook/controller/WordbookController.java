package com.mnu.wordbook.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mnu.wordbook.service.Action;
import com.mnu.wordbook.service.wordbook.CustomWordInsertService;
import com.mnu.wordbook.service.wordbook.WordBookListService;
import com.mnu.wordbook.service.wordbook.WordDeleteService;
import com.mnu.wordbook.service.wordbook.WordInsertService;
import com.mnu.wordbook.service.wordbook.WordbookDeleteService;
import com.mnu.wordbook.service.wordbook.WordbookEditService;
import com.mnu.wordbook.service.wordbook.WordbookMainService;
import com.mnu.wordbook.service.wordbook.WordbookProService;

/**
 * Servlet implementation class WordbookController
 */
@WebServlet("/WordBook")
public class WordbookController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WordbookController() {
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
		if(cmd.equals("wordbook")) {
			action = new WordbookMainService();
		}else if(cmd.equals("wordbookpro")){
			action = new WordbookProService();
		}else if(cmd.equals("wordinsert")) {
			action = new WordInsertService();
		}else if(cmd.equals("wordbooklist")) {
			action = new WordBookListService();
		} else if ("customwordinsert".equals(cmd)) {
		    action = new CustomWordInsertService();
		} else if ("worddelete".equals(cmd)) {
		    action = new WordDeleteService();
		} else if ("wordbookedit".equals(cmd)) {
		    action = new WordbookEditService();
		} else if ("wordbookdelete".equals(cmd)) {
		    action = new WordbookDeleteService();
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
