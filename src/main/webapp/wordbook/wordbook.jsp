<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
request.setAttribute("activePage", "wordbook");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>JLPT V-Master - 단어장 작성</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/wordbook.css">
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>

<body>

	<div class="step-title">2. 단어장</div>

	<div class="app-container">

		<jsp:include page="/common/sidebar.jsp" />

		<main class="main-content">

			<header class="content-header">
				<h1>내 단어장</h1>

				<button class="new-book-btn" type="button" onclick="newwordbook()">
					<span>＋</span> 새 단어장
				</button>
			</header>

			<div class="content-body">

				<section class="book-list">

					<!-- 여기부터 foreach -->
					<c:forEach var="wb" items="${wordbookList}">
						<div class="book-item ${wb.id == selectedWbId ? 'selected' : ''}"
							onclick="location.href='${pageContext.request.contextPath}/WordBook?cmd=wordbooklist&wbId=${wb.id}'">
							<h2>${wb.name}</h2>
							<p>${wb.wordcount}단어</p>
						</div>
					</c:forEach>

				</section>

				<c:if test="${not empty selectedWordbook}">
					<section class="word-section">

						<div class="word-header">
							<h2>${selectedWordbook.name}
								<span>(${selectedWordbook.wordcount})</span>
							</h2>

							<div style="position: relative; display: inline-block;">
								<button class="edit-btn" type="button" onclick="toggleEditMenu()">편집</button>

								<div id="editMenu" style="display:none; position:absolute; top:100%; right:0; background:white; border:1px solid #ddd; border-radius:8px; z-index:100; min-width:120px;">
									<div style="padding:10px 16px; cursor:pointer;" onclick="editWordbookName()">이름 변경</div>
									<div style="padding:10px 16px; cursor:pointer; color:#c0392b;" onclick="deleteWordbook()">삭제</div>
								</div>
							</div>
						</div>

						<div class="table-wrapper">

							<table class="word-table">

								<thead>
									<tr>
										<th class="check-column"><input type="checkbox"
											id="checkAll"></th>
										<th>단어</th>
										<th>발음</th>
										<th>뜻</th>
									</tr>
								</thead>

								<tbody>

									<c:forEach var="w" items="${wordList}">
										<tr>
											<td><input type="checkbox" class="word-check"
												value="${w.id}" data-type="${w.wordType}"></td>
											<td class="japanese">${w.word}</td>
											<td>${w.huri}</td>
											<td>${w.mean}</td>
										</tr>
									</c:forEach>

								</tbody>

							</table>

						</div>

						<div class="word-footer">

							<button class="delete-btn" type="button" onclick="deleteWords()">
								<span>♜</span> 선택한 단어 삭제
							</button>

							<button class="add-word-btn" type="button"
								onclick="$('#customWordModal').css('display','flex')">
								<span>＋</span> 직접 추가
							</button>

						</div>

					</section>
				</c:if>

			</div>

		</main>
		<div id="customWordModal" style="display: none;">
			<div>
				<h3>단어 직접 추가</h3>

				<label>단어 <input type="text" id="cwWord"></label><br> <label>발음
					<input type="text" id="cwHuri">
				</label><br> <label>뜻 <input type="text" id="cwMean"></label><br>
				<label>한자 <input type="text" id="cwKanji"></label><br>
				<label>한국한자 뜻 <input type="text" id="cwKormean"></label><br>
				<label>한국한자 음 <input type="text" id="cwKorsound"></label><br>

				<button type="button" id="cwConfirmBtn">추가</button>
				<button type="button" id="cwCancelBtn">취소</button>
			</div>
		</div>
	</div>
