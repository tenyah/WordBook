<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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

            <!-- 서버에서 받은 문제 데이터를 JS로 넘기기 위한 숨김 영역 -->
            <div id="quizDataContainer" style="display:none;">
                <c:forEach var="q" items="${questions}">
                    <div class="quiz-data"
                         data-word="<c:out value="${q.word}"/>"
                         data-huri="<c:out value="${q.huri}"/>"
                         data-hanja="<c:out value="${fn:replace(q.kanji, ', ', '')}"/>"
                         data-mean="<c:out value="${q.mean}"/>"
                         data-answer="<c:out value="${q.answer}"/>"
                         data-hints="<c:forEach var="h" items="${q.hints}" varStatus="st">${h}<c:if test="${!st.last}">,</c:if></c:forEach>">
                        <c:forEach var="c" items="${q.choices}">
                            <span class="choice-item"><c:out value="${c}"/></span>
                        </c:forEach>
                    </div>
                </c:forEach>
            </div>

            <div class="quiz-play" id="quizPlay">

                <!-- 진행률 -->
                <div class="study-controls" style="margin-bottom:28px;">
                    <span class="progress-count" id="playCount">1 / 1</span>
                    <div class="study-progress">
                        <div class="progress-bar">
                            <div class="progress-fill" id="playFill" style="width:0%;"></div>
                        </div>
                    </div>
                    <button class="study-setting-btn" type="button" onclick="exitQuiz()">
                        ✕ 종료
                    </button>
                </div>

                <!-- 문제 카드 -->
                <div class="quiz-play-card">

                    <div class="quiz-question-area">
                        <div class="quiz-question-hints" id="questionHints"></div>
                    </div>

                    <div class="quiz-choices" id="quizChoices"></div>

                </div>

            </div>

        </main>

    </div>

    <script>
        var mode = "<c:out value='${mode}'/>";
        var questions = [];

        document.querySelectorAll(".quiz-data").forEach(function (div) {
            var choices = [];
            div.querySelectorAll(".choice-item").forEach(function (span) {
                choices.push(span.textContent);
            });

            var hintsStr = div.dataset.hints;
            var hints = hintsStr ? hintsStr.split(",") : [];

            questions.push({
                word: div.dataset.word,
                huri: div.dataset.huri,
                hanja: div.dataset.hanja,
                mean: div.dataset.mean,
                answer: div.dataset.answer,
                choices: choices,
                hints: hints
            });
        });

        var currentIdx = 0;
        var correctCount = 0;
        var totalQ = questions.length;

        function renderQuestion() {

            if (totalQ === 0) {
                document.querySelector(".quiz-play-card").innerHTML = "<p>문제를 만들 수 없습니다.</p>";
                return;
            }

            var q = questions[currentIdx];

            document.getElementById('playCount').textContent = (currentIdx + 1) + ' / ' + totalQ;
            document.getElementById('playFill').style.width = Math.round(((currentIdx + 1) / totalQ) * 100) + '%';

            var hintMap = {
                word: { label: '단어', value: q.word, cls: 'jp' },
                huri: { label: '후리가나', value: q.huri, cls: 'jp' },
                mean: { label: '뜻', value: q.mean, cls: '' }
            };

            var hintsEl = document.getElementById('questionHints');
            hintsEl.innerHTML = '';

            // 한자 맞추기 모드면 힌트 체크 여부와 상관없이 한자 자체를 항상 문제로 보여줌
            if (mode === 'kanji') {
                var stemDiv = document.createElement('div');
                stemDiv.className = 'quiz-hint-item';
                stemDiv.innerHTML =
                    '<span class="quiz-hint-label">한자</span>' +
                    '<span class="quiz-hint-val jp">' + q.hanja + '</span>';
                hintsEl.appendChild(stemDiv);
            }

            q.hints.forEach(function (key) {
                var h = hintMap[key];
                if (!h) return;
                var div = document.createElement('div');
                div.className = 'quiz-hint-item';
                div.innerHTML =
                    '<span class="quiz-hint-label">' + h.label + '</span>' +
                    '<span class="quiz-hint-val ' + h.cls + '">' + h.value + '</span>';
                hintsEl.appendChild(div);
            });

            var choicesEl = document.getElementById('quizChoices');
            choicesEl.innerHTML = '';
            q.choices.forEach(function (c) {
                var btn = document.createElement('button');
                btn.className = 'quiz-choice-btn';
                btn.textContent = c;
                btn.onclick = function () { selectAnswer(btn, c === q.answer, choicesEl); };
                choicesEl.appendChild(btn);
            });
        }

        function selectAnswer(btn, correct, choicesEl) {
            choicesEl.querySelectorAll('.quiz-choice-btn').forEach(function (b) {
                b.disabled = true;
            });
            btn.classList.add(correct ? 'correct' : 'wrong');

            if (correct) {
                correctCount++;
            } else {
                var q = questions[currentIdx];
                choicesEl.querySelectorAll('.quiz-choice-btn').forEach(function (b) {
                    if (b.textContent === q.answer) {
                        b.classList.add('correct');
                    }
                });
            }

            setTimeout(function () {
                currentIdx++;
                if (currentIdx >= totalQ) {
                    alert('퀴즈 완료! ' + correctCount + ' / ' + totalQ + '개 정답');
                    exitQuiz();
                } else {
                    renderQuestion();
                }
            }, 900);
        }

        function exitQuiz() {
            location.href = "${pageContext.request.contextPath}/WordQuiz?cmd=wordquiz";
        }

        renderQuestion();
    </script>

</body>
</html>