<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>

<script>
    <c:choose>
        <c:when test="${result > 0}">
            alert("등록되었습니다");
        </c:when>
        <c:otherwise>
            alert("등록에 실패했습니다");
        </c:otherwise>
    </c:choose>

    location.href = "/WordBook?cmd=wordbook";
</script>

</body>
</html>