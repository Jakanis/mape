<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<link rel="stylesheet" href="resources/css/index.css"/>
<div class="header">
    <img alt="Company logo" class="logo" src="resources/img/logo.png">

    <div class="header-right">
        <a <c:if test="${param.title=='Homepage'}"> class="active" </c:if> href="./"><fmt:message key="header.home"/></a>
        <a <c:if test="${param.title=='Conferences'}"> class="active" </c:if> href="conferences"><fmt:message key="header.conferences"/></a>
        <a <c:if test="${param.title=='Speakers'}"> class="active" </c:if> href="speakers"><fmt:message key="header.speakers"/></a>
        <c:if test="${sessionScope.userId==null}"><a <c:if
                test="${param.title=='Login'}"> class="active" </c:if> href="login"><fmt:message
                key="header.signIn"/></a></c:if>

        <c:if test="${sessionScope.userId!=null}">
            <a
                    <c:if test="${sessionScope.notify}">class="blink"
                    title="There is a conference in less than 7 days"</c:if>
                    <c:if test="${param.title eq sessionScope.userLogin}"> class="active" </c:if>
                    href="user?id=${sessionScope.userId}">${sessionScope.userLogin}</a>
        </c:if>

        <c:if test="${sessionScope.userRole eq 'Admin'}">
            <a href="users" style="color: purple"><fmt:message key="header.users"/></a>
        </c:if>
        <c:if test="${sessionScope.userId!=null}">
            <a href="logout"><fmt:message key="header.logout"/></a>
        </c:if>
    </div>

    <div class="locale_picker">
        <ul>
            <a <c:if test="${sessionScope.lang=='en' || sessionScope.lang==null}"> class="active" </c:if>
                    title="English" href="?sessionLocale=en">EN</a>
            <a <c:if test="${sessionScope.lang=='ua'}"> class="active" </c:if>title="Ukrainian"
                    href="?sessionLocale=ua">UA</a>
        </ul>
    </div>
</div>