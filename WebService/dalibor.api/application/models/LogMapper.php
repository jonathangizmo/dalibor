<?php
require_once "Application_Model_Log.php";

class Application_Model_LogMapper 
{
    protected $_dbTable;
 
    public function setDbTable($dbTable)
    {
        if (is_string($dbTable)) {
            $dbTable = new $dbTable();
        }
        if (!$dbTable instanceof Zend_Db_Table_Abstract) {
            throw new Exception('Invalid table data gateway provided');
        }
        $this->_dbTable = $dbTable;
        return $this;
    }
 
    public function getDbTable()
    {
        if (null === $this->_dbTable) {
            $this->setDbTable('Application_Model_DbTable_Log');
        }
        return $this->_dbTable;
    }
 
    public function save(Application_Model_Log $log)
    {
        $data = array(
            'id'   => $log->getId(),
            'lat' => $log->getLat(),
            'lon'   => $log->getLon(),
            'logtime' => $log->getLogtime()
        );
 
        if (null === ($id = $log->getId())) {
            //unset($data['id']);
            $this->getDbTable()->insert($data);
        } else {
            $this->getDbTable()->update($data, array('id = ?' => $id));
        }
    }
 
    public function find($id, Application_Model_Log $log)
    {
        $result = $this->getDbTable()->find($id);
        if (0 == count($result)) {
            return;
        }
        $row = $result->current();
        $log->setId($row->id)
                ->setLat($row->lat)
                ->setLon($row->lon)
                ->setLogtime($row->logtime);
    }
 
    public function fetchAll()
    {
        $resultSet = $this->getDbTable()->fetchAll();
        $entries   = array();
        foreach ($resultSet as $row) {
            $entry = new Application_Model_Log();
            //echo $row->id;
            $entry->setId($row->id)
                  ->setLat($row->lat)
                  ->setLon($row->lon)
                  ->setLogtime($row->logtime);
            $entries[] = $entry;
        }
        return $entries;
    }
}

?>
