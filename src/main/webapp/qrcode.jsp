<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<meta charset="utf-8" />

<head>
<title>QR Code</title>
<script src="./js/jquery-latest.min.js"></script>
<style type="text/css">
body{        
	width: 95%;
    margin: 0 auto; /* Center the DIV horizontally */
    padding-top: 10px;
    padding-bottom: 10px;
    height: 9.25in;    
}
.textfield{
	border-color: lightgrey;
	border-width: thin;
}
img { width: 100px; height: 100px; image-rendering: pixelated; }
</style>
</head>

<body>

<h1>Generate QR Code :</h1> 
<label style="background:lightyellow;">For Wi-Fi : "WIFI:S:&lt;ssid&gt;;T:WPA;P:&lt;password&gt;;H:false;;"</label>
<br/>
<label>Enter text : </label>
<input id="data" class="textfield" type="text" size="100" value="How to convert octet-stream to image ? Work around using Base64"/>
<br/>

<p><input type="button" id="submit" value="Submit"/></p>
<p><img id="qr_image" alt="QR Code" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPoAAAD6AQAAAACgl2eQAAAA0ElEQVR42u3YXQrEIAwE4Nx8crS9mUs1iVpQln3MxELpz/cUdFIr7T4+UqBAgQIFfgAqfaApVBB3fAB21qHmMzbwvLEqwe94wXOBVqBfgRms59vKSg4sOqNMp6RNDnyMALl03uSgLxTxagl8urABr1Dvq9Zd9wnDARTL4TUjBBGi9lj9a4sMaHxz4xUiXCCC1FDzdUMGlsY6EgR67LypAWaZfCemlMA2YttLXuA9dWYIJ9gPRhBbMJ8wr8ZKApYgtQDRY9ImBvW/ukCBAgX+Bl93qdukVJFmRQAAAABJRU5ErkJggg==" 
width="500px" height="500px"></p>
<script type="text/javascript">
$(document).ready(function() {
	
	$('#submit').on("click", function(e){
		generateQRCode(e);
	})
	
	$('#data').on("keypress", function(e){
		if(e.which == 13) { generateQRCode(e); }
	})

})

function generateQRCode(e){
	var settings = {
			"url": "http://localhost:8080/rest/qr/base64",
			"method": "POST",
			"timeout": 0,
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded"
			},
			"data": {
			  "data": $('#data').val()
			}
		};
	
	$.ajax(settings).done(function (response) {
		console.log(response);
		$('#qr_image').attr('src', 'data:image/png;base64,'+response);
		var a = document.createElement("a"); //Create anchor <a>
	    a.href = "data:image/png;base64," + response; //Image Base64 Goes here
	    a.download = "qrcode.png";
	    a.click(); //Downloaded file
	})		
	.fail(function(xhr, status, error) {
		console.log(status + " " + error);
	});
	e.preventDefault();
}
</script>
</body>
</html>