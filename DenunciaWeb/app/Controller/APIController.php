<?php
namespace App\Controller;

class APIController extends BaseController
{

    protected $user_business;

    public function __construct($params)
    {
        parent::__construct($params, true);
        $this->user_business = new \App\Business\UserBusiness($this->db);
    }

    public function loginUserAction()
    {
        if (isset($_POST['google_id'])) {
            
            $token = sha1(microtime() . $_POST['google_id']);
            $user = $this->user_business->getUserByGoogleId($_POST['google_id']);
            
            if (is_null($user))
                $user = new \App\Model\User($_POST); // Cadastro do novo usuário
            
            $user->setToken($token);
            
            try {
                $this->user_business->update($user);
                $this->view->assign('token', $user->getToken());
            } catch (\Exception $e) {
                $this->view->assign('erro', $e->getMessage());
            }
        } else
            $this->view->assign('erro', 'Necessário google_id');
        
        $this->view->display();
    }

    public function testeAction()
    {
        $user = $this->user_business->getUserByToken($_POST['token']);
        if (is_null($user))
            $this->view->assign('erro', 'Disconnect');
        else
            $this->view->assign('name', $user->getName());
        
        $this->view->display();
    }
}
