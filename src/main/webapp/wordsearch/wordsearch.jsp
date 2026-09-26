<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
request.setAttribute("activePage", "search");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>JLPT V-Master - 단어 검색</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/wordbook.css">
</head>

<body>

	<div class="step-title">1. 단어 검색</div>

	<div class="app-container">

		<jsp:include page="/common/sidebar.jsp" />

		<main class="main-content">

			<form id="search" name="search" class="search-box"
				action="/WordSearch?cmd=wordsearchresult" method="post">
				<input type="hidden" name="cmd" value="wordsearchresult">
				 <input type="text" name="keyword" placeholder="단어, 히라가나, 한자로 검색하세요">
				<button type="submit" class="search-button" aria-label="검색">
					<span class="icon search-icon"></span>
				</button>
			</form>

		</main>

	</div>

</body>
</html>
