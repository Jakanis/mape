<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<head>
    <title><fmt:message key="title.homepage"/></title>
</head>
<body>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Homepage"/>
</jsp:include>
<div>

    <h2>
        <fmt:message key="index.welcome"/>
    </h2>
</div>
</body>

</html>
