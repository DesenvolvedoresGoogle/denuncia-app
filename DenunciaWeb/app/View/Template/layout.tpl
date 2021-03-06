<!DOCTYPE html>
<html lang="pt">
<head>
<meta charset="utf-8">
<title>DenunciaAPP</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap css -->
<link href="{$url}css/bootstrap.min.css" rel="stylesheet">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

<!-- DenunciaAPP css -->
<link href="{$url}css/denuncia.css" rel="stylesheet">
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="{$url}">Denuncia APP</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li {if $active_link== "home"}class="active"{/if}><a href="{$url}">Home</a></li>
					<li {if $active_link== "map"}class="active"{/if}><a href="{$url}map/">Mapa</a></li>
					<li class="dropdown {if $active_link== "admin"}active{/if}"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Administração <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="{$url}admin/view-reports/">Lista de Denúncias</a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="container content">
		{block name=content}{/block}
	</div>

	<!-- jQuery -->
	<script src="{$url}js/jquery-2.1.1.min.js"></script>
	<!-- Bootstrap js -->
	<script src="{$url}js/bootstrap.min.js"></script>
	{block name=javascript}{/block}
</body>
</html>