<?php

class PollController extends Zend_Rest_Controller {

    public function getAction() {
        $response = $this->getResponse();
        $device = $this->_getParam ('id');
        $response->setHeader("device", $device);
        $directionMapper = new Application_Model_DirectionMapper();
        $direction = new Application_Model_Direction();
        $directionMapper->find($device, $direction);
        if((time()-($direction->getMoveTime())<5))
        {
            $response->setHeader("move", $direction->getMove());
        }
        else
        {
            $response->setHeader("move", "0000");   
        }
        $response->setHeader("message",$direction->getMessage());
        $response->setHeader("session", $direction->getMove());
        $response->setHeader("movetime", $direction->getMoveTime());
        $lat = $_GET['lat'];
        $lon = $_GET['lon'];
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
        
        $incoming = file_get_contents("php://input");
        $json = json_decode($incoming,true);
        $userMapper = new Application_Model_UserMapper();
        $user = new Application_Model_User();
        $dir = new Application_Model_DirectionMapper();
        $dirData = new Application_Model_Direction();
        $username = $json["username"];
        $move = $json["move"];
        $message = $json["message"];
        $sessionkey = $json["sessionkey"];
        $userMapper->find($username, $user);
        if($sessionkey == $user->getSessionkey() && $sessionkey!="0")
        {
        $dirData->setId($username);
        $dirData->setMove($move);
        $dirData->setMessage($message);
        $dirData->setMoveTime(time());
        $dir->save($dirData);
        $responseData["AOK"]="true";
        return $this->getResponse()->setBody(json_encode($responseData));
        }
        $responseData["AOK"]="false";
        return $this->getResponse()->setBody(json_encode($responseData));
    }

    public function putAction() {
        
    }

    public function deleteAction() {
        
    }

}

?>
