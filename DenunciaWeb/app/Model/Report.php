<?php
namespace App\Model;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;

/**
 * Model\Report
 *
 * @ORM\Entity()
 * @ORM\Table(name="report", indexes={@ORM\Index(name="fk_report_user1_idx", columns={"user"})})
 */
class Report
{

    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    protected $report_id;

    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $title;

    /**
     * @ORM\Column(type="text")
     */
    protected $description;

    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $photo;
    
    /**
     * @ORM\OneToMany(targetEntity="Comment", mappedBy="report")
     * @ORM\JoinColumn(name="report", referencedColumnName="report_id", nullable=false)
     */
    protected $comments;

    public function __construct()
    {
        $this->comments = new ArrayCollection();
    }

    public function setReportId($report_id)
    {
        $this->report_id = $report_id;
        
        return $this;
    }

    public function getReportId()
    {
        return $this->report_id;
    }

    public function setTitle($title)
    {
        $this->title = $title;
        
        return $this;
    }

    public function getTitle()
    {
        return $this->title;
    }

    public function setDescription($description)
    {
        $this->description = $description;
        
        return $this;
    }

    public function getDescription()
    {
        return $this->description;
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

    public function addComment(Comment $comment)
    {
        $this->comments[] = $comment;
        
        return $this;
    }

    /**
     *
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getComments()
    {
        return $this->comments;
    }
}