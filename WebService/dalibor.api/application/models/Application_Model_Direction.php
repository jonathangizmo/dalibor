<?php

class Application_Model_Direction {
    protected $_id;
    protected $_move;
    protected $_message;
    protected $_movetime;
    
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
 
 
    public function setMove($move)
    {
        $this->_move = (string) $move;
        return $this;
    }
 
    public function getMove()
    {
        return $this->_move;
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
    
    public function setMessage($message)
    {
        $this->_message = $message;
        return $this;
    }
 
    public function getMessage()
    {
        return $this->_message;
    }
    
    public function setMoveTime($movetime)
    {
        $this->_movetime = $movetime;
        return $this;
    }
 
    public function getMoveTime()
    {
        return $this->_movetime;
    }
}

?>
