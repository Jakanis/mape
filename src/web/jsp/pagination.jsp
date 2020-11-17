<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="resources/css/index.css"/>
<div class="center">
    <div class="pagination">

        <c:if test="${currentPage != 1}">
            <a href="<c:url value="?recordsPerPage=${recordsPerPage}&currentPage=${currentPage-1}"/>">&laquo;</a>
        </c:if>

        <c:forEach begin="1" end="${noOfPages}" var="i">
            <c:choose>
                <c:when test="${currentPage eq i}">
                    <a class="current"> ${i}</a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value="?recordsPerPage=${recordsPerPage}&currentPage=${i}"/>">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${currentPage lt noOfPages}">
            <a href="<c:url value="?currentPage=${currentPage+1}&recordsPerPage=${recordsPerPage}"/>">&raquo;</a>
        </c:if>

    </div>
</div>
<form class="center" action="">
    <a><fmt:message key="pagination.recordsPerPage"/></a>
    <select name="recordsPerPage">
        <option value="5">5</option>
        <option value="10">10</option>
        <option value="15">15</option>
    </select>
    <input type="submit" value="<fmt:message key="pagination.submit"/>">
</form>