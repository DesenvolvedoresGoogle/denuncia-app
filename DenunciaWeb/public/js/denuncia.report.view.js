$(document).ready(function() {

	myOptions = {
		zoom : 14,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

	map = new google.maps.Map(document.getElementById("gmap"), myOptions);
	initialLocation = new google.maps.LatLng(ReportLatitude, ReportLongitude);
	map.setCenter(initialLocation);

	var marker = new google.maps.Marker({
		  position: initialLocation,
	      map: map,
	      title: 'Local',
	  });
	
});