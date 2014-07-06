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

    private function getLoggedUser()
    {
        $_POST['token'] = (isset($_POST['token']) ? $_POST['token'] : null);
        $user = $this->user_business->getUserByToken($_POST['token']);
        if (is_null($user)) {
            $this->view->assign('erro', 'Logout');
            
            $this->view->display();
            exit();
        } else
            return $user;
    }

    public function loginUserAction()
    {
        if (isset($_POST['google_id'])) {
            
            $token = sha1(microtime() . $_POST['google_id']);
            $user = $this->user_business->getUserByGoogleId($_POST['google_id']);
            
            if (is_null($user)){
                $user = new \App\Model\User();
                $user->setGoogleId(isset($_POST['google_id']) ? $_POST['google_id'] : null);
                $user->setName(isset($_POST['name']) ? $_POST['name'] : null);
                $user->setPhoto(isset($_POST['photo']) ? $_POST['photo'] : null);
            }
            
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

    public function createReportAction()
    {
        $user = $this->getLoggedUser();
        
        $report = new \App\Model\Report();
        $report->setTitle(isset($_POST['title']) ? $_POST['title'] : null);
        $report->setDescription(isset($_POST['description']) ? $_POST['description'] : null);
        $report->setPhoto(isset($_POST['photo']) ? $_POST['photo'] : null);
        $report->setLatitude(isset($_POST['latitude']) ? $_POST['latitude'] : null);
        $report->setLongitude(isset($_POST['longitude']) ? $_POST['longitude'] : null);
        $report->setAddress(isset($_POST['address']) ? $_POST['address'] : null);
        $report->setCreationDate(new \DateTime("now"));
        $report->setUser($user);
        
        $report_business = new \App\Business\ReportBusiness($this->db);
        
        try {
            $report_business->update($report);
            $this->view->assign('report', $report->toArray());
        } catch (\Exception $e) {
            $this->view->assign('erro', $e->getMessage());
        }
        
        $this->view->display();
    }

    public function addCommentAction()
    {
        $user = $this->getLoggedUser();
        $_POST['report_id'] = (isset($_POST['report_id']) ? $_POST['report_id'] : null);
        $report = (new \App\Business\ReportBusiness($this->db))->getReportById($_POST['report_id']);
        if (! is_null($report)) {
            $comment_business = new \App\Business\CommentBusiness($this->db);
        } else
            $this->view->assign('erro', 'Denuncia não encontrada');
        
        $this->view->display();
    }

    public function getNearReportsAction()
    {
        //if(!isset($_POST['max']))
         //   $user = $this->getLoggedUser();
        if (isset($_POST['latitude']) && isset($_POST['longitude'])) {
            $report_business = new \App\Business\ReportBusiness($this->db);
            $reports = $report_business->getReportsArround($_POST['latitude'], $_POST['longitude']);
            $result = array();
            if (! is_null($reports)) {
                foreach ($reports as $report) {
                    $result[] = $report->toArray();
                }
            }
            $this->view->assign('reports', $result);
        } else
            $this->view->assign('erro', 'Necessário enviar latitude e longitude');
        
        $this->view->display();
    }

    public function testeAction()
    {
        $report_business = new \App\Business\ReportBusiness($this->db);
        $reports = $report_business->getReportsArround(10.6, 10.6);
        $result = array();
        foreach ($reports as $report) {
            $result[] = $report->toArray();
        }
        
        $this->view->assign('reports', $result);
        
        $this->view->display();
    }
}
