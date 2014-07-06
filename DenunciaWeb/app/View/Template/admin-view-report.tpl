{extends 'layout.tpl'}
{block name=content}
<h3 class="text-center">#Visualizar Denúncia</h3>
<div class="row" style="padding: 10px;">
	<div class="col-md-3">
		<img src="{$url}images/{$report->getPhoto()}" class="img-thumbnail">
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
{/block}
