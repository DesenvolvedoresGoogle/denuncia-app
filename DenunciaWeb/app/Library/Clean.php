<?php
namespace App\Library;

class Clean
{

    public static function cleanString($value)
    {
        $value = htmlspecialchars($value);
        $value = strip_tags($value);
        return $value;
    }
}