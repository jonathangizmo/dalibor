<?php

class Application_Model_User {

    protected $_password;
    protected $_id;
 
    public function __set($name, $value);
    public function __get($name);
 
    public function setPassword($email);
    public function getPassword();
 
    public function setId($id);
    public function getId();
}

class Application_Model_UserMapper{
    
    public function save(Application_Model_User $guestbook);
    public function find($id);
    public function fetchAll();
}

?>
