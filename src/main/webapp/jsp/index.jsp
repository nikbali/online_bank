<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
<title>Online Bank</title>
</head>
<body>
    <form:form action="/users/signin" method="POST" modelAttribute="user">
      <form:hidden path="id"/>
          <table>
           <tr>
                <td>Login</td>
                <td><form:input path="login"/></td>
           </tr>
           <tr>
                <td>Password</td>
                <td><form:input path="password"/></td>
           </tr>
           <tr>
                <td></td>
                <td><button type="submit">Sign In</button></td>
           </tr>
           <tr>
               <td></td>
               <td><button type="submit">Sign Up</button></td>
          </tr>
          </table>
     </form:form>

</body>
</html>