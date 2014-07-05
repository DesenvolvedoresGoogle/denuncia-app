<?php
namespace App\Persistence;

class ReportDAO extends BaseDAO
{
    
    protected $db;

    public function __construct(\Doctrine\ORM\EntityManager $db)
    {
        $this->db = $db;
    }

    public function getReportById($id)
    {
        try {
            $query = $this->db->createQuery("SELECT r,ue FROM App\Model\Report AS r JOIN r.user AS ue WHERE r.report_id = :id");
            $query->setParameter('id', $id);
            $user = $query->getOneOrNullResult();
        } catch (\Exception $e) {
            $user = null;
        }
    
        return $user;
    }
    
    public function getAllReports($start, $max)
    {
        try {
            $query = $this->db->createQuery("SELECT r,ue FROM App\Model\Report AS r JOIN r.user AS ue ORDER BY r.report_id DESC");
            
            if(!is_null($start) && !is_null($max))
                $query->setFirstResult($start)->setMaxResults($max);
            
            $report_list = $query->getResult();
        } catch (\Exception $e) {
            $report_list = null;
        }
        
        return $report_list;
    }
    
}
