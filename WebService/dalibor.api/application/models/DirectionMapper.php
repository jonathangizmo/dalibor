<?php
require_once "Application_Model_Direction.php";

class Application_Model_DirectionMapper 
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
            $this->setDbTable('Application_Model_DbTable_Direction');
        }
        return $this->_dbTable;
    }
 
    public function save(Application_Model_Direction $direction)
    {
        $data = array(
            'id'   => $direction->getId(),
            'move' => $direction->getMove(),
            'message'   => $direction->getMessage(),
            'movetime' => $direction->getMovetime()
        );
 
        if (null === ($id = $direction->getId())) {
            //unset($data['id']);
            $this->getDbTable()->insert($data);
        } else {
            $this->getDbTable()->update($data, array('id = ?' => $id));
        }
    }
 
    public function find($id, Application_Model_Direction $direction)
    {
        $result = $this->getDbTable()->find($id);
        if (0 == count($result)) {
            return;
        }
        $row = $result->current();
        $direction->setId($row->id)
                ->setMove($row->move)
                ->setMessage($row->message)
                ->setMovetime($row->movetime);
    }
 
    public function fetchAll()
    {
        $resultSet = $this->getDbTable()->fetchAll();
        $entries   = array();
        foreach ($resultSet as $row) {
            $entry = new Application_Model_Direction();
            //echo $row->id;
            $entry->setId($row->id)
                  ->setMove($row->move)
                  ->setMessage($row->message)
                  ->setMovetime($row->movetime);
            $entries[] = $entry;
        }
        return $entries;
    }
}

?>
