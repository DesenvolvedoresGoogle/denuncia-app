<?php
namespace App\Business;

class UserBusiness
{

    protected $userDAO;

    public $validate_erros;

    public function __construct(\Doctrine\ORM\EntityManager $db)
    {
        $this->userDAO = new \App\Persistence\UserDAO($db);
        $this->validate_erros = array();
    }

    /**
     * @return \App\Model\User
     */
    public function getUserByGoogleId($g_id)
    {
        if (is_int($g_id) || is_numeric($g_id))
            return $this->userDAO->getUserByGoogleId($g_id);
        else
            return null;
    }

    /**
     * @return \App\Model\User
     */
    public function getUserByUserId($user_id)
    {
        if (is_int($user_id) || is_numeric($user_id))
            return $this->userDAO->getUserByUserId($user_id);
        else
            return null;
    }
    
    /**
     * @return \App\Model\User
     */
    public function getUserByToken($token)
    {
        if (!is_null($token))
            return $this->userDAO->getUserByToken($token);
        else
            return null;
    }
    
    public function getAllUsers($start = null, $max = null)
    {
        return $this->userDAO->getAllUsers($start, $max);
    }

    /**
     * @param \App\Model\User $user            
     */
    public function update(\App\Model\User $user)
    {
        try {
            if (! $this->validate($user))
                throw new \Exception(implode('</p><p>', $this->validate_erros));
            $this->userDAO->save($user);
        } catch (\Exception $e) {
            throw new \Exception($e->getMessage());
        }
    }

    public function validate(\App\Model\User $user)
    {
        $this->validate_erros = array();
        
        $this->validateUserType($user->getType());
        
        if (count($this->validate_erros) > 0)
            return false;
        
        return true;
    }
}
