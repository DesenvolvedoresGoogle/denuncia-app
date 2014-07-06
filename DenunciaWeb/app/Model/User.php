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
     * @ORM\Column(type="string", length=255)
     */
    protected $photo;

    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $google_id;

    /**
     * @ORM\Column(type="string", length=50)
     */
    protected $token;

    /**
     * @ORM\OneToMany(targetEntity="Report", mappedBy="user")
     * @ORM\JoinColumn(name="user", referencedColumnName="user_id", nullable=false)
     */
    protected $reports;

    /**
     * @ORM\OneToMany(targetEntity="Comment", mappedBy="user")
     * @ORM\JoinColumn(name="user", referencedColumnName="user_id", nullable=false)
     */
    protected $comments;

    public function __construct($infos = null)
    {
        if (is_array($infos)) {
            $this->google_id = (isset($infos['google_id']) ? $infos['google_id'] : null);
            $this->name = (isset($infos['name']) ? $infos['name'] : null);
            $this->photo = (isset($infos['photo']) ? $infos['photo'] : null);
        }
        
        $this->reports = new ArrayCollection();
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
    
    public function setPhoto($photo)
    {
        $this->photo = $photo;
    
        return $this;
    }
    
    public function getPhoto()
    {
        return $this->photo;
    }

    public function setGoogleId($google_id)
    {
        $this->google_id = $google_id;
        
        return $this;
    }

    public function getGoogleId()
    {
        return $this->google_id;
    }

    public function setToken($token)
    {
        $this->token = $token;
        
        return $this;
    }

    public function getToken()
    {
        return $this->token;
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

    public function addReport(Report $report)
    {
        $this->reports[] = $report;
        
        return $this;
    }

    /**
     *
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getReports()
    {
        return $this->reports;
    }
    
    public function toArray()
    {
        return array(
            'user_id' => $this->user_id,
            'name' => $this->name,
            'photo' => $this->photo,
            'google_id' => $this->google_id
        );
    }
}