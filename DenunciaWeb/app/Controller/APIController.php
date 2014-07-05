<?php
namespace App\Controller;

class APIController extends BaseController
{

    public function __construct($params)
    {
        parent::__construct($params, true);
    }

    public function loginUserAction()
    {
        if (isset($_POST['google_id'])) {
            $user_bussines = new \App\Business\UserBusiness($this->db);
            $user = new \App\Model\User($_POST);
        } else
            $this->view->assign('erro', 'Necessário passar o google_id');
    }

    public function testeAction()
    {
        $this->view->display();
    }
}
