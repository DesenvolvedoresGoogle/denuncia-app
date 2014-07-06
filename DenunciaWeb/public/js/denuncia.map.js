$(document).ready(function() {

	initialLocation = null;
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
	 markers = [];
	 timeout = null;
	 
	// Try W3C Geolocation (Preferred)
	if (navigator.geolocation) {
		browserSupportFlag = true;
		navigator.geolocation.getCurrentPosition(function(position) {
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

		initialLocation = new google.maps.LatLng(-21.7290588, -43.382521);
		map.setCenter(initialLocation);
	}
	
	google.maps.event.addListener(map, 'center_changed', function() {
		deleteMarkers();
		clearTimeout(timeout);
		timeout = setTimeout(function() {
	    	
	      loadPoints(map.getCenter().lat(), map.getCenter().lng());
	    }, 3000);
	  });
	
	function loadPoints(latitude, longitude) {
		$.ajax({
			type : 'POST',
			url : 'http://localhost/DenunciaApp/DenunciaWeb/public/api/get-near-reports/',
			data : {'max': 50, 'latitude': latitude, 'longitude': longitude},
			dataType : 'json'
		}).done(function(data) {
			$.each(data.reports, function(index, report) {
				 
	            var marker = new google.maps.Marker({
	                position: new google.maps.LatLng(report.latitude, report.longitude),
	                title: report.title,
	                map: map
	            });
	            
	            markers.push(marker);
	 
	        });
		}).fail(function() {
			alert("Erro ao carregar denúncias.");
		});
	}
	
	// Sets the map on all markers in the array.
	function setAllMap(map) {
	  for (var i = 0; i < markers.length; i++) {
	    markers[i].setMap(map);
	  }
	}

	// Removes the markers from the map, but keeps them in the array.
	function clearMarkers() {
	  setAllMap(null);
	}

	// Shows any markers currently in the array.
	function showMarkers() {
	  setAllMap(map);
	}

	// Deletes all markers in the array by removing references to them.
	function deleteMarkers() {
	  clearMarkers();
	  markers = [];
	}

});