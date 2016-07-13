<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<jsp:include page="header.jsp"/>
<s:form cssClass="form-horizontal">
    <div class="form-group">
        <label class="col-sm-2 control-label">USER:</label>
        <div class="col-sm-10">
            <s:textfield name="userId" cssClass="form-control"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">PASSWORD:</label>
        <div class="col-sm-10">
            <s:textfield name="password" type="password" cssClass="form-control"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <s:submit method="login" value="LOGIN" cssClass="btn btn-primary"/>
        </div>
    </div>
</s:form>
<p class="err"><s:property value="errmsg" /></p>
<jsp:include page="footer.jsp"/>