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
     * @ORM\Column(type="float")
     */
    protected $latitude;

    /**
     * @ORM\Column(type="float")
     */
    protected $longitude;

    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $address;

    /**
     * @ORM\Column(type="datetime")
     */
    protected $creation_date;

    /**
     * @ORM\ManyToOne(targetEntity="User", inversedBy="reports")
     * @ORM\JoinColumn(name="user", referencedColumnName="user_id", onDelete="CASCADE", nullable=false)
     */
    protected $user;

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

    public function setLatitude($latitude)
    {
        $this->latitude = $latitude;
        
        return $this;
    }

    public function getLatitude()
    {
        return $this->latitude;
    }

    public function setLongitude($longitude)
    {
        $this->longitude = $longitude;
        
        return $this;
    }

    public function getLongitude()
    {
        return $this->longitude;
    }

    public function setAddress($address)
    {
        $this->address = $address;
        
        return $this;
    }

    public function getAddress()
    {
        return $this->address;
    }

    public function setCreationDate($creation_date)
    {
        $this->creation_date = $creation_date;
        
        return $this;
    }

    public function getCreationDate()
    {
        return $this->creation_date;
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

    public function toArray($try_comment = false)
    {
        $return = array(
            'report_id' => $this->report_id,
            'title' => $this->title,
            'description' => $this->description,
            'photo' => $this->photo,
            'latitude' => $this->latitude,
            'longitude' => $this->longitude,
            'address' => $this->address,
            'creation_date' => $this->creation_date->getTimestamp(),
            'user' => $this->user->toArray(),
            'comments' => array()
        );
        if ($try_comment) {
            if (count($this->comments) > 0) {
                foreach ($this->comments as $comment) {
                    $return['comments'][] = $comment->toArray();
                }
            }
        }
        
        return $return;
    }
}