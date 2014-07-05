<?php
namespace App\Controller;

class HomeController extends BaseController
{

    public function __construct($params)
    {
        parent::__construct($params);
    }

    public function indexAction()
    {
        $this->view->display('home.tpl');
    }
}
