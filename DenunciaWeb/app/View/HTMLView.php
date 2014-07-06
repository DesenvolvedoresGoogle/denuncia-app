<?php
namespace App\View;

class HTMLView implements iView
{

    private $smarty;

    public function __construct()
    {
        $this->smarty = new \Smarty();
        $this->smarty->setTemplateDir(APP_PATH . '/View/Template/');
        $this->smarty->setCompileDir(CACHE_PATH . '/tpl_comp/');
        $this->assign('url', URL);
        $this->assign('active_link', '');
    }

    public function assign($key, $value)
    {
        $this->smarty->assign($key, $value);
    }

    public function display($tpl = null)
    {
        $this->smarty->display($tpl);
    }
    
    public function setActiveLink($value)
    {
        $this->assign('active_link', $value);
    }
}
