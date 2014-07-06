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
                throw new \Exception();
            
            $this->view->assign('page', $page);
            $this->view->assign('reports', $reports);
            $this->view->display('view-reports.tpl');
        } catch (\Exception $e) {
            $this->pageNotFoundAction();
        }
    }
}
