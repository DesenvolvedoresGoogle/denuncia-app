<?php
namespace App\Controller;

class MapController extends BaseController
{

    public function __construct($params)
    {
        parent::__construct($params);
        $this->view->setActiveLink('map');
    }

    public function indexAction()
    {
        $this->view->display('map.tpl');
    }
}
