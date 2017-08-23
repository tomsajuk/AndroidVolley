var express = require('express');
var app = express();
var http = require('http');
 
var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/Basket";

app.get('/search/:barcode', function (req, res) {

	var code = parseInt(req.params.barcode);
	
	MongoClient.connect(url, function(err, db) {
	  if (err) throw err;
	  db.collection("Inventory").find({Barcode: code }, { _id: false}).toArray( function(err, result) {
	    if (err) throw err;
	    console.log(result);
	    res.json(result);
	    db.close();
	    res.end();
	  });
	});
});

app.listen(8080, function () {
	console.log("Listenning to port 8080");
});