<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="http://jakanis.io/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<head>
    <title>${user.login}</title>
    <link href="<c:url value="/resources/css/index.css" />" rel="stylesheet" type="text/css"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp">
    <jsp:param name="title" value="${user.login}"/>
</jsp:include>

<div>
    <table class="tableW3" style="width: 50%">
        <tr>
            <th><fmt:message key="user.id"/></th>
            <td>${user.id}</td>
        </tr>
        <tr>
            <th><fmt:message key="user.role"/></th>
            <td>${user.role.name}</td>
        </tr>
        <tr>
            <th><fmt:message key="user.username"/></th>
            <td>${user.login}</td>
        </tr>
        <tr>
            <th><fmt:message key="user.firstName"/></th>
            <td>${user.firstName}</td>
        </tr>
        <tr>
            <th><fmt:message key="user.lastName"/></th>
            <td>${user.lastName}</td>
        </tr>
        <tr>
            <th><fmt:message key="user.email"/></th>
            <td>${user.email}</td>
        </tr>

        <c:if test="${user.role.name=='Speaker'}">

            <tr>
                <th><fmt:message key="user.averageRate"/></th>
                <td><fmt:formatNumber pattern="0.00" value="${averageRate}"/></td>
            </tr>
            <c:if test="${sessionScope.userId!=null && sessionScope.userId!=user.id}">
                <tr>
                <form action="" method="post">
                <c:if test="${currentRate != 0}">
                    <tr>
                        <th><fmt:message key="user.yourRate"/></th>
                        <td>${currentRate}</td>
                    </tr>
                </c:if>
                <th><fmt:message key="user.rate"/></th>
                <td>
                    <select name="newRate">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>
                    <input type="submit" name="action" value="Rate">
                </td>
                </form>
                </tr>
            </c:if>

        </c:if>
    </table>

    <c:if test="${userConferences!=null}">
        <h3><fmt:message key="user.registeredConferences"/></h3>
        <table class="tableW3">
            <tr>
                <th>Name</th>
                <th>Address</th>
                <th>Time</th>
            </tr>
            <c:forEach items="${userConferences}" var="conference">
                <tr>
                    <td><a href="conference?id=${conference.id}"><c:out value="${conference.name}"/></a></td>
                    <td><c:out value="${conference.address}"/></td>
                    <td>${my:formatLocalDateTimeDefault(conference.time)}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>


</div>

</body>
