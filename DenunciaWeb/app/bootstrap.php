<?php
require_once APP_PATH . '/../vendor/autoload.php';
require_once APP_PATH . '/routers.php';

function getCamelCase($string)
{
    $result = '';
    foreach (explode('-', $string) as $str)
        $result .= ucfirst(strtolower($str));
    
    return lcfirst($result);
}

// Dispatching
$route = $router->match(URL_PATH, $_SERVER);

if ($route) {
    $params = array();
    foreach ($route->params as $key => $value) {
        if (! is_null($value) && isset($route->tokens[$key]) && strstr($route->tokens[$key], ')')) {
            preg_match('#' . $route->tokens[$key] . '#', $value, $value);
            array_shift($value);
        }
        $params[$key] = $value;
    }
    
    $controller_class = '\App\Controller\\' . $route->params['controller'] . 'Controller';
    $action = getCamelCase($route->params['action']) . 'Action';
    
    $controller = new $controller_class($params);
    $controller->$action();
} else
    new \App\Controller\PageNotFoundController();

