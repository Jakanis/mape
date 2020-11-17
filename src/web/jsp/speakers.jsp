<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<head>
    <title>Speakers</title>
    <link href="<c:url value="/resources/css/index.css" />" rel="stylesheet" type="text/css"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp">
    <jsp:param name="title" value="Speakers"/>
</jsp:include>

<table class="tableW3">
    <tr>
        <th><fmt:message key="user.username"/></th>
        <th><fmt:message key="user.email"/></th>
        <th><fmt:message key="user.firstName"/></th>
        <th><fmt:message key="user.lastName"/></th>
        <th><fmt:message key="user.averageRate"/></th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td><a href="user?id=${user.id}"><c:out value="${user.login}"/></a></td>
            <td><c:out value="${user.email}"/></td>
            <td><c:out value="${user.firstName}"/></td>
            <td><c:out value="${user.lastName}"/></td>
            <td><fmt:formatNumber pattern="0.00" value="${user.averageRate}"/></td>
        </tr>
    </c:forEach>
</table>

<%@include file="pagination.jsp" %>

</body>
