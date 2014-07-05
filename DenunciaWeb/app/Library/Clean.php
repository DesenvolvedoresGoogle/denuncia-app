<?php
namespace App\Library;

class Clean
{

    public static function cleanString(&$value)
    {
        $value = strip_tags($value);
        $value = htmlspecialchars($value);
        $value = trim($value);
        return $value;
    }
    
    public static function cleanArray(&$array)
    {
        array_walk_recursive($array, '\\App\\Library\\Clean::cleanString');
    }
    
    private static function utf8encode(&$value)
    {
        $value = utf8_encode($value);
    }
    
    public static function utf8EncodeArray(&$array)
    {
        array_walk_recursive($array, '\\App\\Library\\Clean::utf8encode');
    }
    
}