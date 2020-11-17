<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registration Form</title>
    <link rel="stylesheet" href="resources/css/login.css"/>
</head>

<body>

<jsp:include page="/jsp/header.jsp">
    <jsp:param name="title" value="Registration"/>
</jsp:include>

<form action="" method="post" style="border:1px solid #ccc">
    <div class="container">
        <h1><fmt:message key="registration.signUp"/></h1>
        <p><fmt:message key="registration.p"/></p>
        <hr>

        <c:choose>
            <c:when test="${registration_error.equals('fieldEmpty')}">
                <p style="color: red"><fmt:message key="registration.fillAllFields"/></p>
            </c:when>
            <c:when test="${registration_error.equals('notEqualPasswords')}">
                <p style="color: red"><fmt:message key="registration.notEqualPasswords"/></p>
            </c:when>
            <c:when test="${registration_error.equals('notEmail')}">
                <p style="color: red"><fmt:message key="registration.notEmail"/></p>
            </c:when>
            <c:when test="${registration_error.equals('weakPassword')}">
                <p style="color: red"><fmt:message key="registration.weakPassword"/></p>
            </c:when>
            <c:when test="${registration_error.equals('userLoginExists')}">
                <p style="color: red">There is already user with same login.</p>
            </c:when>
            <c:when test="${registration_error.equals('userEmailExists')}">
                <p style="color: red">There is already user with same email.</p>
            </c:when>
        </c:choose>

        <label><b>Login</b></label>
        <input type="text" placeholder="Enter login" name="login" required>

        <label><b>Email</b></label>
        <input type="text" placeholder="Enter Email" name="email" required>

        <label><b>Password</b></label>
        <input type="password" placeholder="Enter Password" id="password" name="password"
               pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
               title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"
               required>

        <div id="message">
            <h3>Password must contain the following:</h3>
            <p id="letter" class="invalid">A <b>lowercase</b> letter</p>
            <p id="capital" class="invalid">A <b>capital (uppercase)</b> letter</p>
            <p id="number" class="invalid">A <b>number</b></p>
            <p id="length" class="invalid">Minimum <b>8 characters</b></p>
        </div>

        <label><b>Repeat Password</b></label>
        <input type="password" placeholder="Repeat Password" name="password-repeat" required>

        <label><b>First name</b></label>
        <input type="text" placeholder="Enter first name" name="first_name" required>

        <label><b>Last name</b></label>
        <input type="text" placeholder="Enter last name" name="last_name" required>

        <div class="clearfix">
            <button type="submit" class="signupbtn">Sign Up</button>
            <button onclick="location.href='login'" type="button" class="loginbtn">Back to login</button>
        </div>
    </div>
</form>

</body>
</html>

<script>
  var myInput = document.getElementById("password");
  var letter = document.getElementById("letter");
  var capital = document.getElementById("capital");
  var number = document.getElementById("number");
  var length = document.getElementById("length");

  // When the user clicks on the password field, show the message box
  myInput.onfocus = function () {
    document.getElementById("message").style.display = "block";
  };

  // When the user clicks outside of the password field, hide the message box
  myInput.onblur = function () {
    document.getElementById("message").style.display = "none";
  };

  // When the user starts to type something inside the password field
  myInput.onkeyup = function () {
    // Validate lowercase letters
    var lowerCaseLetters = /[a-z]/g;
    if (myInput.value.match(lowerCaseLetters)) {
      letter.classList.remove("invalid");
      letter.classList.add("valid");
    } else {
      letter.classList.remove("valid");
      letter.classList.add("invalid");
    }

    // Validate capital letters
    var upperCaseLetters = /[A-Z]/g;
    if (myInput.value.match(upperCaseLetters)) {
      capital.classList.remove("invalid");
      capital.classList.add("valid");
    } else {
      capital.classList.remove("valid");
      capital.classList.add("invalid");
    }

    // Validate numbers
    var numbers = /[0-9]/g;
    if (myInput.value.match(numbers)) {
      number.classList.remove("invalid");
      number.classList.add("valid");
    } else {
      number.classList.remove("valid");
      number.classList.add("invalid");
    }

    // Validate length
    if (myInput.value.length >= 8) {
      length.classList.remove("invalid");
      length.classList.add("valid");
    } else {
      length.classList.remove("valid");
      length.classList.add("invalid");
    }
  }
</script>