<?php

class PollController extends Zend_Rest_Controller {

    public function getAction() {
        $response = $this->getResponse();
        $device = $this->_getParam ('id');
        $response->setHeader("move", "left");
        $response->setHeader("device", $device);
       return $response;
    }

    public function headAction() {
        
    }

    public function indexAction() {
        $user = new Application_Model_UserMapper();
        $this->view->entries = $user->fetchAll();
        return $this->getResponse()->setBody("index<br/>");
    }

    public function postAction() {
        return $this->getResponse()->setBody("POST<br/>");
    }

    public function putAction() {
        
    }

    public function deleteAction() {
        
    }

}

?>
