<?php
header('Content-Type: text/html; charset=UTF-8');
date_default_timezone_set('America/Sao_Paulo');

define('APP_PATH', realpath(dirname(__FILE__) . '/../app'));
define('CACHE_PATH', APP_PATH . '/Cache');
$url_path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);

if (! is_dir(APP_PATH . '/Cache/') || ! is_writable(APP_PATH . '/Cache/') || ! is_readable(APP_PATH . '/Cache/'))
    exit('Pasta app/Cache precisa ter permissão 777');

if (strstr($_SERVER['SERVER_NAME'], 'localhost')) {
    define('URL', 'http://localhost/DenunciaApp/DenunciaWeb/public/');
    define('URL_PATH', str_replace('/DenunciaApp/DenunciaWeb/public', '', $url_path));
    ini_set('display_errors', 1);
    error_reporting(E_ALL);
} else {
    define('URL', 'http://matheusmarques.com/DenunciaWeb/');
    define('URL_PATH', str_replace('/DenunciaWeb', '', $url_path));
    define('PRODUCTION', true);
    ini_set('session.cookie_domain', '.matheusmarques.com');
    ini_set('display_errors', 0);
}

require_once APP_PATH . '/bootstrap.php';