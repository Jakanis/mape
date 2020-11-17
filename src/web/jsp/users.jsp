<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<head>
    <title>All Users</title>
    <link href="<c:url value="/resources/css/index.css" />" rel="stylesheet" type="text/css"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

<table class="tableW3">
    <tr>
        <th>Username</th>
        <th>Role</th>
        <th>Email</th>
        <th>Name</th>
        <th>Surname</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td><a href="user?id=${user.id}"><c:out value="${user.login}"/></a></td>
            <td><c:out value="${user.role.name}"/></td>
            <td><c:out value="${user.email}"/></td>
            <td><c:out value="${user.firstName}"/></td>
            <td><c:out value="${user.lastName}"/></td>
        </tr>
    </c:forEach>
</table>

<%@include file="pagination.jsp" %>

</body>
