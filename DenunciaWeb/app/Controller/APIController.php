<?php
namespace App\Controller;

class APIController extends BaseController
{

    public function __construct($params)
    {
        parent::__construct($params, true);
    }

    public function testeAction()
    {
        
        $this->view->display();
    }
}
