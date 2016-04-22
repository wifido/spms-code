<%@taglib prefix="app" uri="/app-tags" %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<head>
    <link rel="stylesheet" type="text/css" href="../ext-3.4.0/resources/css/ext-all.css"/>
    <link rel="stylesheet" type="text/css" href="../scripts/common/ext-datePicker.css" />
    <script type="text/javascript" src="../ext-3.4.0/adapter/ext/ext-base-ajax-sync-request.js"></script>
    <script type="text/javascript" src="../ext-3.4.0/ext-all.js"></script>
  	<script type="text/javascript" src="../scripts/common/moment.js"></script>
    <script type="text/javascript"
            src="../ext-3.4.0/source/locale/ext-lang-${locale.language}_${locale.country}.js"></script>
	<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
     
              
</head>
<body>
<script type="text/javascript">
	<%@include file="/scripts/common/ext-datePicker.js"%>
	<%@include file="/scripts/common/importWind.js"%>
	<%@include file="/scripts/operation/ext-monthpicker.js"%>
	<%@include file="/scripts/driver/addDriverScheduling.js"%>
	<%@include file="/scripts/driver/updateDriverScheduling.js"%>
    <%@include file="/scripts/driver/driverSchedulingManage.js"%>
</script>
</body>
</html>