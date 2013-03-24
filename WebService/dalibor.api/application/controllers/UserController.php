<?php

class UserController extends Zend_Rest_Controller
{

    public function getAction() {
       return $this->getResponse()->setBody("GET<br/>");
    }

    public function headAction() {
        
    }

    public function indexAction() {
        return $this->getResponse()->setBody("index<br/>");
    }

    public function postAction() {
        $incoming = file_get_contents("php://input");
        $json = json_decode($incoming,true);
        $userMapper = new Application_Model_UserMapper();
        $user = new Application_Model_User();
        $this->view->entries = $userMapper->find($json["username"], $user);;
        if($json["password"] == $user->getPassword()) return $this->getResponse()->setBody("true");
        return $this->getResponse()->setBody("false");
    }

    public function putAction() {
        
    }

    public function deleteAction() {
        
    }

}

?>
