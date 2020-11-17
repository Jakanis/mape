<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="http://jakanis.io/functions" %>
<!DOCTYPE HTML>
<head>
    <title>Speakers</title>
    <link href="<c:url value="/resources/css/index.css" />" rel="stylesheet" type="text/css"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>


<c:if test="${conference!=null}"> </c:if>


<form method="post">

    <h3>Conference №${conference.id}</h3>
    <h3>Name: ${conference.name}</h3>
    <h3>Time: ${my:formatLocalDateTimeDefault(conference.time)}</h3>
    <h3>Address: ${conference.address}</h3>
    <h3>Lectures:</h3>
    <table class="tableW3" style="width: 75%">
        <tr>
            <th>Topic</th>
            <th>Speaker</th>
        </tr>
        <c:forEach items="${conferenceLectures}" var="conferenceLecture">
            <tr>
                <td>${conferenceLecture.topic}</td>
                <td>
                    <a href="user?id=${conferenceLecture.speaker.id}">${conferenceLecture.speaker.firstName} ${conferenceLecture.speaker.lastName}</a>
                </td>
            </tr>
        </c:forEach>

        <tr>
            <td><input type="text" placeholder="Enter topic" name="lectureTopic" required></td>
            <td><a href="user?id=${sessionScope.userId}">${sessionScope.userFirstName} ${sessionScope.userLastName}
                (you)</a></td>
        </tr>

    </table>

    <button type="submit" class="button" style="float: left">Save</button>

</form>
</body>


<script>
  function delRow(currElement) {
    var parentRowIndex = currElement.parentNode.parentNode.rowIndex;
    document.getElementById("lecturesTable").deleteRow(parentRowIndex);
  }

  var getTableRowCount = function (tableNode) {
    var tbody = tableNode.getElementsByTagName('tbody')[0];
    if (tbody) {
      return tbody.getElementsByTagName('tr').length;
    } else {
      return tableNode.getElementsByTagName('tr').length;
    }
  };

  function addRow() {
    var table = document.getElementById("lecturesTable");
    if (table.getElementsByTagName('tr').length > 1) {
      var lectureTopicInput = document.getElementById("lectureTopicInput").cloneNode(true);
      lectureTopicInput.value = "";
      var availableSpeakersComboBox = document.getElementById("availableSpeakers").cloneNode(true);
      var deleteRowButton = document.getElementById("deleteRowButton").cloneNode(true);
      availableSpeakersComboBox.value = -1;
      var lectureId = document.getElementById("lectureId").cloneNode();
      lectureId.value = -1;
      var tr = table.insertRow();
      tr.appendChild(lectureId);
      var td = tr.insertCell();
      td.appendChild(lectureTopicInput);
      td = tr.insertCell();
      td.appendChild(availableSpeakersComboBox);
      td = tr.insertCell();
      td.appendChild(deleteRowButton);
    } else {
      createTable();
    }
  }

  function createTable() {
    var table = document.getElementById("lecturesTable");
    table.innerHTML += "<tbody><tr><input hidden=\"\" type=\"number\" id=\"lectureId\" value=\"-1\" name=\"lectureId\">"
        +
        "<td><input id=\"lectureTopicInput\" type=\"text\" placeholder=\"Enter topic\" value=\"\" name=\"lectureTopic\" required=\"\"></td>"
        +
        "<td><select name=\"Speakers\" id=\"availableSpeakers\"> <option selected=\"\" value=\"-1\">Select speaker</option>"
        <c:forEach items="${availableSpeakers}" var="availableSpeaker">
        + "<option value=${availableSpeaker.id}>${availableSpeaker.firstName} ${availableSpeaker.lastName}</option>"
        </c:forEach>
        + "</select></td><td><input type=\"button\" value=\"Delete\" id=\"deleteRowButton\" onclick=\"delRow(this)\"></td></tr></tbody>"
  }
</script>
