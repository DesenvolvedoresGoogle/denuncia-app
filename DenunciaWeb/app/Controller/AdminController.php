<?php
namespace App\Controller;

use Aura\Router\Exception;

class AdminController extends BaseController
{

    public function __construct($params)
    {
        parent::__construct($params);
        $this->view->setActiveLink('admin');
    }

    public function viewReportsAction()
    {
        if (isset($this->params['page'][0]) && ! is_null($this->params['page'][0]))
            $page = $this->params['page'][0];
        else
            $page = 1;
        
        $max_per_page = 5;
        
        try {
            $reports = (new \App\Business\ReportBusiness($this->db))->getAllReports(($page - 1) * $max_per_page, $max_per_page + 1);
            
            if (! is_null($reports) && count($reports) > 0) {
                if (count($reports) > $max_per_page) {
                    array_pop($reports);
                    $this->view->assign('hasNextPage', true);
                }
            } elseif ($page != 1)
                throw new \Exception('Página inválida');
            
            $this->view->assign('page', $page);
            $this->view->assign('reports', $reports);
            $this->view->display('admin-view-reports.tpl');
        } catch (\Exception $e) {
            $this->pageNotFoundAction();
        }
    }
    
    public function viewReportAction()
    {
        $report = (new \App\Business\ReportBusiness($this->db))->getReportById($this->params['id']);
        if (! is_null($report)) {
            $this->view->assign('report', $report);
            $this->view->display('admin-view-report.tpl');
        } else
            $this->pageNotFoundAction();
    }
    
    public function viewUserAction()
    {
        $user = (new \App\Business\UserBusiness($this->db))->getUserById($this->params['id']);
        if (! is_null($user)) {
            $this->view->assign('user', $user);
            $this->view->display('admin-view-user.tpl');
        } else
            $this->pageNotFoundAction();
    }
}