</body>
</html>
<script>
	function newwordbook() {
		var name = prompt("새 단어장 이름을 입력하세요");
		if (name && name.trim() !== "") {
			var form = document.createElement("form");
			form.method = "post";
			form.action = "/WordBook?cmd=wordbookpro";

			var input = document.createElement("input");
			input.type = "hidden";
			input.name = "name";
			input.value = name;

			form.appendChild(input);
			document.body.appendChild(form);
			form.submit();
		}

	}

	function deleteWords() {
	    var checkedItems = $(".word-check:checked").map(function() {
	        return $(this).val() + ":" + $(this).data("type");
	    }).get();

	    if (checkedItems.length === 0) {
	        alert("삭제할 단어를 선택하세요");
	        return;
	    }

	    if (!confirm(checkedItems.length + "개 단어를 삭제하시겠습니까?")) {
	        return;
	    }

	    var form = document.createElement("form");
	    form.method = "post";
	    form.action = "${pageContext.request.contextPath}/WordBook?cmd=worddelete&wbId=${selectedWbId}";

	    checkedItems.forEach(function(item) {
	        var input = document.createElement("input");
	        input.type = "hidden";
	        input.name = "wordItem";
	        input.value = item;
	        form.appendChild(input);
	    });

	    document.body.appendChild(form);
	    form.submit();
	}

	function toggleEditMenu() {
	    $("#editMenu").toggle();
	}

	function editWordbookName() {
	    var newName = prompt("새 단어장 이름을 입력하세요", "${selectedWordbook.name}");
	    if (newName && newName.trim() !== "") {
	        var form = document.createElement("form");
	        form.method = "post";
	        form.action = "${pageContext.request.contextPath}/WordBook?cmd=wordbookedit";

	        var wbIdInput = document.createElement("input");
	        wbIdInput.type = "hidden";
	        wbIdInput.name = "wbId";
	        wbIdInput.value = "${selectedWbId}";
	        form.appendChild(wbIdInput);

	        var nameInput = document.createElement("input");
	        nameInput.type = "hidden";
	        nameInput.name = "newName";
	        nameInput.value = newName;
	        form.appendChild(nameInput);

	        document.body.appendChild(form);
	        form.submit();
	    }
	    $("#editMenu").hide();
	}

	function deleteWordbook() {
	    if (!confirm("정말 이 단어장을 삭제하시겠습니까? 단어장 안의 단어 연결도 모두 삭제됩니다.")) {
	        return;
	    }

	    var form = document.createElement("form");
	    form.method = "post";
	    form.action = "${pageContext.request.contextPath}/WordBook?cmd=wordbookdelete";

	    var wbIdInput = document.createElement("input");
	    wbIdInput.type = "hidden";
	    wbIdInput.name = "wbId";
	    wbIdInput.value = "${selectedWbId}";
	    form.appendChild(wbIdInput);

	    document.body.appendChild(form);
	    form.submit();
	}

	$(function() {

		$("#checkAll").on("change", function() {
			var checked = $(this).prop("checked");
			$(".word-check").prop("checked", checked);
		});

		$(document).on("change", ".word-check", function() {
			var total = $(".word-check").length;
			var checkedCount = $(".word-check:checked").length;
			$("#checkAll").prop("checked", total === checkedCount);
		});

		$("#cwCancelBtn").click(function() {
			$("#customWordModal").hide();
		});

		$("#cwConfirmBtn")
				.click(
						function() {

							var word = $("#cwWord").val().trim();
							var huri = $("#cwHuri").val().trim();
							var mean = $("#cwMean").val().trim();

							if (word === "" || mean === "") {
								alert("단어와 뜻은 필수입니다");
								return;
							}

							var form = document.createElement("form");
							form.method = "post";
							form.action = "${pageContext.request.contextPath}/WordBook?cmd=customwordinsert";

							var fields = {
								"word" : word,
								"huri" : huri,
								"mean" : mean,
								"kanji" : $("#cwKanji").val(),
								"kormean" : $("#cwKormean").val(),
								"korsound" : $("#cwKorsound").val(),
								"wbId" : "${selectedWbId}"
							};

							for ( var key in fields) {
								var input = document.createElement("input");
								input.type = "hidden";
								input.name = key;
								input.value = fields[key];
								form.appendChild(input);
							}

							document.body.appendChild(form);
							form.submit();
						});

	});
</script>