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
            $query = $this->db->createQuery("SELECT r,ue,c,cu FROM App\Model\Report AS r JOIN r.user AS ue LEFT JOIN r.comments AS c LEFT JOIN c.user AS cu WHERE r.report_id = :id");
            $query->setParameter('id', $id);
            $report = $query->getOneOrNullResult();
        } catch (\Exception $e) {
            $report = null;
        }
        
        return $report;
    }

    public function getReportsArround($lat, $long)
    {
        try {
            $query = $this->db->createQuery("SELECT r, ue, ( 6371 * ACOS( COS( RADIANS(:lat) ) * COS( RADIANS( r.latitude ) ) * COS( RADIANS( r.longitude ) - RADIANS(:long) ) + SIN( RADIANS(:lat) ) * SIN( RADIANS( r.latitude ) ) ) ) AS distance FROM App\Model\Report AS r JOIN r.user AS ue HAVING distance < 25 ORDER BY distance");
            $query->setParameter('lat', $lat);
            $query->setParameter('long', $long);
            $result = $query->getResult();
            $report_list = array();
            
            foreach($result as $report)
                $report_list[] = $report[0];
            
        } catch (\Exception $e) {
            $report_list = null;
        }
        
        return $report_list;
    }

    public function getAllReports($start, $max)
    {
        try {
            $query = $this->db->createQuery("SELECT r,ue FROM App\Model\Report AS r JOIN r.user AS ue ORDER BY r.creation_date DESC");
            
            if (! is_null($start) && ! is_null($max))
                $query->setFirstResult($start)->setMaxResults($max);
            
            $report_list = $query->getResult();
        } catch (\Exception $e) {
            $report_list = null;
        }
        
        return $report_list;
    }
}
