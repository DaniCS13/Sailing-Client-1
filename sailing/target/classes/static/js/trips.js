function ajax_show_tracking(trip_id) {
	jQuery("#trackingDialog").modal("show");
	
	var url = "/trips/tracking/" + trip_id;
	
	jQuery("#trackingBody").load(url);
}