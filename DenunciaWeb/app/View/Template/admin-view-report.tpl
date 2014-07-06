{extends 'layout.tpl'}
{block name=content}
<h3 class="text-center">#Visualizar Denúncia</h3>
<div class="row" style="padding: 10px;">
	<div class="col-md-3">
		<img src="{$url}images/{$report->getPhoto()}" class="img-thumbnail">
		<div id="gmap" style="height:150px;width:100%;margin-top:20px;"></div>
	</div>
	<div class="col-md-9">
        <form name="edit" method="post" class="form-horizontal" role="form">
			<div class="form-group">
				<label class="col-sm-3 control-label">Título</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="{$report->getTitle()}" disabled>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">Descrição</label>
				<div class="col-sm-9">
					<textarea class="form-control" rows="3" disabled>{$report->getDescription()}</textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">Foto</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="{$report->getPhoto()}" disabled>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">Latitude</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="{$report->getLatitude()}" disabled>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">Longitude</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="{$report->getLongitude()}" disabled>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">Endereço</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="{$report->getAddress()}" disabled>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label"><a href="{$url}admin/view-user/id-{$report->getUser()->getUserId()}/" target="_blank">Usuário</a></label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="{$report->getUser()->getName()}" disabled>
				</div>
			</div>
		</form>
	</div>
</div>
<h2 class="text-center" style="margin:20px;">Comentários</h2>
<table class="table table-striped table-hover">
        	<tr>
        		<th></th>
        		<th>Usuário</th>
        		<th>Comentário</th>
        	</tr>
        	{foreach $report->getComments() as $comment}
        	<tr>
        		<td><img src="{$comment->getUser()->getPhoto()}" class="img-circle" height="30" width="30"></td>
        		<td><a href="{$url}admin/view-user/id-{$comment->getUser()->getUserId()}/" target="_blank">{$comment->getUser()->getName()}</a></td>
        		<td>{$comment->getComment()}</td>
        	</tr>
        	{foreachelse}
        	   <tr>
        		<td colspan="3"><h4 class="text-center">Nada encontrado!</h4></td>
        	</tr>
        	{/foreach}
        
        </table>
{/block}
{block name=javascript}
    <!-- Maps API Javascript -->
    <script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyA5gTD_bN9U_fl_quA7FrWAz4o21fMTnKo&amp;sensor=false"></script>
	<script>
		ReportLatitude = "{$report->getLatitude()}";
		ReportLongitude = "{$report->getLongitude()}";
	</script>
	<!-- denuncia.map js -->
	<script src="{$url}js/denuncia.report.view.js"></script>
{/block}
