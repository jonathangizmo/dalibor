<?php

class Application_Model_Log {
    protected $_id;
    protected $_lat;
    protected $_lon;
    protected $_logtime;
    
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
 
 
    public function setLat($lat)
    {
        $this->_lat = (string) $lat;
        return $this;
    }
 
    public function getLat()
    {
        return $this->_lat;
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
    
    public function setLon($lon)
    {
        $this->_lon = $lon;
        return $this;
    }
 
    public function getLon()
    {
        return $this->_lon;
    }
    
    public function setLogtime($logtime)
    {
        $this->_logtime = $logtime;
        return $this;
    }
 
    public function getLogtime()
    {
        return $this->_logtime;
    }
}

?>
