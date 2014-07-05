<?php
namespace App\Persistence;

abstract class BaseDAO
{

    protected $db;

    public function save($object)
    {
        try {
            $this->db->persist($object);
            $this->db->flush();
        } catch (\Exception $exception) {
            throw $exception;
        }
    }
}
