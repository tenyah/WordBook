<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    request.setAttribute("activePage", "study");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JLPT V-Master - 단어 암기</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/wordbook.css">
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>

<body>

    <div class="step-title">3. 단어 암기</div>

    <div class="app-container">

        <jsp:include page="/common/sidebar.jsp" />

        <main class="main-content">

            <!-- 상단 컨트롤: 단어장 선택 / 진행률 / 설정 -->
            <div class="study-controls" style="position: relative;">

                <button class="study-book-select" type="button" onclick="toggleBookDropdown()">
                    <c:choose>
                        <c:when test="${not empty selectedWordbook}">${selectedWordbook.name}</c:when>
                        <c:otherwise>단어장 선택</c:otherwise>
                    </c:choose>
                    &#9662;
                </button>

                <div id="bookDropdown" style="display:none; position:absolute; top:100%; left:0; background:white; border:1px solid #ddd; border-radius:8px; z-index:100;">
                    <c:forEach var="wb" items="${wordbookList}">
                        <div style="padding:10px 16px; cursor:pointer;"
                             onclick="location.href='${pageContext.request.contextPath}/WordStudy?cmd=wordstudylist&wbId=${wb.id}'">
                            ${wb.name} (${wb.wordcount})
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${not empty selectedWordbook}">
                    <div class="study-progress">
                        <div class="progress-bar">
                            <div class="progress-fill" id="progressFill" style="width: 0%;"></div>
                        </div>
                        <span class="progress-count" id="progressCount">1 / ${selectedWordbook.wordcount}</span>
                    </div>
                </c:if>

            </div>

            <c:if test="${not empty selectedWordbook}">

                <!-- 서버에서 받은 단어 데이터를 JS로 넘기기 위한 숨김 영역 -->
                <div id="wordDataContainer" style="display:none;">
                    <c:forEach var="w" items="${wordList}">
                        <div class="word-data"
                             data-word="<c:out value="${w.word}"/>"
                             data-huri="<c:out value="${w.huri}"/>"
                             data-mean="<c:out value="${w.mean}"/>"
                             data-kanji="<c:out value="${w.kanji}"/>"
                             data-kormean="<c:out value="${w.kormean}"/>"
                             data-korsound="<c:out value="${w.korsound}"/>">
                        </div>
                    </c:forEach>
                </div>

                <!-- 본문: 단어 패널 + 정보 테이블 -->
                <div class="study-body">

                    <section class="study-word-panel">

                        <button class="study-nav-btn study-nav-prev" type="button"
                                aria-label="이전 단어" onclick="prevWord()">&#8249;</button>

                        <div class="study-word-jp" id="studyWordJp"></div>
                        <div class="study-word-reading" id="studyWordReading"></div>

                        <button class="study-nav-btn study-nav-next" type="button"
                                aria-label="다음 단어" onclick="nextWord()">&#8250;</button>

                    </section>

                    <section class="study-info-panel">

                        <table class="study-info-table">
                            <tbody>

                                <tr>
                                    <td class="study-info-label">발음</td>
                                    <td class="study-info-value jp" id="valHuri"></td>
                                    <td style="text-align:right; padding-right:26px;">
                                        <button class="study-toggle-btn visible" type="button" onclick="toggleField(this)">표시</button>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="study-info-label">단어</td>
                                    <td class="study-info-value jp" id="valWord"></td>
                                    <td style="text-align:right; padding-right:26px;">
                                        <button class="study-toggle-btn visible" type="button" onclick="toggleField(this)">표시</button>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="study-info-label">한국 한자</td>
                                    <td class="study-info-value hidden-val" id="valHanja">●●●●●</td>
                                    <td style="text-align:right; padding-right:26px;">
                                        <button class="study-toggle-btn hidden" type="button" onclick="toggleField(this)">숨김</button>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="study-info-label">뜻</td>
                                    <td class="study-info-value" id="valMean"></td>
                                    <td style="text-align:right; padding-right:26px;">
                                        <button class="study-toggle-btn visible" type="button" onclick="toggleField(this)">표시</button>
                                    </td>
                                </tr>

                            </tbody>
                        </table>

                    </section>

                </div>

            </c:if>

            <c:if test="${empty selectedWordbook}">
                <p style="padding: 40px; color: #888;">암기할 단어장을 선택하세요.</p>
            </c:if>

        </main>

    </div>

    <script>
        var words = [];
        $(".word-data").each(function () {
            words.push({
                word: $(this).data("word"),
                huri: $(this).data("huri"),
                mean: $(this).data("mean"),
                kanji: $(this).data("kanji"),
                kormean: $(this).data("kormean"),
                korsound: $(this).data("korsound")
            });
        });

        var currentIndex = 0;

        function toggleBookDropdown() {
            $("#bookDropdown").toggle();
        }

        // 한자 여러 개를 훈음과 짝지어 紙(종이 지) 형태로 조합
        function buildHanjaText(kanji, kormean, korsound) {
            if (!kanji) return "";

            var kanjiArr = kanji.split(",");
            var meanArr = kormean ? kormean.split(",") : [];
            var soundArr = korsound ? korsound.split(",") : [];

            var result = [];
            for (var i = 0; i < kanjiArr.length; i++) {
                var k = kanjiArr[i].trim();
                var m = (meanArr[i] || "").trim();
                var s = (soundArr[i] || "").trim();
                result.push(k + "(" + m + " " + s + ")");
            }

            return result.join(", ");
        }

        function renderWord() {
            if (words.length === 0) return;

            var w = words[currentIndex];

            $("#studyWordJp").text(w.word);

            $("#valHuri").text(w.huri);
            $("#valWord").text(w.word);
            $("#valMean").text(w.mean);
            $("#valHanja").text(buildHanjaText(w.kanji, w.kormean, w.korsound));

            // 표시/숨김 상태 초기화
            $(".study-toggle-btn").each(function () {
                var $btn = $(this);
                var $td = $btn.closest("tr").find(".study-info-value");
                var isHanja = $td.attr("id") === "valHanja";

                if (isHanja) {
                    $td.data("original", $td.text());
                    $td.text("●●●●●");
                    $td.addClass("hidden-val");
                    $btn.text("숨김").removeClass("visible").addClass("hidden");
                } else {
                    $td.removeClass("hidden-val");
                    $btn.text("표시").removeClass("hidden").addClass("visible");
                }
            });

            $("#progressCount").text((currentIndex + 1) + " / " + words.length);
            $("#progressFill").css("width", ((currentIndex + 1) / words.length * 100) + "%");
        }

        function prevWord() {
            if (words.length === 0) return;
            currentIndex = (currentIndex - 1 + words.length) % words.length;
            renderWord();
        }

        function nextWord() {
            if (words.length === 0) return;
            currentIndex = (currentIndex + 1) % words.length;
            renderWord();
        }

        function toggleField(btn) {
            var $btn = $(btn);
            var $td = $btn.closest("tr").find(".study-info-value");
            var isVisible = $btn.hasClass("visible");

            if (isVisible) {
                $td.data("original", $td.text());
                $td.text("●●●●●");
                $td.addClass("hidden-val");
                $btn.text("숨김").removeClass("visible").addClass("hidden");
            } else {
                $td.text($td.data("original") || "");
                $td.removeClass("hidden-val");
                $btn.text("표시").removeClass("hidden").addClass("visible");
            }
        }

        renderWord();
    </script>

</body>
</html>
