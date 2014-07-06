{extends 'layout.tpl'}
{block name=content}
        <h3 class="text-center">#Lista de Denúncias</h3>
            
        <table class="table table-striped table-hover">
        	<tr>
        		<th></th>
        		<th>Título</th>
        		<th>Endereço</th>
        		<th>Usuário</th>
        		<th></th>
        	</tr>
        	{foreach $reports as $report}
        	<tr>
        		<td><img src="{$url}images/{$report->getPhoto()}" class="img-circle" height="30" width="30"></td>
        		<td>{$report->getTitle()}</td>
        		<td>{$report->getAddress()}</td>
        		<td><a href="{$url}admin/view-user/id-{$report->getUser()->getUserId()}/" target="_blank">{$report->getUser()->getName()}</a></td>
        		<td><a href="{$url}admin/view-report/id-{$report->getReportId()}/" target="_blank"><span class="glyphicon glyphicon-cog"></span></a></td>
        	</tr>
        	{foreachelse}
        	   <tr>
        		<td colspan="5"><h4 class="text-center">Nada encontrado!</h4></td>
        	</tr>
        	{/foreach}
        
        </table>
        <ul class="pager">
        	{if $page > 1}
        	   <li class="previous"><a href="{$url}admin/view-reports/page-{$page-1}/"><span class="glyphicon glyphicon-arrow-left"></span> Anterior</a></li>
        	{/if}
        	{if isset($hasNextPage)}
        	<li class="next"><a href="{$url}admin/view-reports/page-{$page+1}/">Próxima <span class="glyphicon glyphicon-arrow-right"></span></a></li>
        	{/if}
        </ul>
{/block}
