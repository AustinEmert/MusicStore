<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">

<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/1.10.25/css/jquery.dataTables.css">
<script type="text/javascript" charset="uft8"
	src="https://cdn.datatables.net/1.10.25/js/jquery.dataTables.min.js"></script>
</head>

<body>
	
	<div class="container">
		<h1>All Items</h1>
		<table id="itemTable" class="display">

			<thead>
				<tr>
					<th>Name</th>
					<th>Price</th>
					<th>Image</th>
					<th>Category</th>
					<th>Description</th>
					<th>View Item</th>

				</tr>
			</thead>
		</table>
	</div>

</body>




<script>
	$(document).ready(function() {
		var table = $('#itemTable').DataTable(
		{
			"sAjaxSource" : "/item",
			"sAjaxDataProp" : "",
			"order" : [ [ 0, "asc" ] ],
			"aoColumns" : [
				{"mData" : "name"},
				{"mData" : "price",
				 "mRender": function(mData, type, row){
					 return '$'+mData
				}
				},
				{"mData" : "thumbnail",
				"mRender" : function(mData, type, row) {
					return '<img src='+mData+' alt="images" width="100" height="75">'
				}
				}, 
				{"mData" : "category"}, 
				{"mData" : "description"},
				{"mData" : "id",
				"mRender" : function(mData, type, row) {
					return '<a class="btn btn-primary" href="/catalog/'+mData+'" role="button">View</button>'
				}}
				]
		})

	});
</script>

</html>