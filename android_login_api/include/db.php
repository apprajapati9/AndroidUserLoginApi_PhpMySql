<?php

//including config.php file to get the database credentials we saved earliar 
include_once 'config.php';

class DbConnect{
	
	
	private $connect;
	
	
	public function __construct(){
		
		//connecting using the parameters passed in config.php
		$this->connect = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
		 
		 //if error post it on server
		if (mysqli_connect_errno($this->connect))
		{
			echo "Failed to connect to MySQL: " . mysqli_connect_error();  
		}
	}
	
	//if no, get the connection.
	public function getDb(){
		
		return $this->connect;
	}
	
}
