<?php
namespace App\Model;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;

/**
 * Model\Comment
 *
 * @ORM\Entity()
 * @ORM\Table(name="comment", indexes={@ORM\Index(name="fk_comment_user1_idx", columns={"user"}), @ORM\Index(name="fk_comment_report_idx", columns={"report"})})
 */
class Comment
{

    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    protected $comment_id;

    /**
     * @ORM\Column(type="text")
     */
    protected $comment;

    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $photo;

    /**
     * @ORM\ManyToOne(targetEntity="User", inversedBy="comments")
     * @ORM\JoinColumn(name="user", referencedColumnName="user_id", onDelete="CASCADE", nullable=false)
     */
    protected $user;

    /**
     * @ORM\ManyToOne(targetEntity="Report", inversedBy="comments")
     * @ORM\JoinColumn(name="report", referencedColumnName="report_id", onDelete="CASCADE", nullable=false)
     */
    protected $report;

    public function __construct()
    {}

    public function setCommentId($comment_id)
    {
        $this->comment_id = $comment_id;
        
        return $this;
    }

    public function getCommentId()
    {
        return $this->comment_id;
    }

    public function setComment($comment)
    {
        $this->comment = $comment;
        
        return $this;
    }

    public function getComment()
    {
        return $this->comment;
    }

    public function setPhoto($photo)
    {
        $this->photo = $photo;
        
        return $this;
    }

    public function getPhoto()
    {
        return $this->photo;
    }

    public function setUser(User $user = null)
    {
        $this->user = $user;
        
        return $this;
    }

    /**
     *
     * @return \Model\User
     */
    public function getUser()
    {
        return $this->user;
    }

    public function setReport(Report $report = null)
    {
        $this->report = $report;
        
        return $this;
    }

    /**
     *
     * @return \Model\Report
     */
    public function getReport()
    {
        return $this->report;
    }
    
    public function toArray()
    {
        return array(
            'comment_id' => $this->comment_id,
            'comment' => $this->comment,
            'photo' => $this->photo,
            'user' => $this->user->toArray()
        );
    }
}