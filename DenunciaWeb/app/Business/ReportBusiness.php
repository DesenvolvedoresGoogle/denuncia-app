<?php
namespace App\Business;

class ReportBusiness
{

    protected $reportDAO;

    public $validate_erros;

    public function __construct(\Doctrine\ORM\EntityManager $db)
    {
        $this->reportDAO = new \App\Persistence\ReportDAO($db);
        $this->validate_erros = array();
    }

    /**
     * @return \App\Model\Report
     */
    public function getReportById($id)
    {
        if (is_int($id) || is_numeric($id))
            return $this->reportDAO->getReportById($id);
        else
            return null;
    }
    
    /**
     * @return \App\Model\Report
     */
    public function getReportsArround($lat, $long)
    {
        if ((is_double($lat) || is_numeric($lat)) && (is_double($long) || is_numeric($long)))
            return $this->reportDAO->getReportsArround($lat, $long);
        else
            return null;
    }
    
    /**
     * @return \App\Model\Report
     */
    public function getAllReports($start = null, $max = null)
    {
        return $this->reportDAO->getAllReports($start, $max);
    }

    public function update(\App\Model\Report $report)
    {
        try {
            if (! $this->validate($report))
                throw new \Exception(implode('</p><p>', $this->validate_erros));
            $this->reportDAO->save($report);
        } catch (\Exception $e) {
            throw new \Exception($e->getMessage());
        }
    }

    public function validate(\App\Model\Report $report)
    {
        $this->validate_erros = array();
        
        $this->validateTitle($report->getTitle());
        $this->validateDescription($report->getDescription());
        $this->validateUser($report->getUser());
        $this->validatePhoto($report->getPhoto());
        
        if (count($this->validate_erros) > 0)
            return false;
        
        return true;
    }

    public function validateTitle($title)
    {
        if(!is_null($title) && strlen($title) > 5)
            return true;
        else {
            $this->validate_erros['title'] = 'Título deve possuir pelo menos 5 caracteres';
            return false;
        }
    }
    
    public function validateDescription($description)
    {
        if(!is_null($description) && strlen($description) > 10)
            return true;
        else {
            $this->validate_erros['description'] = 'Descrição deve possuir pelo menos 10 caracteres';
            return false;
        }
    }

    public function validateUser(\App\Model\User $user)
    {
        if(!is_null($user) && !is_null($user->getUserId()))
            return true;
        else {
            $this->validate_erros['user'] = 'Denuncia precisa de um usuário';
            return false;
        }
    }
    
    public function validatePhoto($photo)
    {
        if(!is_null($photo) && strlen($photo) > 0)
            return true;
        else {
            $this->validate_erros['photo'] = 'Denuncia precisa de uma foto';
            return false;
        }
    }
}
