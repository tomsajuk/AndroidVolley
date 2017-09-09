var express = require('express');
var router = express.Router();
var MongoClient = require('mongodb').MongoClient;

var url = "mongodb://localhost:27017/Basket";

router.get('/code/:barcode', function(req, res, next) {
	var code = parseInt(req.params.barcode);
	
	MongoClient.connect(url, function(err, db) {
	  if (err) throw err;
	  db.collection("Inventory").find({Barcode: code }, { _id: false}).toArray( function(err, result) {
	    if (err) throw err;
	    console.log(result);
	    res.json(result);
	    db.close();
	    res.status(200);
	    res.end();
	  });
	});
});

router.get('/name/:name', function(req, res, next) {
	var product_name = req.params.name;
	MongoClient.connect(url, function(err, db) {
	  if (err) throw err;
	  db.collection("Inventory").find({"Product Name":{$regex: product_name, $options:"$i" }}, { _id: false}).toArray( function(err, result) {
	    if (err) throw err;
	    console.log(result);
	    db.close();

	    if(result.length == 0) {
	    	res.status(202);
	    }
	    else {
		    res.json(result);
		    res.status(200);
		}
		res.end();
		
	  });
	});
});

module.exports = router;
