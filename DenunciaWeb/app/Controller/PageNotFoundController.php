<?php
namespace App\Controller;

class PageNotFoundController extends BaseController
{

    public function __construct($params = array())
    {
        parent::__construct($params);
        
        $this->pageNotFoundAction();
    }
}
