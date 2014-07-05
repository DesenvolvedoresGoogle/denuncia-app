<?php
namespace App\View;

interface iView
{
    public function assign($key, $value);
    public function display($tpl);
}