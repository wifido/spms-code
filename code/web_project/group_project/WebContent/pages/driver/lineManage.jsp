<%@taglib prefix="app" uri="/app-tags" %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<head>
    <link rel="stylesheet" type="text/css" href="../ext-3.4.0/resources/css/ext-all.css"/>
    <script type="text/javascript" src="../ext-3.4.0/adapter/ext/ext-base-ajax-sync-request.js"></script>
    <script type="text/javascript" src="../ext-3.4.0/ext-all.js"></script>
    <script type="text/javascript"
            src="../ext-3.4.0/source/locale/ext-lang-${locale.language}_${locale.country}.js"></script>
</head>
<body>
<script type="text/javascript">
	<%@include file="/scripts/common/departmentWind.js"%>
	<%@include file="/scripts/driver/validateTime.js"%>
    <%@include file="/scripts/driver/addLine.js"%>
    <%@include file="/scripts/driver/exportLine.js"%>
    <%@include file="/scripts/driver/lineManage.js"%>
</script>
</body>
</html>