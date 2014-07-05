<?php
namespace App\Persistence;

class UserDAO extends BaseDAO
{

    protected $db;

    public function __construct(\Doctrine\ORM\EntityManager $db)
    {
        $this->db = $db;
    }

    public function getUserByGoogleId($g_id)
    {
        try {
            $query = $this->db->createQuery("SELECT u FROM App\Model\User AS u WHERE u.google_id = :gid");
            $query->setParameter('gid', $g_id);
            $user = $query->getOneOrNullResult();
        } catch (\Exception $e) {
            $user = null;
        }
        
        return $user;
    }

    public function getUserByUserId($user_id)
    {
        try {
            $query = $this->db->createQuery("SELECT u FROM App\Model\User AS u WHERE u.user_id = :userid");
            $query->setParameter('userid', $user_id);
            $user = $query->getOneOrNullResult();
        } catch (\Exception $e) {
            $user = null;
        }
        
        return $user;
    }
    
    public function getUserByToken($token)
    {
        try {
            $query = $this->db->createQuery("SELECT u FROM App\Model\User AS u WHERE u.token = :token");
            $query->setParameter('token', $token);
            $user = $query->getOneOrNullResult();
        } catch (\Exception $e) {
            $user = null;
        }
    
        return $user;
    }

    public function getAllUsers($start, $max)
    {
        try {
            $query = $this->db->createQuery("SELECT u FROM App\Model\User AS u ORDER BY u.name ASC");
            
            if(!is_null($start) && !is_null($max))
                $query->setFirstResult($start)->setMaxResults($max);
            
            $user_list = $query->getResult();
        } catch (\Exception $e) {
            $user_list = null;
        }
        
        return $user_list;
    }
 
}
