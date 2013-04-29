<?php
class Application_Model_User {
    protected $_password;
    protected $_id;
    protected $_sessionkey;
    protected $_sessiontime;
 
    public function __construct(array $options = null)
    {
        if (is_array($options)) {
            $this->setOptions($options);
        }
    }
 
    public function __set($name, $value)
    {
        $method = 'set' . $name;
        if (('mapper' == $name) || !method_exists($this, $method)) {
            throw new Exception('Invalid user property');
        }
        $this->$method($value);
    }
 
    public function __get($name)
    {
        $method = 'get' . $name;
        if (('mapper' == $name) || !method_exists($this, $method)) {
            throw new Exception('Invalid user property');
        }
        return $this->$method();
    }
 
    public function setOptions(array $options)
    {
        $methods = get_class_methods($this);
        foreach ($options as $key => $value) {
            $method = 'set' . ucfirst($key);
            if (in_array($method, $methods)) {
                $this->$method($value);
            }
        }
        return $this;
    }
 
 
    public function setPassword($password)
    {
        $this->_password = (string) $password;
        return $this;
    }
 
    public function getPassword()
    {
        return $this->_password;
    }

 
    public function setId($id)
    {
        $this->_id = $id;
        return $this;
    }
 
    public function getId()
    {
        return $this->_id;
    }
    
    public function setSessionkey($sessionkey)
    {
        $this->_sessionkey = $sessionkey;
        return $this;
    }
 
    public function getSessionkey()
    {
        return $this->_sessionkey;
    }
    
    public function setSessiontime($sessiontime)
    {
        $this->_sessiontime = $sessiontime;
        return $this;
    }
 
    public function getSessiontime()
    {
        return $this->_sessiontime;
    }
}

?>
