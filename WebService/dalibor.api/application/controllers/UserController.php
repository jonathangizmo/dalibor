<?php

class UserController extends Zend_Rest_Controller
{

    public function getAction() {
    }

    public function headAction() {
        
    }

    public function indexAction() {
    }

    public function postAction() {
        $incoming = file_get_contents("php://input");
        $json = json_decode($incoming,true);
        $userMapper = new Application_Model_UserMapper();
        $user = new Application_Model_User();
        $this->view->entries = $userMapper->find($json["username"], $user);
        $responseData["auth"]="false";
        $responseData["sessionkey"]="0";
        if($json["password"] == $user->getPassword()){
            $responseData["auth"]="true";
            $responseData["sessionkey"]="12345";
        }
        return $this->getResponse()->setBody(json_encode($responseData));
    }

    public function putAction() {
        
    }

    public function deleteAction() {
        
    }
    
    private function generateToken() {
        
    }

}

?>
