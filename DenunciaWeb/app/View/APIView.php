<?php
namespace App\View;

class APIView implements iView
{

    private $output;

    public function __construct()
    {
        $this->output = array();
        $this->output['status'] = 'sucess';
    }

    public function assign($key, $value)
    {
        $this->output[$key] = $value;
    }

    public function display($tpl = null)
    {        
        if(isset($this->output['erro']))
            $this->output['status'] = 'fail';
        header('Content-Type: application/json');
        echo json_encode($this->output, JSON_PRETTY_PRINT);
    }
}
