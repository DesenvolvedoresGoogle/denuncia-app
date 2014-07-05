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
        if ($tpl == '500.tpl')
            $this->output['erro'] = 'Internal Error';
        if ($tpl == '404.tpl')
            $this->output['erro'] = 'Not Found';
        
        if (isset($this->output['erro']))
            $this->output['status'] = 'fail';
        
        \App\Library\Clean::utf8EncodeArray($this->output);
        
        header('Content-Type: application/json');
        echo json_encode($this->output, JSON_PRETTY_PRINT);
    }
}
