<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
<title>Online Bank</title>
</head>
<body>
    <h1>Sign In</h1>
    <p>${error}</p>
    <form:form action="/signin" method="POST" modelAttribute="user">
      <form:hidden path="id"/>
          <table>
           <tr>
                <td>Login</td>
                <td><form:input path="login"/></td>
           </tr>
           <tr>
                <td>Password</td>
                <td><form:input type="password" path="password"/></td>
           </tr>
           <tr>
                <td></td>
                <td><input type="submit" name="sign-in" value="Sign In"/></td>
           </tr>
           <tr>
               <td></td>
               <td><input type="submit" name="sign-up" value="Sign Up"/></td>
          </tr>
          </table>
     </form:form>

</body>
</html>