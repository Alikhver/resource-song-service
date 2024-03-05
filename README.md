
# Resource Song Services

There are 2 services: resource and songs. Resource service is designed to store song data in bytes. Song service is for storing metadata for this song. 


ports:

Resource service is on port: 7080
resource db is on port: 7432

Song service is on port: 7081
song db is on port: 8432
    

postman collection: 
```json
	"item": [
		{
			"name": "Song requests",
			"item": [
				{
					"name": "get info by info id",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7081/songs/8",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7081",
							"path": [
								"songs",
								"8"
							]
						}
					},
					"response": []
				},
				{
					"name": "create info",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"name\": \"We are the champions\",\r\n    \"artist\": \"Queen\",\r\n    \"album\": \"News of the world\",\r\n    \"length\": \"2:59\",\r\n    \"resourceId\": \"123432\",\r\n    \"year\": \"1977\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:7081/songs",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7081",
							"path": [
								"songs"
							]
						}
					},
					"response": []
				},
				{
					"name": "delete info",
					"request": {
						"method": "DELETE",
						"header": [],
						"body": {
							"mode": "formdata",
							"formdata": [
								{
									"key": "file",
									"type": "file",
									"src": []
								}
							]
						},
						"url": {
							"raw": "http://localhost:7081/songs?ids=4,87fd",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7081",
							"path": [
								"songs"
							],
							"query": [
								{
									"key": "ids",
									"value": "4,87fd"
								}
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Resource requests",
			"item": [
				{
					"name": "get resource by id",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:7080/resources/7",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7080",
							"path": [
								"resources",
								"7"
							]
						}
					},
					"response": []
				},
				{
					"name": "create resource",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "file",
							"file": {
								"src": "/C:/Users/alikhver/Desktop/file_Jingle Bells with Lyrics _ Kids Christmas Songs _ Christmas Carols 2018 (128 kbps).mp3"
							}
						},
						"url": {
							"raw": "http://localhost:7080/resources",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7080",
							"path": [
								"resources"
							]
						}
					},
					"response": []
				},
				{
					"name": "delete resource",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:7080/resources?ids=1,3,7,5,2",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "7080",
							"path": [
								"resources"
							],
							"query": [
								{
									"key": "ids",
									"value": "1,3,7,5,2"
								}
							]
						}
					},
					"response": []
				}
			]
		}
	]
```
