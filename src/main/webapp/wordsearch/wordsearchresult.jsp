<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {

		var currentWordId = null;
		$(document).on("click", ".add-word-btn", function() {
			currentWordId = $(this).data("wordid");
			$("#wordbookModal").css("display", "flex");
		});

		$("#modalConfirmBtn")
				.click(
						function() {
							var bookIds = $("input[name='bookId']:checked")
									.map(function() {
										return $(this).val();
									}).get();
							
							
									$.ajax({
										url : '${pageContext.request.contextPath}/WordBook?cmd=wordinsert',
										type : 'post',
										data : {
											wordId : currentWordId,
											bookId : bookIds
										},
										success : function(result) {
											if (result > 0) {
												alert("단어장에 추가되었습니다");
											} else {
												alert("추가된 단어장이 없습니다");
											}
											$("#wordbookModal").hide();
										}
									});
						});

		$("#modalCancelBtn").click(function() {
			$("#wordbookModal").hide();
		});

	});
</script>
</head>

<body>

	<div class="step-title">1. 단어 검색</div>

	<div class="app-container">

		<jsp:include page="/common/sidebar.jsp" />

		<main class="main-content">

			<form id="search" name="search" class="search-box"
				action="/WordSearch?cmd=wordsearchresult" method="post">
				<input type="hidden" name="cmd" value="wordsearchresult"> <input
					type="text" name="keyword" value="${param.keyword}"
					placeholder="단어, 히라가나, 한자로 검색하세요">
				<button type="submit" class="search-button" aria-label="검색">
					<span class="icon search-icon"></span>
				</button>
			</form>

			<c:choose>
				<c:when test="${not empty wordList}">

					<c:forEach var="w" items="${wordList}">

						<section class="word-card">

							<div class="word-header">
								<div class="word-title">
									<h1>${w.word}</h1>
								</div>
							</div>

							<div class="word-info">

								<div class="info-left">

									<div class="info-section">
										<div class="info-label">발음</div>
										<div class="hiragana">${w.huri}</div>
									</div>

								</div>

								<div class="info-right">

									<div class="info-section">
										<div class="info-label">한국한자</div>
										<div class="korean-hanja">
											<c:set var="kanjiArr" value="${fn:split(w.kanji, ',')}" />
											<!-- ,기준으로 한자 분리 -->
											<c:set var="kormeanArr" value="${fn:split(w.kormean, ',')}" />
											<!-- ,기준으로 뜻 분리 -->
											<c:set var="korsoundArr" value="${fn:split(w.korsound, ',')}" />
											<!-- ,기준으로 음 분리 -->

											<c:forEach var="i" begin="0" end="${fn:length(kanjiArr) - 1}">
												<!-- 한자 개수만큼 반복 -->
          										  ${fn:trim(kanjiArr[i])}(${fn:trim(kormeanArr[i])} ${fn:trim(korsoundArr[i])})
           										 <c:if test="${i < fn:length(kanjiArr) - 1}">
												</c:if>
												<!-- trim = 앞뒤공백제거, set,var 배열 선언 -->
											</c:forEach>
										</div>
									</div>
									<div class="info-section">
										<div class="info-label">한국어 뜻</div>
										<div class="meaning">${w.mean}</div>
									</div>
								</div>
							</div>
							<div class="add-area">

								<button class="add-word-btn" type="button" data-wordid="${w.id}">
									<span>＋</span> 단어장에 추가
								</button>
							</div>
						</section>

					</c:forEach>

				</c:when>
				<c:otherwise>
					<p class="no-result">"${param.keyword}"에 대한 검색 결과가 없습니다.</p>
				</c:otherwise>
			</c:choose>

		</main>
		<div id="wordbookModal" style="display: none;">
			<div>
				<h3>추가할 단어장 선택</h3>

				<c:forEach var="wb" items="${wordbookList}">
					<label> <input type="checkbox" name="bookId"
						value="${wb.id}"> ${wb.name}
					</label>
					<br>
				</c:forEach>

				<button type="button" id="modalConfirmBtn">확인</button>
				<button type="button" id="modalCancelBtn">취소</button>
			</div>
		</div>
	</div>

</body>
</html>
