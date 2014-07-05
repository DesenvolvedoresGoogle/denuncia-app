<?php
namespace App\View;

class View
{

    private $smarty;

    public function __construct()
    {
        $this->smarty = new \Smarty();
        $this->smarty->setTemplateDir(APP_PATH . '/View/Template/');
        $this->smarty->setCompileDir(CACHE_PATH . '/tpl_comp/');
        $this->assign('url', URL);
    }

    public function assign($var, $value)
    {
        $this->smarty->assign($var, $value);
    }

    public function display($tpl)
    {
        $this->smarty->display($tpl);
    }

    public function setTitle($value, $opt = 0)
    {
        $this->assign('title', $value);
    }
    
}
