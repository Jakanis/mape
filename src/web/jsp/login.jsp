<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registration Form</title>
    <link rel="stylesheet" href="resources/css/login.css"/>
</head>

<body>
<jsp:include page="/jsp/header.jsp">
    <jsp:param name="title" value="Login"/>
</jsp:include>

<form action="" method="post" style="border:1px solid #ccc">
    <div class="container">
        <h1><fmt:message key="header.signIn"/></h1>
        <p><fmt:message key="signIn.fill"/></p>

        <c:choose>
            <c:when test="${login_error.equals('fieldEmpty')}">
                <p style="color: red"><fmt:message key="registration.fillAllFields"/></p>
            </c:when>
            <c:when test="${login_error.equals('userLoginNotExists')}">
                <p style="color: red"><fmt:message key="signIn.incorrectLogin"/></p>
            </c:when>
            <c:when test="${login_error.equals('incorrectPassword')}">
                <p style="color: red"><fmt:message key="signIn.incorrectPassword"/></p>
            </c:when>
        </c:choose>


        <label><b><fmt:message key="singIn.login"/></b></label>
        <input type="text" placeholder="Enter login" name="login" required>

        <label><b><fmt:message key="signIn.password"/></b></label>
        <input type="password" placeholder="Enter Password" name="password" required>


        <div class="clearfix">
            <button type="submit" class="loginbtn"><fmt:message key="signIn.logIn"/></button>
            <button onclick="location.href='registration'" type="button" class="signupbtn">Sign Up
            </button>
        </div>
    </div>
</form>

</body>
</html>