{extends 'layout.tpl'}
{block name=content}
		<h3 class="text-center">#Mapa de Denúncias</h3>
		<div id="gmap" style="height:500px;width:100%;margin-bottom:20px;"></div>
{/block}
{block name=javascript}
    <!-- Maps API Javascript -->
    <script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyA5gTD_bN9U_fl_quA7FrWAz4o21fMTnKo&amp;sensor=false"></script>
	<!-- denuncia.map js -->
	<script src="{$url}js/denuncia.map.js"></script>
{/block}