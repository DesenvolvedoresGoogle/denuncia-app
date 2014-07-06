<?php
namespace App\Persistence;

class CommentDAO extends BaseDAO
{

    protected $db;

    public function __construct(\Doctrine\ORM\EntityManager $db)
    {
        $this->db = $db;
    }

    public function getCommentById($id)
    {
        try {
            $query = $this->db->createQuery("SELECT c,ue FROM App\Model\Comment AS c JOIN c.user AS ue WHERE c.comment_id = :id");
            $query->setParameter('id', $id);
            $comment = $query->getOneOrNullResult();
        } catch (\Exception $e) {
            $comment = null;
        }
        
        return $comment;
    }

}
