<?php
namespace App\Persistence;

use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;

class DoctrineUtil
{

    private $configuration;

    private $conn;

    private $sql_logger;

    public function __construct($config)
    {
        $isDevMode = true;
        $this->configuration = Setup::createAnnotationMetadataConfiguration(array(
            APP_PATH . '/Model'
        ), $isDevMode, null, null, false);
        
        $this->sql_logger = new \Doctrine\DBAL\Logging\DebugStack();
        $this->configuration->setSQLLogger($this->sql_logger);
        $this->configuration->addCustomNumericFunction('ACOS', 'App\\Library\\DoctrineExtensions\\Acos');
        $this->configuration->addCustomNumericFunction('COS', 'App\\Library\\DoctrineExtensions\\Cos');
        $this->configuration->addCustomNumericFunction('SIN', 'App\\Library\\DoctrineExtensions\\Sin');
        $this->configuration->addCustomNumericFunction('RADIANS', 'App\\Library\\DoctrineExtensions\\Radians');
        
        $this->conn = $config;
    }

    public function getDatabaseEntityManager()
    {
        $em = EntityManager::create($this->conn, $this->configuration);
        $em->getConnection()->connect();
        
        return $em;
    }

}