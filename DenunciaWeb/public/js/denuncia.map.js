$(document).ready(function() {

	initialLocation = null;
	juizDeFora = new google.maps.LatLng(-21.7290588, -43.382521);
	browserSupportFlag = new Boolean();
	positionGlobal = {};
	
	 myOptions = {
		zoom: 15,                        // set the zoom level manually
		zoomControl: false,
		scaleControl: false,
		scrollwheel: false,
		disableDoubleClickZoom: true,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	
	 map = new google.maps.Map(document.getElementById("gmap"),
			myOptions);

	// Try W3C Geolocation (Preferred)
	if (navigator.geolocation) {
		browserSupportFlag = true;
		navigator.geolocation.getCurrentPosition(function(position) {
			positionGlobal = position;
			initialLocation = new google.maps.LatLng(position.coords.latitude,
					position.coords.longitude);
			map.setCenter(initialLocation);
		}, function() {
				handleNoGeolocation(browserSupportFlag);
		});
	} else {// Browser doesn't support Geolocation
		browserSupportFlag = false;
		handleNoGeolocation(browserSupportFlag);
	}
	
	function handleNoGeolocation(errorFlag) {
		if (errorFlag == true)
			alert("Geolocation service falhou!Nós te colocamos em Juiz de Fora.");
		else
			alert("Seu browser não suporta geolocalização.Nós te colocamos em Juiz de Fora.");

		initialLocation = juizDeFora;
		map.setCenter(initialLocation);
	}
	
	$.ajax({
		type : 'POST',
		url : 'http://denuncia.matheusmarques.com/api/get-near-reports/',
		data : {'max': 50, 'latitude': positionGlobal.coords.latitude, 'longitude': positionGlobal.coords.longitude},
		dataType : 'json'
	}).done(function(data) {
		
	}).fail(function() {
		alert("Erro ao carregar denúncias.");
	});

});