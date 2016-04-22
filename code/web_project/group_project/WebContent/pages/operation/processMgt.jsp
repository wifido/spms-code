<%@taglib prefix="app" uri="/app-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>排班</title>
	<link rel="stylesheet" type="text/css" href="../ext-3.4.0/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="../scripts/businessMgt/ext-datePicker.css" />
	<script type="text/javascript" src="../ext-3.4.0/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="../ext-3.4.0/ext-all.js"></script>
	<script type="text/javascript" src="../ext-3.4.0/ux/PagingMemoryProxy.js"></script>
	<script type="text/javascript" src="../ext-3.4.0/src/locale/ext-lang-${locale.language}_${locale.country}.js"></script> 
</head>
<body>
<script type="text/javascript">
<%@include file="/ext-3.4.0/sf/ext.js"%>
<%@include file="/scripts/operation/ext-datePicker.js"%>
<%@include file="/scripts/operation/ext-monthpicker.js"%>
<%@include file="/scripts/getWorkType.js"%>
<%@include file="/scripts/operation/processMgt.js"%>
</script>
<style type="text/css">
.cell_bgcolor {
  background-color: rgb(192,192,192);
}
.unfinished_cell_bgcolor {
  background-color: rgb(232,84,84);
}
.cell_blue_bgcolor {
  background-color: rgb(151,213,255);
}
</style>
</body>
</html>