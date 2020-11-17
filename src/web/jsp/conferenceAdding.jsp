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
    <c:if test="${conference!=null}"><h3>Conference №<input type="number" value="${conference.id}" name="conferenceId"
                                                            readonly> editing</h3></c:if>
    <c:if test="${conference==null}"><h3>Conference creating</h3></c:if>

    <h3>Name<input type="text" placeholder="Enter name"
                   <c:if test="${conference!=null}">value="${conference.name}"</c:if> name="conferenceName" required>
    </h3>
    <h3>Time
        <input type="date" name="conferenceDate"
               <c:if test="${conference!=null}">value="${my:formatLocalDateTime(conference.time, 'yyyy-MM-dd')}"</c:if>
               required/>
        <input type="time" name="conferenceTime"
               <c:if test="${conference!=null}">value="${my:formatLocalDateTime(conference.time, 'HH:mm')}"</c:if>
               required/>
    </h3>
    <h3>Address<input type="text" placeholder="Enter address"
                      <c:if test="${conference!=null}">value="${conference.address}"</c:if> name="conferenceAddress"
                      required></h3>
    <h3>Visitor capacity<input type="number"
                               <c:if test="${conference!=null}">value="${conference.maxVisitors}"</c:if>
                               name="maxVisitors" required></h3>
    <h3>Lectures:</h3>
    <table class="tableW3" id="lecturesTable">
        <tr>
            <th>Topic</th>
            <th>Speaker</th>
            <th></th>
        </tr>
        <c:if test="${conference!=null}">
            <c:forEach items="${conferenceLectures}" var="conferenceLecture">
                <tr>
                    <input hidden type="number" id="lectureId" value="${conferenceLecture.id}" name="lectureId">
                    <td><input id="lectureTopicInput" type="text" placeholder="Enter topic"
                               value="${conferenceLecture.topic}" name="lectureTopic" required></td>
                    <td>
                        <select name="Speakers" id="availableSpeakers">
                            <option selected value="-1">Select speaker</option>
                            <c:forEach items="${availableSpeakers}" var="availableSpeaker">
                                <option
                                        <c:if test="${availableSpeaker.id eq conferenceLecture.speaker.id}">selected</c:if>
                                        value=${availableSpeaker.id}>
                                        ${availableSpeaker.firstName} ${availableSpeaker.lastName}
                                </option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <input type="button" value="Delete" id="deleteRowButton" onclick="delRow(this)">
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
    <input type="button" class="button" style="float: left" value="Add lecture" onclick="addRow()">
    <button type="submit" class="button">Sign Up</button>

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
