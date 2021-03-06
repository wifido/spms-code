<%@taglib prefix="app" uri="/app-tags" %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<head>
    <link rel="stylesheet" type="text/css" href="../ext-3.4.0/resources/css/ext-all.css"/>
    <script type="text/javascript" src="../ext-3.4.0/adapter/ext/ext-base-ajax-sync-request.js"></script>
    <script type="text/javascript" charset="utf-8" src="../ext-3.4.0/ext-all.js"></script>
    <script type="text/javascript" charset="utf-8" src="../ext-3.4.0/source/locale/ext-lang-${locale.language}_${locale.country}.js"></script>
   	<script type="text/javascript" src="../scripts/common/moment.js"></script>
	<link rel="stylesheet" type="text/css" href="../scripts/common/ext-datePicker.css" />
	<script type="text/javascript" src="../ext-3.4.0/ux/PagingMemoryProxy.js"></script>
	<script type="text/javascript" src="../ext-3.4.0/src/locale/ext-lang-${locale.language}_${locale.country}.js"></script> 
</head>
<body>
<script type="text/javascript" charset="utf-8">

	<%@include file="/scripts/common/sfTree.js"%>
	<%@include file="/scripts/common/ext-datePicker.js"%>
	<%@include file="/scripts/operation/ext-monthpicker.js"%>
	 <%@include file="/scripts/getWorkType.js"%>
	<%@include file="/scripts/warehouse/exportWarehouseScheduling.js"%>
	<%@include file="/scripts/warehouse/importWarehouseScheduling.js"%>
	<%@include file="/scripts/warehouse/updateWarehouseScheduling.js"%>
	<%@include file="/scripts/warehouse/addWarehouseScheduling.js"%>
	<%@include file="/scripts/warehouse/warehouseScheduling.js"%>
</script>
</body>
</html>