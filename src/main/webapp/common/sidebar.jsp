<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // 각 페이지에서 include 하기 전에
    // request.setAttribute("activePage", "search|wordbook|study|quiz");
    // 를 지정하면 해당 메뉴가 active로 표시됩니다.
    String activePage = (String) request.getAttribute("activePage");
    if (activePage == null) activePage = "";
%>

<div class="sidebar">

    <div class="logo">
        커스텀 단어장
    </div>

    <nav class="menu">

        <a href="WordSearch?cmd=wordsearch"
           class="menu-item <%= "search".equals(activePage) ? "active" : "" %>">
            <span class="icon search-icon"></span>
            <span>단어 검색</span>
        </a>

        <a href="WordBook?cmd=wordbook"
           class="menu-item <%= "wordbook".equals(activePage) ? "active" : "" %>">
            <span class="icon book-icon"></span>
            <span>단어장 작성</span>
        </a>

        <a href="WordStudy?cmd=wordstudy"
           class="menu-item <%= "study".equals(activePage) ? "active" : "" %>">
            <span class="icon graduation-icon"></span>
            <span>단어 암기</span>
        </a>

        <a href="WordQuiz?cmd=wordquiz"
           class="menu-item <%= "quiz".equals(activePage) ? "active" : "" %>">
            <span class="icon question-icon">?</span>
            <span>퀴즈</span>
        </a>

    </nav>

</div>
