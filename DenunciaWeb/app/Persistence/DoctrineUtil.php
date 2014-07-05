<?php
namespace App\Persistence;

use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;

class DoctrineUtil
{

    private $annotation;

    private $conn;

    private $sql_logger;

    public function __construct($config)
    {
        $isDevMode = true;
        $this->annotation = Setup::createAnnotationMetadataConfiguration(array(
            APP_PATH . '/Model'
        ), $isDevMode, null, null, false);
        
        $this->sql_logger = new \Doctrine\DBAL\Logging\DebugStack();
        $this->annotation->setSQLLogger($this->sql_logger);
        
        $this->conn = $config;
    }

    public function getDatabaseEntityManager()
    {
        $em = EntityManager::create($this->conn, $this->annotation);
        $em->getConnection()->connect();
        
        return $em;
    }
}