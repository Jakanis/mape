<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="http://jakanis.io/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<html>
<head>
    <title>Conferences</title>
    <link rel="stylesheet" href="/resources/css/index.css"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp">
    <jsp:param name="title" value="Conferences"/>
</jsp:include>

<table class="tableW3">
    <tr>
        <th><fmt:message key="conference.name"/></th>
        <th><fmt:message key="conference.address"/></th>
        <th><fmt:message key="conference.time"/></th>
        <th><fmt:message key="conference.registeredVisitors"/></th>
    </tr>
    <c:forEach items="${conferences}" var="conference">
        <tr>
            <td><a href="conference?id=${conference.id}"><c:out value="${conference.name}"/></a></td>
            <td><c:out value="${conference.address}"/></td>
            <td>${my:formatLocalDateTimeDefault(conference.time)}</td>
            <td><c:out value="${conference.registeredVisitors}/${conference.maxVisitors}"/></td>
        </tr>
    </c:forEach>
</table>


<%@include file="pagination.jsp" %>
<c:if test="${sessionScope.userRole eq 'Moderator' || sessionScope.userRole eq 'Admin'}">
    <a href="add_conference" class="button"><fmt:message key="conferences.add"/></a>
</c:if>

</body>


</html>
