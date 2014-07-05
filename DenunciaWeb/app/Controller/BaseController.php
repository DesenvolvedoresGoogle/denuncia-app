<?php
namespace App\Controller;

class BaseController
{

    protected $params;

    protected $config;

    protected $view;

    protected $db;

    public function __construct($params)
    {
        session_start();
        $this->params = $params;
        $this->config = require_once APP_PATH . '/config.php';
        $this->view = new \App\View\View();
        
        try {
            $doctrine_util = new \App\Persistence\DoctrineUtil($this->config['database']);
            $this->db = $doctrine_util->getDatabaseEntityManager();
        } catch (\Exception $e) {
            $this->fatalErrorAction();
        }
    }

    public function pageNotFoundAction()
    {
        header("HTTP/1.0 404 Not Found");
        $this->view->display('404.tpl');
        exit();
    }

    public function fatalErrorAction()
    {
        header("HTTP/1.0 500 Internal Server Error");
        $this->view->display('500.tpl');
        exit();
    }
}
