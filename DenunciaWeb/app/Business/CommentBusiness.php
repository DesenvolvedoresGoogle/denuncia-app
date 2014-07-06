<?php
namespace App\Business;

class CommentBusiness
{

    protected $commentDAO;

    public $validate_erros;

    public function __construct(\Doctrine\ORM\EntityManager $db)
    {
        $this->commentDAO = new \App\Persistence\CommentDAO($db);
        $this->validate_erros = array();
    }

    /**
     * @return \App\Model\Comment
     */
    public function getReportById($id)
    {
        if (is_int($id) || is_numeric($id))
            return $this->commentDAO->getCommentById($id);
        else
            return null;
    }

    public function update(\App\Model\Comment $comment)
    {
        try {
            if (! $this->validate($comment))
                throw new \Exception(implode(',', $this->validate_erros));
            $this->commentDAO->save($comment);
        } catch (\Exception $e) {
            throw new \Exception($e->getMessage());
        }
    }

    public function validate(\App\Model\Comment $comment)
    {
        $this->validate_erros = array();
        
        $this->validateComment($comment->getComment());
        $this->validateUser($comment->getUser());
        
        if (count($this->validate_erros) > 0)
            return false;
        
        return true;
    }

    public function validateComment($title)
    {
        if(!is_null($title) && strlen($title) > 10)
            return true;
        else {
            $this->validate_erros['comment'] = 'Comentário deve possuir pelo menos 10 caracteres';
            return false;
        }
    }
    
    public function validateUser(\App\Model\User $user)
    {
        if(!is_null($user) && !is_null($user->getUserId()))
            return true;
        else {
            $this->validate_erros['user'] = 'Comentário precisa de um usuário';
            return false;
        }
    }

}
