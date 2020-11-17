<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="http://jakanis.io/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<!DOCTYPE HTML>
<head>
    <title>Speakers</title>
    <link href="<c:url value="/resources/css/index.css" />" rel="stylesheet" type="text/css"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

<div>
    <c:if test="${registrationError=='passed'}">
        <h4 style="color: red"><fmt:message key="conference.late"/></h4>
    </c:if>
    <c:if test="${registrationError=='full'}">
        <h4 style="color: red"><fmt:message key="conference.full"/></h4>
    </c:if>

    <c:if test="${currentUserRegistered!=null}">
        <c:if test="${conferenceState=='passed'}">
            <c:if test="${currentUserRegistered}">
                <h4 style="color: green"><fmt:message key="conference.passed.registered"/></h4>
            </c:if>
            <c:if test="${!currentUserRegistered}">
                <h4 style="color: gray"><fmt:message key="conference.passed.notRegistered"/></h4>
            </c:if>
        </c:if>

        <c:if test="${conferenceState=='full'}">
            <form method="post">
                <c:if test="${currentUserRegistered}">
                    <h4 style="color: green"><fmt:message key="conference.registered"/>
                        <button type="submit" name="button" value="unregister"><fmt:message
                                key="conference.unregister"/></button>
                    </h4>
                </c:if>
                <c:if test="${!currentUserRegistered}">
                    <h4 style="color: grey;">You are not registered to this conference and it's full.</h4>
                </c:if>
            </form>
        </c:if>

        <c:if test="${conferenceState=='open'}">
            <form method="post">
                <c:if test="${currentUserRegistered}">
                    <h4 style="color: green">You are registered to this conference.
                        <button type="submit" name="button" value="unregister"><fmt:message
                                key="conference.unregister"/></button>
                    </h4>
                </c:if>
                <c:if test="${!currentUserRegistered}">
                    <h4 style="color: grey;">You are not registered to this conference.
                        <button type="submit" name="button" value="register"><fmt:message
                                key="conference.register"/></button>
                    </h4>
                </c:if>
            </form>
        </c:if>
    </c:if>

    <h3>Conference №${conference.id}</h3>
    <h3>Name: ${conference.name}</h3>
    <h3>Time: ${my:formatLocalDateTimeDefault(conference.time)}</h3>
    <h3>Address: ${conference.address}</h3>
    <h3>Lectures:</h3>
    <table class="tableW3">
        <tr>
            <th>Topic</th>
            <th>Speaker</th>
            <c:if test="${sessionScope.userRole eq 'Moderator' || sessionScope.userRole eq 'Admin' || sessionScope.userRole eq 'Speaker'}">
                <th>Status</th>
            </c:if>
        </tr>
        <c:forEach items="${conferenceLectures}" var="conferenceLecture">
            <tr>
                <td>${conferenceLecture.topic}</td>
                <td>
                    <a href="user?id=${conferenceLecture.speaker.id}">${conferenceLecture.speaker.firstName} ${conferenceLecture.speaker.lastName}</a>
                </td>
                <c:if test="${sessionScope.userRole eq 'Moderator' || sessionScope.userRole eq 'Admin' || sessionScope.userRole eq 'Speaker'}">
                    <c:if test="${conferenceLecture.approvedByModerator && conferenceLecture.approvedBySpeaker}">
                        <td>Approved</td>
                    </c:if>

                    <c:if test="${!conferenceLecture.approvedByModerator && conferenceLecture.approvedBySpeaker}">
                        <td>Proposed by speaker
                            <c:if test="${sessionScope.userRole eq 'Moderator' || sessionScope.userRole eq 'Admin'}">
                                <form method="post">
                                    <button type="submit" name="button" value="approve${conferenceLecture.id}">Approve
                                    </button>
                                    <button type="submit" name="button" value="delete${conferenceLecture.id}">Delete
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </c:if>

                    <c:if test="${conferenceLecture.approvedByModerator && !conferenceLecture.approvedBySpeaker}">
                        <td>Waiting for speaker
                            <c:if test="${sessionScope.userRole eq 'Speaker'}">
                                <form method="post">
                                    <button type="submit" name="button" value="approve${conferenceLecture.id}">Approve
                                    </button>
                                    <button type="submit" name="button" value="delete${conferenceLecture.id}">Delete
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </c:if>

                </c:if>
            </tr>
        </c:forEach>
    </table>

    <c:if test="${sessionScope.userRole eq 'Moderator' || sessionScope.userRole eq 'Admin'}">
        <a href="edit_conference?id=${conference.id}" class="button">Edit conference</a>
    </c:if>
    <c:if test="${sessionScope.userRole eq 'Speaker'}">
        <a href="propose_lecture?id=${conference.id}" class="button">Propose lection</a>
    </c:if>

</div>
</body>
