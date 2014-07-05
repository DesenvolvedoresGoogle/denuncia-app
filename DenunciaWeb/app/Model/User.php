<?php
namespace App\Model;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;

/**
 * Model\User
 *
 * @ORM\Entity()
 * @ORM\Table(name="user")
 */
class User
{

    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    protected $user_id;

    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $name;

    /**
     * @ORM\OneToMany(targetEntity="Comment", mappedBy="user")
     * @ORM\JoinColumn(name="user", referencedColumnName="user_id", nullable=false)
     */
    protected $comments;

    public function __construct()
    {
        $this->comments = new ArrayCollection();
    }

    public function setUserId($user_id)
    {
        $this->user_id = $user_id;
        
        return $this;
    }

    public function getUserId()
    {
        return $this->user_id;
    }

    public function setName($name)
    {
        $this->name = $name;
        
        return $this;
    }

    public function getName()
    {
        return $this->name;
    }

    public function addComment(Comment $comment)
    {
        $this->comments[] = $comment;
        
        return $this;
    }

    /**
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getComments()
    {
        return $this->comments;
    }
}