<?php

//Including db.php for login purpose
include_once 'db.php';

class User{
	
	//creating a variable to hold an instance of database.
	private $db;
	//table name we are using to add data of username and password
	private $db_table = "users";
	

	public function __construct(){
		$this->db = new DbConnect();
	}
	
	//Api function for login
	public function isLoginExist($username, $password){		
		
		//if username and password passed matches with uesrs record then find 1, 
		$query = "select * from " . $this->db_table . " where username = '$username' AND password = '$password' Limit 1";
		
		//result is saved 
		$result = mysqli_query($this->db->getDb(), $query);
		
		//if you found more than 0 , then yes It exists with return true
		if(mysqli_num_rows($result) > 0){
			mysqli_close($this->db->getDb());
			return true;
		}		
		mysqli_close($this->db->getDb());
		return false;		
	}
	
	//registeration 
	public function createNewRegisterUser($username, $password, $email){
			
		//query inserting email, username and password to users table	
		$query = "insert into users (username, password, email, created_at, updated_at) values ('$username', '$password', '$email', NOW(), NOW())";
		//getDb -- instance of database, with the query above passed in .
		$inserted = mysqli_query($this->db->getDb(), $query);
		
		//1 = success , JSON , so in Android we can get this value in JSON 1, then this means, registration successfully done..
		if($inserted == 1){
			$json['success'] = 1;									
		}else{
			$json['success'] = 0;
		}
		//closing the connection
		mysqli_close($this->db->getDb());
	
		//returning Json object with success = 1 or 0 (true/false);
		return $json;
	}
	
	//Login purpose
	public function loginUsers($username, $password){
			
		$json = array();
		//if isLoginExist returns true, then 
		//$canUserLogin = true 
		// so, if that's true, then success = 1 , so user can login. 
		$canUserLogin = $this->isLoginExist($username, $password);
		if($canUserLogin){
			$json['success'] = 1;
		}else{
			$json['success'] = 0;
		}
		return $json;
	}

}


?>