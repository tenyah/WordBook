<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    request.setAttribute("activePage", "quiz");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JLPT V-Master - 퀴즈</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/wordbook.css">
</head>

<body>

    <div class="step-title">4. 퀴즈</div>

    <div class="app-container">

        <jsp:include page="/common/sidebar.jsp" />

        <main class="main-content">

            <div class="quiz-setup" id="quizSetup">

                <!-- 퀴즈 모드 -->
                <div class="quiz-section">
                    <div class="quiz-section-title">퀴즈 모드</div>

                    <div class="quiz-mode-grid">

                        <label class="quiz-mode-card active" id="mode-meaning">
                            <input type="radio" name="quizMode" value="meaning" checked
                                   onchange="onModeChange()">
                            <div class="quiz-mode-top">
                                <span class="quiz-mode-name">뜻 맞추기</span>
                                <span class="quiz-radio-dot"></span>
                            </div>
                            <p class="quiz-mode-desc">일본어 단어를 보고<br>한국어 뜻을 맞추세요</p>
                        </label>

                        <label class="quiz-mode-card" id="mode-word">
                            <input type="radio" name="quizMode" value="word"
                                   onchange="onModeChange()">
                            <div class="quiz-mode-top">
                                <span class="quiz-mode-name">단어 맞추기</span>
                                <span class="quiz-radio-dot"></span>
                            </div>
                            <p class="quiz-mode-desc">한국어 뜻에 맞는<br>일본어 단어를 고르세요</p>
                        </label>

                        <label class="quiz-mode-card" id="mode-kanji">
                            <input type="radio" name="quizMode" value="kanji"
                                   onchange="onModeChange()">
                            <div class="quiz-mode-top">
                                <span class="quiz-mode-name">한자 맞추기</span>
                                <span class="quiz-radio-dot"></span>
                            </div>
                            <p class="quiz-mode-desc">한자의 뜻과 음을<br>맞추세요</p>
                        </label>

                    </div>
                </div>

                <div class="quiz-divider"></div>

                <!-- 문제에 표시할 항목 -->
                <div class="quiz-section">
                    <div class="quiz-section-title">문제에 표시할 항목</div>
                    <div class="quiz-section-desc">체크한 항목이 문제 화면에 표시됩니다. 정답 항목은 항상 숨겨집니다.</div>

                    <div class="quiz-hint-grid" id="hintGrid">

                        <label class="quiz-hint-card" id="hint-word">
                            <input type="checkbox" name="hint" value="word" checked>
                            <div class="quiz-hint-check">✓</div>
                            <div class="quiz-hint-info">
                                <span class="quiz-hint-name">단어</span>                              
                            </div>
                        </label>

                        <label class="quiz-hint-card" id="hint-huri">
                            <input type="checkbox" name="hint" value="huri" checked>
                            <div class="quiz-hint-check">✓</div>
                            <div class="quiz-hint-info">
                                <span class="quiz-hint-name">발음</span>                               
                            </div>
                        </label>

                        <label class="quiz-hint-card" id="hint-mean">
                            <input type="checkbox" name="hint" value="mean">
                            <div class="quiz-hint-check">✓</div>
                            <div class="quiz-hint-info">
                                <span class="quiz-hint-name">뜻</span>                              
                            </div>
                        </label>

                    </div>
                </div>

                <div class="quiz-divider"></div>

                <!-- 문제 설정 -->
                <div class="quiz-section">
                    <div class="quiz-section-title">문제 설정</div>

                    <div class="quiz-setting-row">

                        <div class="quiz-setting-item">
                            <label class="quiz-setting-label">단어장</label>
                            <select class="quiz-select" id="quizWbSelect">
                                <c:forEach var="wb" items="${wordbookList}">
                                    <option value="${wb.id}">${wb.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="quiz-setting-item">
                            <label class="quiz-setting-label">문제 수</label>
                            <select class="quiz-select" id="quizCountSelect">
                                <option value="10">10문제</option>
                                <option value="20" selected>20문제</option>
                                <option value="30">30문제</option>
                                <option value="50">50문제</option>
                            </select>
                        </div>

                    </div>
                </div>

                <button class="quiz-start-btn" type="button" onclick="startQuiz()">
                    퀴즈 시작
                </button>

            </div>

        </main>

    </div>

    <script>
 /* 
    모드 변경 시 힌트 옵션 제어
    정답 항목은 체크 불가 처리
 */
 const modeAnswerMap = {
     meaning:  'mean',
     word:     'word',
     kanji:    'all'  // 한자 맞추기일 때 전체 비활성화를 위해 all로 지정
 };

 function onModeChange() {
     document.querySelectorAll('.quiz-mode-card').forEach(el => {
         el.classList.remove('active');
     });
     const checked = document.querySelector('input[name="quizMode"]:checked');
     if (checked) checked.closest('.quiz-mode-card').classList.add('active');

     const answerHint = modeAnswerMap[checked.value];
     document.querySelectorAll('.quiz-hint-card').forEach(el => {
         const cb = el.querySelector('input');
         
         // kanji 모드이거나 해당 정답 항목인 경우 비활성화
         if (answerHint === 'all' || el.id === 'hint-' + answerHint) {
             cb.checked = false;
             cb.disabled = true;
             el.classList.add('disabled');
         } else {
             cb.disabled = false;
             el.classList.remove('disabled');
         }
     });

     syncHintCards();
 }

 function syncHintCards() {
     document.querySelectorAll('.quiz-hint-card').forEach(el => {
         const cb = el.querySelector('input');
         if (cb.checked) {
             el.classList.add('active');
         } else {
             el.classList.remove('active');
         }
     });
 }

 document.querySelectorAll('.quiz-hint-card input').forEach(cb => {
     cb.addEventListener('change', syncHintCards);
 });

 syncHintCards();
 onModeChange();

 /* 
    퀴즈 시작 - 서버로 제출
  */
 function startQuiz() {
     const mode = document.querySelector('input[name="quizMode"]:checked').value;

     // 한자 맞추기가 아닐 때만 힌트 1개 이상 선택 여부 검사
     if (mode !== 'kanji') {
         const checkedHints = document.querySelectorAll('input[name="hint"]:checked');
         if (checkedHints.length === 0) {
             alert("힌트를 최소 1개 이상 선택하세요");
             return;
         }
     }

     const wbId = document.getElementById('quizWbSelect').value;
     const count = document.getElementById('quizCountSelect').value;

     const form = document.createElement("form");
     form.method = "post";
     form.action = "${pageContext.request.contextPath}/WordQuiz?cmd=wordquizstart";

     function addInput(name, value) {
         const input = document.createElement("input");
         input.type = "hidden";
         input.name = name;
         input.value = value;
         form.appendChild(input);
     }

     addInput("mode", mode);
     addInput("wbId", wbId);
     addInput("count", count);

     const checkedHints = document.querySelectorAll('input[name="hint"]:checked');
     checkedHints.forEach(cb => addInput("hint", cb.value));

     document.body.appendChild(form);
     form.submit();
 }
    </script>

</body>
</html>