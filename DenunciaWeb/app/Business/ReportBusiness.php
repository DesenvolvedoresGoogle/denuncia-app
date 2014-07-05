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
    public function getReportById($f_id)
    {
        if (is_int($id) || is_numeric($id))
            return $this->reportDAO->getReportById($id);
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
        
        if (count($this->validate_erros) > 0)
            return false;
        
        return true;
    }

    public function validateTitle($title)
    {
        if(!is_null($title) && strlen($title) > 10)
            return true;
        else {
            $this->validate_erros['title'] = 'Título deve possuir pelo menos 10 caracteres';
            return false;
        }
    }
    
    public function validateDescription($description)
    {
        if(!is_null($description) && strlen($description) > 20)
            return true;
        else {
            $this->validate_erros['description'] = 'Descrição deve possuir pelo menos 20 caracteres';
            return false;
        }
    }

}