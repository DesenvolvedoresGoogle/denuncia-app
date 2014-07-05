<?php
namespace App\View;

class APIView implements iView
{

    private $output;

    public function __construct()
    {
        $this->output = array();
    }

    public function assign($key, $value)
    {
        $this->output[$key] = $value;
    }

    public function display($tpl = null)
    {        
        header('Content-Type: application/json');
        echo json_encode($this->output, JSON_PRETTY_PRINT);
    }
}
